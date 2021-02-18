package android_serialport_api;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rairmmd.serialport.ByteUtil;
import com.rairmmd.serialport.Constant;
import com.rairmmd.serialport.OnDataReceiverListener;
import com.rairmmd.serialport.LibTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import android_serialport_api.bean.Data;

public class LockerControl {

    private static final String TAG = "LockerControl";

    byte[] buffer = new byte[50];
    private String mDeviceName;
    private int mBaudRate;
    private SerialPort mSerialPort;
    private SerialPortFinder mSerialPortFinder;

    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadCOMThread mReadCOMThread;
    private SendCOMThread mSendCOMThread;

    private OnDataReceiverListener onDataReceiverListener;
    private int overtimeCount = 0;

    private BlockingQueue<Data> highPriorityQueue;
    private BlockingQueue<Data> lowPriorityQueue;
    private volatile boolean isSendComplete = false;
    private volatile int currentHandle = -1;
    private final int OVERTIME = 999;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == OVERTIME) {
                isSendComplete = false;
                onDataReceiverListener.onDataReceiver(new byte[]{0x09}, 1, getCurrentHandle());
            }
            return false;
        }
    }).get());


    public LockerControl(String devName, int baudRate) {
        LibTools.writeBehaviorLog(devName);
        mSerialPortFinder = new SerialPortFinder();
        mDeviceName = devName;
        mBaudRate = baudRate;
        mSerialPort = null;
        highPriorityQueue = new LinkedBlockingQueue<>(100);
        lowPriorityQueue = new LinkedBlockingQueue<>(100);
    }


    /**
     * 打开串口
     *
     * @return 是否成功
     */
    public boolean openCOM() {
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
                mReadCOMThread = new ReadCOMThread();
                mReadCOMThread.start();
                mSendCOMThread = new SendCOMThread();
                mSendCOMThread.start();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                mSerialPort = null;
            }
        }
        return false;
    }

    private String[] getCOMList() {
        return mSerialPortFinder.getAllDevicesPath();
    }

    /**
     * 关闭串口
     */
    public void closeCOM() {
        if (mSerialPort != null) {
            mReadCOMThread.interrupt();
            mSendCOMThread.interrupt();
            mSerialPort.closeIOStream();
            mSerialPort.close();
            mSerialPort = null;
        }
    }


    public void addData(byte[] sendData, boolean isHighPriorityCMD, int handle) {
        Data data = new Data();
        data.setData(sendData);
        data.setHandle(handle);
        if (isHighPriorityCMD) {
            data.setTag(1);
            highPriorityQueue.offer(data);
        } else {
            data.setTag(0);
            lowPriorityQueue.offer(data);
        }
    }

    /**
     * 发送报文
     *
     * @param data 报文
     * @return 是否成功
     */
    private boolean sendCMD(byte[] data) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(data);
                mOutputStream.flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    private class SendCOMThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                if (!isSendComplete) {
                    Data tempOrder;
                    if (!highPriorityQueue.isEmpty()) {
                        isSendComplete = true;
                        tempOrder = removeHighPriorityData();
                    } else if (!lowPriorityQueue.isEmpty()) {
                        isSendComplete = true;
                        tempOrder = removeLowPriorityData();
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    if (tempOrder != null && tempOrder.getData() != null) {
                        synchronized (mReadCOMThread) {
                            try {
                                mReadCOMThread.notify();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        overtimeCount = 0;
                        setCurrentHandle(tempOrder.getHandle());
                        sendCMD(tempOrder.getData());
                        Log.e("COM>>>CMD>>", ByteUtil.byteToHex(tempOrder.getData().length, tempOrder.getData()));
                        LibTools.byteToHex("发送数据：=====>", tempOrder.getData().length, tempOrder.getData());
                        mHandler.sendEmptyMessageDelayed(OVERTIME, 5000);
                    }
                }
            }
        }
    }

    /**
     * 读取串口数据
     */
    private class ReadCOMThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) {
                        break;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //无数据超时计时
                    if (mInputStream.available() == 0) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        overtimeCount++;
                        if (overtimeCount < 25) {
                            continue;
                        }
                        onDataReceiverListener.onDataReceiver(new byte[]{0x09}, 1, getCurrentHandle());
                        isSendComplete = false;
                        overtimeCount = 0;
                        synchronized (mReadCOMThread) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        overtimeCount = 0;
                    }
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        LibTools.byteToHex("接收到的数据：=====>", size, buffer);
                        int result = onDataReceiverListener.onDataReceiver(buffer, size, getCurrentHandle());
                        if (result == Constant.CheckResultTYPE.RESULT_OK) {
                            mHandler.removeMessages(OVERTIME);
                            onDataReceiverListener.onSendComplete(buffer, size, getCurrentHandle());
                            isSendComplete = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置回调监听
     *
     * @param onDataReceiverListener onDataReceiverListener
     */
    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }


    public Data removeHighPriorityData() {
        return highPriorityQueue.poll();
    }

    public Data removeLowPriorityData() {
        return lowPriorityQueue.poll();
    }

    public int getCurrentHandle() {
        return currentHandle;
    }

    public void setCurrentHandle(int currentHandle) {
        this.currentHandle = currentHandle;
    }

}

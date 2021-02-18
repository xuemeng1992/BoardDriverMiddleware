package android_serialport_api;

import android.content.Context;
import android.util.Log;

import com.rairmmd.serialport.ByteUtil;
import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rairmmd.serialport.LibTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommonControl {

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    public ReadCOMThread mReadCOMThread;
    private int overtimeCount = 0;
    byte[] buffer = new byte[500];
    private String mDeviceName;
    private int mBaudRate;
    private int overTime;
    private SerialPortFinder mSerialPortFinder;
    private CommonDataReceiverListener commonDataReceiverListener;


    public CommonControl(String devName, int baudRate, int overTime, Context context) {
        this.mDeviceName = devName;
        this.mBaudRate = baudRate;
        this.overTime = overTime;
        mSerialPort = null;
        mSerialPortFinder = new SerialPortFinder();
        boolean isOpen = openCOM();
        if (!isOpen) {
            Log.e("CommonControl", "打开串口失败");
            return;
        }
    }

    /**
     * 打开串口
     *
     * @return 是否成功
     */
    private boolean openCOM() {
        boolean isFindCom = false;
        String[] comList = getCOMList();
        for (String comname : comList) {
            if (comname.equals(mDeviceName.trim())) {
                isFindCom = true;
            }
        }
        if (!isFindCom) {
            return false;
        }
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                mSerialPort = null;
            }
        }
        return false;
    }

    public boolean reopenSerialPort() {
        try {
            return mSerialPort.reopenSerialPort(new File(mDeviceName), mBaudRate, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private String[] getCOMList() {
        return mSerialPortFinder.getAllDevicesPath();
    }

    public void start() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mReadCOMThread != null) {
            mReadCOMThread.cancel();
            mReadCOMThread = null;
        }
        mReadCOMThread = new ReadCOMThread();
        mReadCOMThread.start();
    }

    public void cancelReadThread() {
        if (mReadCOMThread != null) {
            mReadCOMThread.cancel();
        }
    }

    public boolean sendData(byte[] data) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(data);
                LibTools.byteToHex("CommonControl:Send :", data.length, data);
                mOutputStream.flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            Log.e("CommonControl:Send :", "SendData Exception");
            return false;
        }
        return true;
    }

    /**
     * 读取串口数据
     */
    class ReadCOMThread extends Thread {

        private boolean mRunning;

        public ReadCOMThread() {
            mRunning = true;
        }

        public void cancel() {
            mRunning = false;
            interrupt();
        }

        @Override
        public void run() {

            while (mRunning && !isInterrupted()) {
                try {
                    if (mInputStream == null) {
                        continue;
                    }
                    //无数据超时计时
                    if (mInputStream.available() == 0) {
                        if (overTime != 0) {
                            overtimeCount++;
                            if (overtimeCount < (overTime / 10)) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            commonDataReceiverListener.onDataReceiver(new byte[]{0x09}, 1);
                            overtimeCount = 0;
                            cancel();
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                    } else {
                        overtimeCount = 0;
                    }
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        LibTools.byteToHex("CommonControl:Recv :", buffer.length, buffer);
                        commonDataReceiverListener.onDataReceiver(buffer, size);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void closeCOM() {
        if (mReadCOMThread != null) {
            mReadCOMThread.cancel();
            mReadCOMThread = null;
        }
        if (mSerialPort != null) {
            mSerialPort.closeIOStream();
            mSerialPort = null;
        }
    }

    public void setCommonDataReceiverListener(CommonDataReceiverListener commonDataReceiverListener) {
        this.commonDataReceiverListener = commonDataReceiverListener;
    }

    public OutputStream getmOutputStream() {
        return mOutputStream;
    }

    public InputStream getmInputStream() {
        return mInputStream;
    }
}

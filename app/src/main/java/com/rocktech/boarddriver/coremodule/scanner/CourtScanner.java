package com.rocktech.boarddriver.coremodule.scanner;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.fcbox.locker.driver.IScannerCallback;
import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rairmmd.serialport.LibTools;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.HandlerThread;
import com.rocktech.boarddriver.tools.SerialPortTools;


import android_serialport_api.CommonControl;

public class CourtScanner implements CommonDataReceiverListener {

    private static final String TAG = "CourtScanner";
    private CommonControl scanner;
    private IScannerCallback callback;
    private Context context;
    private static byte[] start = null;
    private static byte[] stop = null;
    private static byte[] LISHIZHI = null;
    private HandlerThread handlerThread;
    private Handler mHandler;

    public CourtScanner(int duration, Context context) throws Exception {
        this.context = context;
        String scanType = ConfigureTools.getQrCode(context);
        if (scanType.equals(Constant.ScannerTYPE.HONEYWELL_SERIES) || scanType.equals(Constant.ScannerTYPE.HONEYWELL_USB)) {
            // 霍尼韦尔
            start = new byte[]{22, 84, 13};
            stop = new byte[]{22, 85, 13};
        } else if (scanType.equals(Constant.ScannerTYPE.DEWO)) {
            // 德沃
            start = new byte[]{0x1a, 0x54, 0x0d};
            stop = new byte[]{0x1a, 0x55, 0x0d};
        } else if (scanType.equals(Constant.ScannerTYPE._4102S)) {
            start = new byte[]{0x02, (byte) 0xF4, 0x03};
            stop = new byte[]{0x02, (byte) 0xF5, 0x03};
        }
        scanner = new CommonControl(ConfigureTools.getQrCodeTty(context), Constant.SCANNER_BAUDRATE, duration * 1000, context);
        Log.v(TAG, "===初始化成功!====");
        scanner.setCommonDataReceiverListener(this);
        handlerThread = new HandlerThread();
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {

            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            String res = new String(LISHIZHI, "GBK");
                            LibTools.writeLog("扫描到的内容 --> " + res);
                            if (callback != null) {
                                callback.onComplete(4224, "Success.", res.trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LISHIZHI = null;
                        // 关闭扫描
                        cancelScanner();
                        break;
                    case 2:
                        scanner.start();
                        break;
                    case 3:
                        cancelScanner();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onDataReceiver(byte[] buffer, int size) {
        if (LISHIZHI == null) {
            LISHIZHI = SerialPortTools.subBytes(buffer, 0, size);
            // 0.3s之后反馈结果
            mHandler.sendEmptyMessageDelayed(1, 300);
        } else {
            LISHIZHI = SerialPortTools.byteMerger(LISHIZHI, SerialPortTools.subBytes(buffer, 0, size));
        }
        if (LISHIZHI.length == 1 && LISHIZHI[0] == 9) {
            LISHIZHI = null;
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessage(3);
            return;
        }
        // 结尾不等于回车,继续接收
        if (LISHIZHI[LISHIZHI.length - 1] != 13) {
            mHandler.sendEmptyMessage(2);
        } else {
            // 结尾等于回车、删除回车
            LISHIZHI = SerialPortTools.subBytes(LISHIZHI, 0, LISHIZHI.length - 1);
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessage(1);
        }

    }

    public void cancelScanner() {
        scanner.cancelReadThread();
        scanner.sendData(stop);
        return;
    }

    public void closeSerialPort() {
        Log.v(TAG, "====关闭扫秒抢====");
        scanner.sendData(stop);
        scanner.closeCOM();
        return;
    }

    public void startScanner() {
        Log.v(TAG, "===开始发光并扫码====");
        scanner.sendData(start);
        scanner.start();
        return;
    }


    public void startScanner(IScannerCallback callback) {
        LibTools.writeLog("===开始发光并扫码====");
        this.callback = callback;
        scanner.start();
        scanner.sendData(start);
    }
}

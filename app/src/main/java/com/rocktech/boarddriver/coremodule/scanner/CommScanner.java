package com.rocktech.boarddriver.coremodule.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.SerialPortTools;

import android_serialport_api.CommonControl;

public class CommScanner implements CommonDataReceiverListener {

    private CommonControl scanner;
    private Context context;
    private static byte[] start;
    private static byte[] stop;
    private static byte[] manualMode;
    private static byte[] LISHIZHI;

    @SuppressLint("StaticFieldLeak")
    private static volatile CommScanner mCommScanner;

    public static CommScanner getInstance(int duration, Context context) {
        if (mCommScanner == null) {
            synchronized (CommScanner.class) {
                if (mCommScanner == null) {
                    mCommScanner = new CommScanner(duration, context);
                }
            }
        }
        return mCommScanner;
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent("android.intent.action.hal.barcodescanner.scandata");
                    try {
                        String content = new String(LISHIZHI, "GBK");
                        intent.putExtra("scandata", content);
                        context.sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LISHIZHI = null;
                    // 关闭扫描
                    cancelScanner();
                    break;
                case 2: {
                    scanner.start();
                    break;
                }
                case 3: {
                    cancelScanner();
                    break;
                }
            }
            return false;
        }
    });


    public CommScanner(int duration, Context context) {
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
        } else if (scanType.equals(Constant.ScannerTYPE.FM50)) {
            start = new byte[]{0x1B, 0x51};
            stop = new byte[]{0x1B, 0x50};
        } else if (scanType.equals(Constant.ScannerTYPE._4102S)) {
            start = new byte[]{0x02, (byte) 0xF4, 0x03};
            stop = new byte[]{0x02, (byte) 0xF5, 0x03};
        } else if (scanType.equals(Constant.ScannerTYPE.SPR4308)) {
            start = new byte[]{0x02, (byte) 0xF4, 0x03};
            stop = new byte[]{0x02, (byte) 0xF5, 0x03};
            manualMode = new byte[]{0x02, (byte) 0xF0, 0x03, '0', '9', '1', 'A', '0', '0', '.'};
        }
        scanner = new CommonControl(ConfigureTools.getQrCodeTty(context), Constant.SCANNER_BAUDRATE, duration * 1000, context);
        scanner.setCommonDataReceiverListener(this);
        if (scanType.equals(Constant.ScannerTYPE.SPR4308)) {
            scanner.start();
            scanner.sendData(manualMode);
        }
    }


    @Override
    public void onDataReceiver(byte[] buffer, int size) {
        if (ConfigureTools.getQrCode(context).equals(Constant.ScannerTYPE.FM50)) {
            if (LISHIZHI == null) {
                LISHIZHI = SerialPortTools.subBytes(buffer, 0, size);
                boolean b = true;
                for (int i = 0; i < LISHIZHI.length; i++) {
                    if (LISHIZHI[i] != 6) {
                        b = false;
                        mHandler.sendEmptyMessageDelayed(1, 300);
                        break;
                    }
                }
                if (b) {
                    LISHIZHI = null;
                    mHandler.sendEmptyMessage(2);
                    return;
                } else {
                    mHandler.sendEmptyMessage(2);
                }
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
        } else {
            if (LISHIZHI == null) {
                LISHIZHI = SerialPortTools.subBytes(buffer, 0, size);
                // 0.5s之后反馈结果
                mHandler.sendEmptyMessageDelayed(1, 500);
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
    }

    public void cancelScanner() {
        scanner.cancelReadThread();
        scanner.sendData(stop);
        LISHIZHI = null;
    }

    public void closeSerialPort() {
        scanner.sendData(stop);
        scanner.closeCOM();
        LISHIZHI = null;
        mCommScanner = null;
    }

    public void startScanner() {
        scanner.start();
        scanner.sendData(start);
    }
}

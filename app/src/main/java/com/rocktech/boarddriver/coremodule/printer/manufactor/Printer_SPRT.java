package com.rocktech.boarddriver.coremodule.printer.manufactor;

import android.content.Context;
import android.content.Intent;

import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android_serialport_api.CommonControl;

public class Printer_SPRT extends Printer implements CommonDataReceiverListener {

    private CommonControl printer;
    private Context context;
    private byte[] cmd = {0x10, 0x4, 0x4};
    private boolean status = false;

    public Printer_SPRT(Context context) {
        this.context = context;
        printer = new CommonControl(ConfigureTools.getPrinterTty(context), Constant.PRINTER_BAUDRATE, 5000, context);
        printer.setCommonDataReceiverListener(this);
    }

    @Override
    public void onDataReceiver(byte[] buffer, int size) {
        if (size == 1 && buffer[0] == 9) {
            return;
        }
        byte[] strData = new byte[size];
        for (int i = 0; i < size; i++) {
            strData[i] = buffer[i];
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.HASPAPER) {
            if ((getByte(strData[0], 5)) == 3) {
                // 发送没纸广播
                Intent intent = new Intent("android.intent.action.hal.printer.result.haspaper");
                intent.putExtra("haspaper", false);
                context.sendBroadcast(intent);
                return;
            }
            if ((getByte(strData[0], 5)) == 0) {
                // 发送有纸广播
                Intent intent = new Intent("android.intent.action.hal.printer.result.haspaper");
                intent.putExtra("haspaper", true);
                context.sendBroadcast(intent);
            }
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.NEEDMORE) {
            if (getByte(strData[0], 2) == 3) {
                // 发送纸将近广播
                Intent intent = new Intent("android.intent.action.hal.printer.result.needmore");
                intent.putExtra("needmore", true);
                context.sendBroadcast(intent);
                return;
            }
            if (getByte(strData[0], 2) == 0) {
                // 不发生纸将近广播
                Intent intent = new Intent("android.intent.action.hal.printer.result.needmore");
                intent.putExtra("needmore", false);
                context.sendBroadcast(intent);
                return;
            }
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.DATA2PRINT) {
            if ((1 & (strData[0] >> 4)) == 0) {
                status = true;
            }
        }
    }

    private byte getByte(byte paramByte, int paramInt) {
        return (byte) (0x3 & (paramByte >> paramInt));
    }

    @Override
    public void hasPaper() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.HASPAPER;
        printer.start();
        printer.sendData(cmd);
    }

    @Override
    public void hasPaperMore() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.NEEDMORE;
        printer.start();
        printer.sendData(cmd);
    }

    @Override
    public void print(final String str, int type) {
        Constant.PrinterState.HANDLE = Constant.PrinterState.DATA2PRINT;
        final byte[] query = {16, 6, 1};
        printer.start();
        printer.sendData(query);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (status) {
                    byte[] b;
                    byte[] bnew;
                    try {
                        b = str.getBytes("GBK");
                        if (b[b.length - 3] == b[b.length - 1]) {
                            bnew = Arrays.copyOfRange(b, 0, b.length - 2);
                        } else {
                            bnew = b;
                        }
                    } catch (UnsupportedEncodingException e1) {
                        return;
                    }
                    status = false;
                    printer.sendData(bnew);
                    try {
                        Thread.sleep(3000);
                        printer.start();
                        printer.sendData(query);
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (status) {
                        Intent intent = new Intent("android.intent.action.hal.printer.result.status");
                        intent.putExtra("status", true);
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent("android.intent.action.hal.printer.result.status");
                        intent.putExtra("status", false);
                        context.sendBroadcast(intent);
                    }
                }
            }
        }).start();
    }

    @Override
    public void paperSize() {
        Intent intent = new Intent("android.intent.action.hal.printer.supportsize.result");
        intent.putExtra("papersize", 0); // 0-3寸，1-4寸
        context.sendBroadcast(intent);
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public boolean hasPaperSyn() {
        return false;
    }

    @Override
    public boolean hasPaperMoreSyn() {
        return false;
    }

    @Override
    public boolean printSyn(String str, int type) {
        return false;
    }

    @Override
    public void closeSerialPort() {
        printer.closeCOM();
    }
}

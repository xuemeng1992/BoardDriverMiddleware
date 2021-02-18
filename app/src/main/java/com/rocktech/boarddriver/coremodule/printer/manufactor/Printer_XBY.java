package com.rocktech.boarddriver.coremodule.printer.manufactor;

import android.content.Context;
import android.content.Intent;

import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.opt.MyCpclCtrl;

import java.io.UnsupportedEncodingException;

import android_serialport_api.CommonControl;

public class Printer_XBY extends Printer implements CommonDataReceiverListener {


    private CommonControl printer;
    private Context context;
    private Intent intent;

    public Printer_XBY(Context context) {
        this.context = context;
        printer = new CommonControl(ConfigureTools.getPrinterTty(context), 19200, 5000, context);
        printer.setCommonDataReceiverListener(this);
    }

    @Override
    public void onDataReceiver(byte[] buffer, int size) {
        switch (Constant.PrinterState.HANDLE) {
            case Constant.PrinterState.HASPAPER:
                // 是否有纸
                intent = new Intent("android.intent.action.hal.printer.result.haspaper");
                intent.putExtra("haspaper", (buffer[0] & 0x60) != 0x60);
                context.sendBroadcast(intent);
                break;
            case Constant.PrinterState.STATUS:
                // 打印结果
                intent = new Intent("android.intent.action.hal.printer.result.status");
                intent.putExtra("status", (buffer[0] & 0x01) == 0x00);
                context.sendBroadcast(intent);
                break;
            case Constant.PrinterState.NEEDMORE:
                // 是否纸将近
                intent = new Intent("android.intent.action.hal.printer.result.needmore");
                intent.putExtra("needmore", (buffer[0] & 0x0C) == 0x0C);
                context.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void hasPaper() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.HASPAPER;
        byte[] szCmd = {0x10, 0x04, 0x04}; // 传输纸传感器状态
        printer.start();
        printer.sendData(szCmd);
    }

    @Override
    public void hasPaperMore() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.NEEDMORE;
        byte[] szCmd = {0x10, 0x04, 0x04}; // 传输纸传感器状态
        printer.start();
        printer.sendData(szCmd);
    }

    @Override
    public void print(String str, int type) {
        Constant.PrinterState.HANDLE = Constant.PrinterState.STATUS;
        byte[] szCmd = {0x10, 0x04, 0x05};// 传输打印结果
        byte[] qzCmd = {0x1D, 0x56, 0x42, 0x10}; // 切纸指令
        printer.sendData(new byte[]{0x1b, 0x40});//add by yazhou
        if (str != null && str.length() > 0) {
            byte[] total = GetPrintText(MyCpclCtrl.getCpclFromJson(str));
            int j = total.length / 800;
            for (int i = 0; i < j; i++) {
                printer.sendData(SerialPortTools.subBytes(total, 0, 800));
                total = SerialPortTools.subBytes(total, 800, total.length - 800);
            }
            if (total != null) {
                printer.sendData(SerialPortTools.byteMerger(total, qzCmd));
            } else {
                printer.sendData(qzCmd);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printer.start();
            printer.sendData(szCmd);
        }
    }

    @Override
    public void paperSize() {
        Intent intent = new Intent("android.intent.action.hal.printer.supportsize.result");
        intent.putExtra("papersize", 1); // 0-3寸，1-4寸
        context.sendBroadcast(intent);
    }

    @Override
    public void closeSerialPort() {
        printer.closeCOM();
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
    public String getVersion() {
        return null;
    }

    private byte[] GetPrintText(String strText) {
        byte[] strRst = null;
        try {
            strRst = strText.getBytes("GB18030");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strRst;
    }
}

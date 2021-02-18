package com.rocktech.boarddriver.coremodule.printer.manufactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android_serialport_api.CommonControl;
import shiyin.printer.ShiYinCommand;

public class Printer_SHIYIN extends Printer implements CommonDataReceiverListener {

    private CommonControl printer;
    private Context context;
    private ShiYinCommand mShiYinCommand;
    private ShiYinCommand.PaperState tPaperState = ShiYinCommand.PaperState.PaperHave;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;

    public Printer_SHIYIN(Context context) throws Exception {
        this.context = context;
        mShiYinCommand = new ShiYinCommand(context);
        printer = new CommonControl(ConfigureTools.getPrinterTty(context), Constant.PRINTER_BAUDRATE, 5000, context);
        printer.setCommonDataReceiverListener(this);
        mInputStream = (FileInputStream) printer.getmInputStream();
        mOutputStream = (FileOutputStream) printer.getmOutputStream();
    }

    @Override
    public void onDataReceiver(byte[] buffer, int size) {

    }

    @Override
    public void hasPaper() {
        tPaperState = ShiYinCommand.PaperState.fromInt(mShiYinCommand.ShiYin_CheckPaper(mInputStream, mOutputStream));
        Intent intent = new Intent("android.intent.action.hal.printer.result.haspaper");
        switch (tPaperState) {
            case PaperOut:
                intent.putExtra("haspaper", false);
                break;
            case PaperHave:
            case PaperLow:
            case PaperCoverOpen:
            case PaperJamOrOther:
            default:
                intent.putExtra("haspaper", true);
                break;
        }
        context.sendBroadcast(intent);
    }

    @Override
    public void hasPaperMore() {
        tPaperState = ShiYinCommand.PaperState.fromInt(mShiYinCommand.ShiYin_CheckPaper(mInputStream, mOutputStream));
        Intent intent = new Intent("android.intent.action.hal.printer.result.needmore");
        switch (tPaperState) {
            case PaperOut:
            case PaperLow:
                intent.putExtra("needmore", true);
                break;
            case PaperHave:
            case PaperCoverOpen:
            case PaperJamOrOther:
            default:
                intent.putExtra("needmore", false);
                break;
        }
        context.sendBroadcast(intent);
    }

    @Override
    public void print(String str, int type) {
        Intent intent = new Intent("android.intent.action.hal.printer.result.status");
        try {
            if (mOutputStream != null) {
                List<byte[]> jsonByteList = mShiYinCommand.ShiYin_parseFCboxJsonToShiYin(str);
                mOutputStream.write(mShiYinCommand.ShiYin_CreatePage(0, 0));
                for (byte[] json : jsonByteList) {
                    Log.v("zyz", "json string==" + new String(json));
                    mOutputStream.write(json);
                }
                mOutputStream.write(mShiYinCommand.ShiYin_PrintPage(1));
                try {
                    Thread.sleep(5500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra("status", true);
        context.sendBroadcast(intent);
    }

    @Override
    public void paperSize() {
        Intent intent = new Intent("android.intent.action.hal.printer.supportsize.result");
        intent.putExtra("papersize", 1); // 0-3寸，1-4寸
        context.sendBroadcast(intent);
    }

    @Override
    public void closeSerialPort() {

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
}

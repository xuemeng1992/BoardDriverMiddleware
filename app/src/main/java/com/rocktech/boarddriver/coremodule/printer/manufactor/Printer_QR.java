package com.rocktech.boarddriver.coremodule.printer.manufactor;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.rairmmd.serialport.CommonDataReceiverListener;
import com.rocktech.boarddriver.bean.CommonDataBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.CommonDataObservable;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.DataObserver;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.coremodule.printer.sdk.QrCommand;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.LoggerUtils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android_serialport_api.CommonControl;

public class Printer_QR extends Printer implements CommonDataReceiverListener {

    public static final int OPT_CODE = 999;
    public static final int FYG_CODE = 888;
    private int type = FYG_CODE;
    private CommonControl printer;
    private final static String cmd = "READSTA " + "\r\n";
    private String data2Print;
    private static int time = 3;

    private Context context;
    CommonDataObservable commonDataObservable;
    QrCommand qrCommand;

    public Printer_QR(Context context) {
        this.context = context;
        printer = new CommonControl(ConfigureTools.getPrinterTty(context), Constant.PRINTER_BAUDRATE, 5000, context);
        printer.setCommonDataReceiverListener(this);
        commonDataObservable = new CommonDataObservable();
        qrCommand = new QrCommand();
    }


    @Override
    public void onDataReceiver(byte[] buffer, int size) {
        if (size == 1 && buffer[0] == 9) {
            return;
        }
        String str = null;
        try {
            str = new String(buffer, "gb2312");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.HASPAPER) {
            if (!TextUtils.isEmpty(str)) {
                String[] state = str.split("[ ,]");
                Intent intent = new Intent("android.intent.action.hal.printer.result.haspaper");
                if (state[1].equals("PAPEREND") || state[1].equals("PAPER")) {
                    intent.putExtra("haspaper", true);
                    context.sendBroadcast(intent);
                    commonDataObservable.setData(new CommonDataBean(true));
                } else if (state[1].equals("NOPAPER")) {
                    // 缺纸
                    intent.putExtra("haspaper", false);
                    context.sendBroadcast(intent);
                    commonDataObservable.setData(new CommonDataBean(false));
                } else {
                    commonDataObservable.setData(new CommonDataBean(false));
                }
            }
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.STATUS) {
            if (!TextUtils.isEmpty(str)) {
                LoggerUtils.Log().e("打印结束,发广播给丰巢返回数据 = " + str);
                Intent intent = new Intent("android.intent.action.hal.printer.result.status");
                intent.putExtra("status", true);
                context.sendBroadcast(intent);
                commonDataObservable.setData(new CommonDataBean(true));
            } else {
                commonDataObservable.setData(new CommonDataBean(false));
            }
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.DATA2PRINT) {
            if (!TextUtils.isEmpty(str)) {
                if (qrCommand.QiRui_PrinterState((FileInputStream) printer.getmInputStream(), (FileOutputStream) printer.getmOutputStream()) == 1) {
                    if (qrCommand.getPaperState() == 0) {
                        LoggerUtils.Log().e("打印机无纸，打印失败");
                        commonDataObservable.setData(new CommonDataBean(false));
                        return;
                    }
                    if (qrCommand.IsBusy) {
                        LoggerUtils.Log().e("打印机忙");
                        checkState();
                        if (time == 0) {
                            LoggerUtils.Log().e("超时，打印失败");
                            commonDataObservable.setData(new CommonDataBean(false));
                            return;
                        }
                    } else {
                        LoggerUtils.Log().e("打印机处于空闲状态");
                    }
                }
                LoggerUtils.Log().e("开始打印");
                List<byte[]> data;
                if (type == FYG_CODE) {
                    data = qrCommand.QiRui_ParserFYGboxJsonToQR(data2Print);
                } else {
                    data = qrCommand.QiRui_ParserFCboxJsonToQR(data2Print);
                }
                for (byte[] b : data) {
                    printer.sendData(b);
                }
                LoggerUtils.Log().e("打印结束");
                //再去轮询查打印机的状态 18s
                checkState();
                if (time == 0) {
                    LoggerUtils.Log().e("打印机出现异常，打印失败");
                    commonDataObservable.setData(new CommonDataBean(false));
                    return;
                }
                Constant.PrinterState.HANDLE = Constant.PrinterState.STATUS;
                printer.start();
                try {
                    printer.sendData(cmd.getBytes("gb2312"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Constant.PrinterState.HANDLE == Constant.PrinterState.NEEDMORE) {
            if (!TextUtils.isEmpty(str)) {
                String[] state = str.split("[ ,]");
                if (state[1].equals("PAPEREND") || state[1].equals("NOPAPER")) {
                    Intent intent = new Intent("android.intent.action.hal.printer.result.needmore");
                    intent.putExtra("needmore", true);
                    context.sendBroadcast(intent);
                    commonDataObservable.setData(new CommonDataBean(true));
                } else {
                    Intent intent = new Intent("android.intent.action.hal.printer.result.needmore");
                    intent.putExtra("needmore", false);
                    context.sendBroadcast(intent);
                    commonDataObservable.setData(new CommonDataBean(false));
                }
            }
        }
    }


    private void checkState() {
        while (time > 0) {
            printer.reopenSerialPort();
            LoggerUtils.Log().e("result --> " + qrCommand.QiRui_PrinterState((FileInputStream) printer.getmInputStream(), (FileOutputStream) printer.getmOutputStream()));
            if (qrCommand.QiRui_PrinterState((FileInputStream) printer.getmInputStream(), (FileOutputStream) printer.getmOutputStream()) == 1) {
                if (qrCommand.IsBusy) {
                    LoggerUtils.Log().e("打印机忙,间隔1s去看其状态");
                    try {
                        Thread.sleep(1000);
                        time -= 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else {
                    LoggerUtils.Log().e("打印机从BUSY-->空闲状态");
                    time = 6;
                    break;
                }
            } else {
                try {
                    Thread.sleep(1000);
                    time -= 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
        }
    }


    @Override
    public void hasPaper() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.HASPAPER;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void hasPaperMore() {
        Constant.PrinterState.HANDLE = Constant.PrinterState.NEEDMORE;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void print(String str, int type) {
        this.type = type;
        time = 3;
        Constant.PrinterState.HANDLE = Constant.PrinterState.DATA2PRINT;
        data2Print = str;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
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
    public synchronized boolean hasPaperSyn() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Constant.PrinterState.HANDLE = Constant.PrinterState.HASPAPER;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (Boolean) bean.getO1();
        }
        commonDataObservable.deleteObservers();
        return false;
    }

    @Override
    public synchronized boolean hasPaperMoreSyn() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Constant.PrinterState.HANDLE = Constant.PrinterState.NEEDMORE;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (Boolean) bean.getO1();
        }
        commonDataObservable.deleteObservers();
        return false;
    }

    @Override
    public synchronized boolean printSyn(String str, int type) {
        this.type = type;
        time = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Constant.PrinterState.HANDLE = Constant.PrinterState.DATA2PRINT;
        data2Print = str;
        printer.start();
        try {
            printer.sendData(cmd.getBytes("gb2312"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (Boolean) bean.getO1();
        }
        commonDataObservable.deleteObservers();
        return false;
    }

    @Override
    public synchronized String getVersion() {
        return qrCommand.QiRui_GetVersion((FileInputStream) printer.getmInputStream(), (FileOutputStream) printer.getmOutputStream());
    }

}

package com.rocktech.boarddriver.mainserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.LockerFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_QR;
import com.rocktech.boarddriver.coremodule.scanner.CommScanner;
import com.rocktech.boarddriver.tools.Action;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.Tools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MainServer extends Service {

    private Context mContext;
    private ILocker locker;
    private Printer printer;
    private BlockingQueue<Intent> queue;
    ExecutorService service;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        locker = LockerFactory.getLocker(ConfigureTools.getLockerTypeBean(this).getId(), this);
        queue = new LinkedBlockingQueue<>(100);
        service = Executors.newCachedThreadPool();
        service.execute(new Consumer(queue));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getExtras().getInt("action");
        if (CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(mContext).getId()).configureCheck(mContext, locker, false)) {
            switch (action) {
                case Action.BARCODESCANNER_ONOFF_S:
                    int onOff = intent.getExtras().getInt("onoff");
                    if (1 == onOff) {
                        CommScanner.getInstance(60, mContext);
                    } else {
                        CommScanner.getInstance(60, mContext).closeSerialPort();
                    }
                    break;
                case Action.BARCODESCANNER_SCAN_S:
                    CommScanner.getInstance(60, mContext).startScanner();
                    break;
                case Action.BARCODESCANNER_CANCEL_S:
                    CommScanner.getInstance(60, mContext).cancelScanner();
                    break;
                case Action.PRINTER_ONOFF_S:
                    int i = intent.getExtras().getInt("onoff");
                    if (i == 1) {
                        printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(mContext), mContext, printer);
                    } else {
                        if (printer != null) {
                            printer = null;
                        }
                    }
                    break;
                case Action.PRINTER_SUPPORTSIZE_S:
                    printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(mContext), mContext, printer);
                    printer.paperSize();
                    break;
                case Action.PRINTER_HASPAPER_S:
                    printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(mContext), mContext, printer);
                    printer.hasPaper();
                    break;
                case Action.PRINTER_NEEDMORE_S:
                    printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(mContext), mContext, printer);
                    printer.hasPaperMore();
                    break;
                case Action.PRINTER_PRINT_S:
                    printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(mContext), mContext, printer);
                    if (ConfigureTools.getPrinter(mContext).equals(Constant.PrinterTYPE.SPRT)) {
                        String string = intent.getExtras().getString("pstr");
                        printer.print(string, 0);
                    } else if (ConfigureTools.getPrinter(mContext).equals(Constant.PrinterTYPE.QR)) {
                        String string = intent.getExtras().getString("json");
                        printer.print(string, Printer_QR.OPT_CODE);
                    } else {
                        String string = intent.getExtras().getString("json");
                        printer.print(string, 0);
                    }
                    break;
                default:
                    queue.offer(intent);
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    class Consumer implements Runnable {

        private BlockingQueue<Intent> queue;

        Consumer(BlockingQueue<Intent> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (true) {
                    Intent intent = queue.poll();
                    if (intent != null) {
                        if (CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(mContext).getId()).configureCheck(mContext, locker, true)) {
                            int action = intent.getExtras().getInt("action");
                            dispenseAction(action, intent);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }


    private void dispenseAction(int action, Intent intent) {
        switch (action) {
            case Action.LAMP_MAIN_ONOFF_S:
                int lamp_main_value = intent.getExtras().getInt("onoff");
                if (1 == lamp_main_value) {
                    locker.openZlamp();
                } else if (0 == lamp_main_value) {
                    locker.closeZlamp();
                }
                break;
            case Action.LAMP_ONOFF_S:
                int lamp_value = intent.getExtras().getInt("onoff");
                if (1 == lamp_value) {
                    locker.controlLamp(true);
                } else if (0 == lamp_value) {
                    locker.controlLamp(false);
                }
                Tools.deleteOverdueLog();
                break;
            case Action.IOCONTROLLER_OPEN_S:
                String olockNum = intent.getExtras().getString("boxid");
                locker.openLock(olockNum);
                break;
            case Action.IOCONTROLLER_QUERY_S:
                String qlockNum = intent.getExtras().getString("boxid");
                locker.queryLock(qlockNum);
                break;
            case Action.IOCONTROLLER_BATCH_OPEN_S:
                String[] obatchBoxId = intent.getExtras().getStringArray("batchboxid");
                locker.openLock(obatchBoxId);
                break;
            case Action.IOCONTROLLER_SIMPLEBATCHQUERY_S:
                String[] qbatchBoxId = intent.getExtras().getStringArray("batchboxid");
                locker.queryLock(qbatchBoxId);
                break;
            case Action.IOCONTROLLER_QUERYALL_S:
                String boxCount = intent.getExtras().getString("boxCount");
                locker.queryAll(boxCount);
                break;
            case Action.BOXINFO_QUERY_S:
                locker.readCodes();
                break;
            case Action.APP_REBOOT_S:
                long time = intent.getExtras().getLong("delayMs");
                reboot(time);
                break;
            case Action.QUERY_METER_S:
                locker.queryPower();
                break;
            case Action.IOCONTROLLER_QUERYSINGLECABINET_S:
                String[] cbatchBoxId = intent.getExtras().getStringArray("batchboxid");
                locker.querySingleCabinetLock(cbatchBoxId);
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void reboot(long i) {
        final long delay = i;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(Intent.ACTION_REBOOT);
                intent2.putExtra("nowait", 1);
                intent2.putExtra("interval", 1);
                intent2.putExtra("window", 0);
                mContext.sendBroadcast(intent2);
            }
        }).start();
    }
}

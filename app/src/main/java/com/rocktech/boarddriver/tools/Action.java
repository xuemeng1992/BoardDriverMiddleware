package com.rocktech.boarddriver.tools;

public class Action {
    public static String BARCODESCANNER_ONOFF = "android.intent.action.hal.barcodescanner.onoff";
    public static final int BARCODESCANNER_ONOFF_S = 1;
    public static String BARCODESCANNER_SCAN = "android.intent.action.hal.barcodescanner.scan";
    public static final int BARCODESCANNER_SCAN_S = 2;
    public static String BARCODESCANNER_CANCEL = "android.intent.action.hal.barcodescanner.cancel";
    public static final int BARCODESCANNER_CANCEL_S = 3;

    public static String PRINTER_ONOFF = "android.intent.action.hal.printer.onoff";
    public static final int PRINTER_ONOFF_S = 4;
    public static String PRINTER_HASPAPER = "android.intent.action.hal.printer.haspaper";
    public static final int PRINTER_HASPAPER_S = 5;
    public static String PRINTER_NEEDMORE = "android.intent.action.hal.printer.needmore";
    public static final int PRINTER_NEEDMORE_S = 6;
    public static String PRINTER_PRINT = "android.intent.action.hal.printer.print";
    public static final int PRINTER_PRINT_S = 7;
    public static String PRINTER_SUPPORTSIZE = "android.intent.action.hal.printer.supportsize";
    public static final int PRINTER_SUPPORTSIZE_S = 8;

    public static String IOCONTROLLER_OPEN = "android.intent.action.hal.iocontroller.open";
    public static final int IOCONTROLLER_OPEN_S = 9;
    public static String IOCONTROLLER_QUERY = "android.intent.action.hal.iocontroller.query";
    public static final int IOCONTROLLER_QUERY_S = 10;
    public static String IOCONTROLLER_BATCH_OPEN = "android.intent.action.hal.iocontroller.batchopen";
    public static final int IOCONTROLLER_BATCH_OPEN_S = 11;
    public static String IOCONTROLLER_SIMPLEBATCHQUERY = "android.intent.action.hal.iocontroller.simplebatchquery";
    public static final int IOCONTROLLER_SIMPLEBATCHQUERY_S = 12;
    public static String IOCONTROLLER_QUERYALL = "android.intent.action.hal.iocontroller.queryAll";
    public static final int IOCONTROLLER_QUERYALL_S = 13;


    public static String LAMP_ONOFF = "android.intent.action.hal.lamp.onoff";
    public static final int LAMP_ONOFF_S = 14;
    public static String LAMP_MAIN_ONOFF = "android.intent.action.hal.lamp.main.onoff";
    public static final int LAMP_MAIN_ONOFF_S = 15;

    public static String APP_REBOOT = "android.intent.action.hal.app.reboot";
    public static final int APP_REBOOT_S = 16;

    public static String BOXINFO_WRITE = "android.intent.action.hal.boxinfo.write";
    public static final int BOXINFO_WRITE_S = 17;
    public static String BOXINFO_QUERY = "android.intent.action.hal.boxinfo.query";
    public static final int BOXINFO_QUERY_S = 18;

    public static String CFGTABLE_QUERY = "android.intent.action.hal.cfgtable.query";
    public static final int CFGTABLE_QUERY_S = 19;

    public static String GUARD_ONOFF = "android.intent.action.hal.guard.onoff";
    public static final int GUARD_ONOFF_S = 20;

    public static String SYS_QUERY = "android.intent.action.hal.sys.query";
    public static final int SYS_QUERY_S = 21;

    public static String IOCONTROLLER_QUERYSINGLECABINET = "android.intent.action.hal.iocontroller.querySingleCabinet";
    public static final int IOCONTROLLER_QUERYSINGLECABINET_S = 23;

    //485抄表
    public final static String Query485 = "rocktech.intent.action.meter";
    //查询温度
    public final static String Temp = "rocktech.intent.action.temp";
    //查询当前温度补偿
    public final static String queryTemp = "rocktech.intent.action.queryTemp";
    //查询湿度
    //public final static String ShiDu = "rocktech.intent.action.shidu";
    //查询当前湿度补偿
    public final static String queryShidu = "rocktech.intent.action.queryShidu";
    //查询抄表485设置
    public final static String query485SET = "rocktech.intent.action.query485Setting";
    //查询风扇A设置温度
    public final static String query12SET = "rocktech.intent.action.query12Setting";
    //查询风扇B设置温度
    public final static String query25SET = "rocktech.intent.action.query25Setting";
    //查询加热器设置温度
    public final static String query26SET = "rocktech.intent.action.query26Setting";
    //门磁监测设置
    public final static String queryMCSET = "rocktech.intent.action.queryMCSetting";

    public final static String sendComplete = "rocktech.intent.action.sendComplete";

    public final static String OVER = "rocktech.intent.action.over";

    public final static String OVERTIME = "rocktech.intent.action.overtime";

    public final static String QUERY_METER = "rocktech.intent.action.queryMeter";
    public final static int QUERY_METER_S = 22;

}

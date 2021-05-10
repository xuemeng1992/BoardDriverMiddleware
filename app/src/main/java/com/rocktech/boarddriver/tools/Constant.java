package com.rocktech.boarddriver.tools;

public class Constant {

    public static class LockerTYPE {
        public static final int PROCURATORATE_LOCKER = 1;
        public static final int COURT_LOCKER = 2;
        public static final int COMMON_LOCKER = 3;
        public static final int RAOPING_LOCKER = 4;
        public static final int THREECOLUMNS_LOCKER = 5;
    }

    public static class BoardTYPE {
        public static final int ROCKTECH = 1;
        public static final int HIVEBOX = 2;
        public static final int SUDIYI = 3;
    }

    public static class ShowTemperature {
        public static final int SHOW = 1;
        public static final int HIDE = 2;
    }

    public static class ScannerTYPE {
        public static final String HONEYWELL_USB = "honeywell_Usb";
        public static final String HONEYWELL_SERIES = "honeywell_Series";
        public static final String DEWO = "dewo";
        public static final String FM50 = "fm50";
        public static final String _4102S = "4102s";
        public static final String SPR4308 = "spr4308";
    }

    public static class PrinterTYPE {
        public static final String SPRT = "SPRT";
        public static final String QR = "QR";
        public static final String XBY = "XBY";
        public static final String SHIYIN = "SHIYIN";
    }

    public static class CustomerTYPE {
        public static final String PANJI = "1";
        public static final String GELANDA = "2";
        public static final String QIAOMU = "3";
        public static final String DEFAULT = "-1";
    }

    public static class PrinterState {
        public static int HANDLE = -1;
        public final static int HASPAPER = 1;
        public final static int STATUS = 2;
        public final static int DATA2PRINT = 3;
        public final static int NEEDMORE = 4;
    }

    public static final String DB_NAME = "boarddriver.db";

    public static final String[] m1 = {"请选择", "honeywell N5680SR", "honeywell 3310G", "德沃 XN-201", "新大陆 FM5050-20",
            "擎亚 4102s", "斯普瑞-4108"};
    public static final String[] m2 = {"请选择", "思普瑞特 3寸  SP-EU801SU", "启锐 4寸  QR-588Q ", "新北洋BK-T6112"};

    public static final String[] m3 = {"请选择校验模式", "PARITY_NONE", "PARITY_ODD", "PARITY_EVEN", "PARITY_ALWAYS_ONE",
            "PARITY_ALWAYS_ZERO"};

    public static final String[] m4 = {"请选择波特率", "BAUD_RATE_300", "BAUD_RATE_600", "BAUD_RATE_1200", "BAUD_RATE_2400",
            "BAUD_RATE_4800", "BAUD_RATE_9600", "BAUD_RATE_19200", "BAUD_RATE_38400", "BAUD_RATE_57600",
            "BAUD_RATE_115200"};

    public final static String[] boardList = {"Z01", "A01", "B01", "C01", "D01", "E01", "F01", "G01", "H01", "I01", "J01", "K01", "L01"};
    public final static String[] st_cupboard_placement = {"L08", "L07", "L06", "L05", "L04", "L03", "L02", "L01", "Z00", "R01", "R02", "R03", "R04",
            "R05", "R06", "R07", "R08"};
    public final static String[] st_cupboard_type = {"主柜", "副柜"};
    public final static String[] st_cupboard_court_mainboard_type = {"带读卡器", "不带读卡器"};
    public final static String[] st_cupboard_court_board_type = {"32格口", "16格口"};
    public final static String[] str_cupboard_position = {"室内", "户外"};

    public final static String[] COM_LIST = new String[]{
            "默认串口",
            "/dev/ttymxc0",
            "/dev/ttymxc1", "/dev/ttymxc2", "/dev/ttymxc3",
            "/dev/ttymxc4", "/dev/ttymxc5", "/dev/ttymxc6",
            "/dev/ttyS0",
            "/dev/ttyS1", "/dev/ttyS2", "/dev/ttyS3",
            "/dev/ttyS4", "/dev/ttyS5", "/dev/ttyS6",
            "/dev/ttyUSB0", "/dev/ttyUSB1", "/dev/ttyUSB2",
            "/dev/ttyUSB3", "/dev/ttyUSB4", "/dev/ttyUSB5",
            "/dev/ttyUSB6"
    };

    public static class CheckResultTYPE {
        public static final int OVERTIME = 1;
        public static final int DATA_HEADE_RERROR = 2;
        public static final int RESULT_OK = 3;
        public static final int ENOUGH_LENGTH = 4;
        public static final int NOT_ENOUGH_LENGTH = 5;
        public static final int _485_ENOUGH_LENGTH = 6;
    }

    public static class SPKEY {
        public static final String PRINTER = "printer";
        public static final String PRINTER_COM = "PRINTER_COM";
        public static final String SCANNER = "scanner";
        public static final String SCANNER_COM = "SCANNER_COM";
        public static final String LOCKSTAT = "LOCKSTAT";
        public static final String BUZENABLEONE = "BUZENABLEONE";
        public static final String PARITY = "PARITY";
        public static final String BAUDRATE = "BAUDRATE";
        public static final String BUZZER = "BUZZER";
        public static final String CONNECTBOXSIZE = "CONNECTBOXSIZE";
        public static final String LOCK_COM = "LOCK_COM";
    }


    //锁控版
    public final static int LOCKER_BAUDRATE = 9600;

    public final static int PRINTER_BAUDRATE = 115200;

    public final static int SCANNER_BAUDRATE = 115200;

    public static final int FC_GET_TEMP_FOR_OTHER = 20;
    public static final int FLAG_THREE = 50;
    public static final int FLAG_CHECK_CONNECTED_BOARD = 51;
    public static final int FLAG_GET_CHIP_VERSION = 52;
    public static final int FLAG_UPDATE_BOARD_ADDR = 53;
    public static final int FLAG_WRITE_ASSET_CODE = 54;

    public static final int STATE_OPEN_SINGLE_LOCK = 100;
    public static final int STATE_QUERY_SINGLE_LOCK = 200;
    public static final int STATE_QUERY_SINGLE_CABINET_LOCK = 300;
    public static final int STATE_OPEN_MUTI_LOCK = 101;
    public static final int STATE_QUERY_MUTI_LOCK = 201;
    public static final int STATE_QUERY_ALL_LOCK_FOR_COURT = 202;
    public static final int STATE_QUERY_OPENED_LOCK = 102;
    public static final int STATE_HITESTER_READ_ASSETCODE = 103;
    public static final int STATE_HITESTER_WRITE_ASSETCODE = 104;
    public static final int STATE_DOOR_MAGNET_CHECK = 105;
    public static final int STATE_485_POWER_READ = 106;
    public static final int STATE_GET_CURRENT_TEMP = 107;
    public static final int STATE_GET_CURRENT_HUMIDITY = 108;
    public static final int STATE_QUERY_WDBC = 109;
    public static final int STATE_QUERY_SDBC = 110;
    public static final int STATE_485_SET = 111;
    public static final int STATE_QUERY_TEMP = 112;
    public static final int STATE_QUERY_DOOR_MAGNET = 113;
    public static final int STATE_LOCK_OPEN_TEST = 114;

    public static final int MCCHECK = 990;
    public static final int MCLAMP = 991;
    public static final int SET485 = 992;
    public static final int SETWDBC = 993;
    public static final int SETSDBC = 994;
    public static final int GETCONTROL = 995;
    public static final int DODEBUG = 996;
    public static final int CONTROLMAINDOOR = 997;
    public static final int OPENZLAMP = 998;
    public static final int CLOSEZLAMP = 999;
    public static final int OPENLAMP = 1000;
    public static final int CLOSELAMP = 1001;
    public static final int SETBUZZER = 1002;
    public static final int SETCONTROL = 1003;


}

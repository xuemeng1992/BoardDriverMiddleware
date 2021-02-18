package com.rocktech.boarddriver.tools.opt;

public class OptTable {

    public static final int OPT_CODE_NOP = 0; // 空指令, 无实际含义
    public static final int OPT_CODE_READY = 10; // 准备指令, 必须为一组指令list中的first指令,表示预备打印
    public static final int OPT_CODE_OVER = 11; // 完成指令,  必须为一组指令list中的last指令,表示已完成指令发送,等待打印机打印
    public static final int OPT_CODE_TEXT = 12; // 文字
    public static final int OPT_CODE_LINE = 13; // 线段
    public static final int OPT_CODE_BARCODE  = 14; // 一维条码
    public static final int OPT_CODE_QRCODE = 15; // 二维条码
    public static final int OPT_CODE_BITMAP = 16; // 点阵图像    

    
    public static final int FONT_TYPE_MATRX_16x16 = 1; // 16x16中文点阵 数字英文为8x16
    public static final int FONT_TYPE_MATRX_24x24 = 2; // 24x24中文点阵 数字英文为12x24
    public static final int FONT_TYPE_MATRX_24x48 = 3; // 24x48数字英文点阵
    public static final int FONT_TYPE_MATRX_36x36 = 4; // 36x36中文点阵 数字英文为18x36
    public static final int FONT_TYPE_MATRX_48x48 = 5; // 48x48中文点阵 数字英文为24x48
    public static final int FONT_TYPE_MATRX_72x72 = 6; // 72x72中文点阵 数字英文为36x72
    
    public static final int BARCODE_TYPE_128 = 1; // Code 128 (Auto) 条形码
    
    public static final int QRCODE_LEVEL_L = 1; // 7% 容错率
    public static final int QRCODE_LEVEL_M = 2; // 15% 容错率
    public static final int QRCODE_LEVEL_Q = 3; // 25% 容错率
    public static final int QRCODE_LEVEL_H = 4; // 30% 容错率
}

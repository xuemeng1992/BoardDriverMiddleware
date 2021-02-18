package com.rocktech.boarddriver.coremodule.printer;

public abstract class Printer {
    /**
     * 是否有纸
     */
    public abstract void hasPaper();

    public abstract boolean hasPaperSyn();

    /**
     * 是否纸将近
     */
    public abstract void hasPaperMore();

    public abstract boolean hasPaperMoreSyn();

    /**
     * 打印
     */
    public abstract void print(String str, int type);

    public abstract boolean printSyn(String str, int type);

    /**
     * 纸张尺寸 3寸-0 4寸-1
     */
    public abstract void paperSize();

    public abstract void closeSerialPort();

    public abstract  String getVersion();

}

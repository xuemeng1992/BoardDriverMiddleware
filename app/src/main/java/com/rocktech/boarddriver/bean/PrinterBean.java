package com.rocktech.boarddriver.bean;

public class PrinterBean {


    /**
     * com : /dev/ttymxc4
     * baudrate : 115200
     */

    private String com;
    private int baudrate;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }
}

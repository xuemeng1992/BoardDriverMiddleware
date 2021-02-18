package com.rocktech.boarddriver.bean;

public class CommonDataBean {


    private Object o1;
    private Object o2;
    private Object o3;
    private Object o4;


    public CommonDataBean() {

    }

    public CommonDataBean(Object o1) {
        this.o1 = o1;
    }

    public CommonDataBean(Object o1, Object o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public CommonDataBean(Object o1, Object o2, Object o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    public CommonDataBean(Object o1, Object o2, Object o3, Object o4) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
    }

    public Object getO1() {
        return o1;
    }

    public void setO1(Object o1) {
        this.o1 = o1;
    }

    public Object getO2() {
        return o2;
    }

    public void setO2(Object o2) {
        this.o2 = o2;
    }

    public Object getO3() {
        return o3;
    }

    public void setO3(Object o3) {
        this.o3 = o3;
    }

    public Object getO4() {
        return o4;
    }

    public void setO4(Object o4) {
        this.o4 = o4;
    }
}

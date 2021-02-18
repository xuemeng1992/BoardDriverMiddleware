package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import com.rocktech.boarddriver.bean.CommonDataBean;

import java.util.Observable;

public class CommonDataObservable extends Observable {


    private CommonDataBean data;

    public void setData(CommonDataBean data) {
        this.data = data;
        this.setChanged();//通知，数据已改变
        this.notifyObservers();
    }

    public CommonDataBean getData() {
        return data;
    }
}

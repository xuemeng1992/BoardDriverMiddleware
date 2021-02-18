package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import com.rocktech.boarddriver.bean.AssetCodeBean;

import java.util.ArrayList;
import java.util.Observable;

public class AssetCodeObservable extends Observable {

    private int index = 0;
    private ArrayList<AssetCodeBean> codeBeans = new ArrayList<>();//资产编码

    public void initData() {
        this.index = 0;
        codeBeans.clear();
    }


    public int getIndex() {
        return index;
    }

    public void addCodeBean(AssetCodeBean assetCodeBean) {
        codeBeans.add(assetCodeBean);
    }


    public ArrayList<AssetCodeBean> getCodeBeans() {
        return codeBeans;
    }

    public void setIndex(int index) {
        this.index = index;
        this.setChanged();//通知，数据已改变
        this.notifyObservers();
    }
}

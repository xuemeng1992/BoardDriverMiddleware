package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BoxDataObservable extends Observable {

    private int index = 0;
    private List<String> batchBoxId = new ArrayList<>();
    private List<Boolean> HasOpened = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void initData(String boxid) {
        this.index = 0;
        batchBoxId.clear();
        HasOpened.clear();
        if (!TextUtils.isEmpty(boxid)) {
            addbatchBoxId(boxid);
        }
    }

    public void addbatchBoxId(String boxid) {
        batchBoxId.add(boxid);
    }

    public void addHasOpened(boolean hasopen) {
        HasOpened.add(hasopen);
    }

    public void addOpenedList(List<Boolean> openLists) {
        HasOpened.addAll(openLists);
    }

    public List<String> getBatchBoxIds() {
        return batchBoxId;
    }

    public List<Boolean> getHasOpeneds() {
        return HasOpened;
    }

    public void setIndex(int index) {
        this.index = index;
        this.setChanged();//通知，数据已改变
        this.notifyObservers();
    }
}

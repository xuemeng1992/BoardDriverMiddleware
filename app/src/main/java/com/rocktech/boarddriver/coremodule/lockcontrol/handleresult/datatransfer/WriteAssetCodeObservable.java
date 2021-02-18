package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import java.util.ArrayList;
import java.util.Observable;

public class WriteAssetCodeObservable extends Observable {

    private ArrayList<String> resultList = new ArrayList<>();
    private ArrayList<Boolean> resultList1 = new ArrayList<>();
    private int index = 0;

    public int getIndex() {
        return index;
    }

    public void initData() {
        this.index = 0;
        resultList.clear();
        resultList1.clear();
    }

    public void addResult(String res) {
        resultList.add(res);
    }

    public void addResult1(boolean res) {
        resultList1.add(res);
    }

    public ArrayList<String> getResultList() {
        return resultList;
    }

    public ArrayList<Boolean> getResultList1() {
        return resultList1;
    }

    public void setIndex(int index) {
        this.index = index;
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public String toString() {
        return "WriteAssetCodeObservable{" +
                "resultList=" + resultList.toString() +
                ", resultList1=" + resultList1.toString() +
                ", index=" + index +
                '}';
    }
}

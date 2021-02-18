package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;


import java.util.Observable;

public class LampObservable extends Observable {


    private int index;

    public void initIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
        this.setChanged();
        this.notifyObservers();
    }


}

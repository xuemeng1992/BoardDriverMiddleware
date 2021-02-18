package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import java.util.ArrayList;
import java.util.Observable;

public class QConnBoardObservable extends Observable {
    private int index = 0;
    private ArrayList<String> boards = new ArrayList<>();

    public void initData() {
        this.index = 0;
        boards.clear();
    }


    public int getIndex() {
        return index;
    }

    public void addBoxid(String board) {
        boards.add(board);
    }


    public ArrayList<String> getBoards() {
        return boards;
    }

    @Override
    public String toString() {
        return "QConnBoardObservable{" +
                "index=" + index +
                ", boards=" + boards +
                '}';
    }

    public void setIndex(int index) {
        this.index = index;
        this.setChanged();
        this.notifyObservers();
    }
}

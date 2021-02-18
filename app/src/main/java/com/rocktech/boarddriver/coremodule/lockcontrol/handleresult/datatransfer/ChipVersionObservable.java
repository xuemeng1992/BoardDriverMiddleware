package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import java.util.ArrayList;
import java.util.Observable;

public class ChipVersionObservable extends Observable {

    private int index = 0;
    private ArrayList<String> boardVs = new ArrayList<>();
    private ArrayList<String> courtBoardVs = new ArrayList<>();

    public void initData() {
        this.index = 0;
        boardVs.clear();
        courtBoardVs.clear();
    }

    public int getIndex() {
        return index;
    }

    public void addBoardV(String board) {
        boardVs.add(board);
    }


    public ArrayList<String> getBoardVs() {
        return boardVs;
    }

    public ArrayList<String> getCourtBoardVs() {
        return courtBoardVs;
    }

    public void setCourtBoardV(String courtBoardV) {
        this.courtBoardVs.add(courtBoardV);
    }

    public void setIndex(int index) {
        this.index = index;
        this.setChanged();
        this.notifyObservers();
    }

}

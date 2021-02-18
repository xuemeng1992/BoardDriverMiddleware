package com.rocktech.boarddriver.coremodule.lockcontrol;

import com.rocktech.boarddriver.coremodule.lockcontrol.hivebox.HiveboxHandle;
import com.rocktech.boarddriver.coremodule.lockcontrol.rocktech.RocktechHandle;
import com.rocktech.boarddriver.coremodule.lockcontrol.sudiyi.SudiyiHandle;
import com.rocktech.boarddriver.tools.Constant;

public class HandleFactory {

    private static int currentCMD;
    private static String currentBoxId;
    private static String[] currentBoxList;
    private static String currentAssetCode;

    public static String getCurrentBoxId() {
        return currentBoxId;
    }

    public static void setCurrentBoxId(String currentBoxId) {
        HandleFactory.currentBoxId = currentBoxId;
    }

    public static String[] getCurrentBoxList() {
        return currentBoxList;
    }

    public static void setCurrentBoxList(String[] currentBoxList) {
        HandleFactory.currentBoxList = currentBoxList;
    }

    public static String getCurrentAssetCode() {
        return currentAssetCode;
    }

    public static void setCurrentAssetCode(String currentAssetCode) {
        HandleFactory.currentAssetCode = currentAssetCode;
    }

    public static int getCurrentCMD() {
        return currentCMD;
    }

    public static void setCurrentCMD(int currentCMD) {
        HandleFactory.currentCMD = currentCMD;
    }

    public static IHandle produceHandle(int type) {
        if (type == Constant.BoardTYPE.ROCKTECH) {
            return new RocktechHandle();
        }
        if (type == Constant.BoardTYPE.HIVEBOX) {
            return new HiveboxHandle();
        }
        if (type == Constant.BoardTYPE.SUDIYI) {
            return new SudiyiHandle();
        }
        return null;
    }

}

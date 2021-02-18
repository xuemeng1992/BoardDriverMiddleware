package com.rocktech.boarddriver.coremodule.lockcontrol;

import com.rocktech.boarddriver.coremodule.lockcontrol.hivebox.HiveboxCMD;
import com.rocktech.boarddriver.coremodule.lockcontrol.rocktech.RocktechCMD;
import com.rocktech.boarddriver.coremodule.lockcontrol.sudiyi.SudiyiCMD;
import com.rocktech.boarddriver.tools.Constant;

public class CMDFactory {

    public static ICMD produceCMD(int type) {
        if (type == Constant.BoardTYPE.ROCKTECH) {
            return new RocktechCMD();
        }
        if (type == Constant.BoardTYPE.HIVEBOX) {
            return new HiveboxCMD();
        }
        if (type == Constant.BoardTYPE.SUDIYI) {
            return new SudiyiCMD();
        }
        return null;
    }

}

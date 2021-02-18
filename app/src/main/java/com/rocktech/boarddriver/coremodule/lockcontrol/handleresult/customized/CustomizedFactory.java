package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import com.rocktech.boarddriver.tools.Constant;


public class CustomizedFactory {

    public static ICustomized getCustomized(int type) {
        if (type == Constant.LockerTYPE.PROCURATORATE_LOCKER) {
            return new Procuratorate();
        } else if (type == Constant.LockerTYPE.COURT_LOCKER) {
            return new Court();
        } else if (type == Constant.LockerTYPE.COMMON_LOCKER) {
            return new Common();
        } else if (type == Constant.LockerTYPE.RAOPING_LOCKER) {
            return new Raoping();
        } else if (type == Constant.LockerTYPE.THREECOLUMNS_LOCKER) {
            return new ThreeColumns();
        }
        return null;
    }

}

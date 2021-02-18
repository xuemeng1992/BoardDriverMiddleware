package com.rocktech.boarddriver.coremodule.lockcontrol;

import android.content.Context;

import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker.CourtLocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker.CommonLocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker.ProcuratorateLocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker.RaopingLocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker.ThreeColumnsLocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;

public class LockerFactory {

    public static ILocker getLocker(int type, Context context) {
        if (type == Constant.LockerTYPE.PROCURATORATE_LOCKER) {
            return ProcuratorateLocker.getInstance(context, ConfigureTools.getBoardTypeBean(context).getId());
        } else if (type == Constant.LockerTYPE.COURT_LOCKER) {
            return CourtLocker.getInstance(context, ConfigureTools.getBoardTypeBean(context).getId());
        } else if (type == Constant.LockerTYPE.COMMON_LOCKER) {
            return CommonLocker.getInstance(context, ConfigureTools.getBoardTypeBean(context).getId());
        } else if (type == Constant.LockerTYPE.RAOPING_LOCKER) {
            return RaopingLocker.getInstance(context, ConfigureTools.getBoardTypeBean(context).getId());
        } else if (type == Constant.LockerTYPE.THREECOLUMNS_LOCKER) {
            return ThreeColumnsLocker.getInstance(context, ConfigureTools.getBoardTypeBean(context).getId());
        }
        return null;
    }

}

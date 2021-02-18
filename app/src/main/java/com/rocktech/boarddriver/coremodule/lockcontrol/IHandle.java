package com.rocktech.boarddriver.coremodule.lockcontrol;

import android.content.Context;

import com.rocktech.boarddriver.bean.AssetCodeBean;

import java.util.List;

public interface IHandle {

    Object checkData(byte[] LISHIZHI, byte[] buffer);

    String fcGetTempForOther(byte[] LISHIZHI, Context context);

    String flagGetChipVersion(byte[] LISHIZHI);

    boolean stateQuerySingleLock(byte[] LISHIZHI, String boxid, Context context);

    boolean stateOpenSingleLock(byte[] LISHIZHI, String boxId, Context context);

    List<Boolean> checkAllLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context);

    boolean[] singleCabinetLockState(byte[] LISHIZHI, String[] boxList, String assetCode, Context context);

    AssetCodeBean readAssetCode(byte[] LISHIZHI);

    byte[] state485PowerRead(byte[] LISHIZHI);

    boolean handleDoorMagnet(byte[] LISHIZHI, Context context);

    double stateGetCurrentTemp(byte[] LISHIZHI);

    int stateQueryWdbc(byte[] LISHIZHI);

    int stateQuerySdbc(byte[] LISHIZHI);

    int[] state485Set(byte[] LISHIZHI);

    float[] stateQueryTemp(byte[] LISHIZHI);

    boolean stateQueryDoorMagnet(byte[] LISHIZHI);
}

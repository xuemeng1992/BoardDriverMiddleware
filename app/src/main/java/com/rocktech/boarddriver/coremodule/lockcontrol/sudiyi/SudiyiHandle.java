package com.rocktech.boarddriver.coremodule.lockcontrol.sudiyi;

import android.content.Context;

import com.rairmmd.serialport.CRC16Verify;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.IHandle;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

import java.util.Arrays;
import java.util.List;

public class SudiyiHandle implements IHandle {

    @Override
    public Object checkData(byte[] LISHIZHI, byte[] buffer) {
        if (buffer.length == 1 && buffer[0] == 9) {
            return Constant.CheckResultTYPE.OVERTIME;
        }
        if (LISHIZHI.length > 5) {
            return Constant.CheckResultTYPE.RESULT_OK;
        } else {
            return Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH;
        }

    }

    @Override
    public String fcGetTempForOther(byte[] LISHIZHI, Context context) {
        return null;
    }

    @Override
    public String flagGetChipVersion(byte[] LISHIZHI) {
        return null;
    }

    @Override
    public boolean stateQuerySingleLock(byte[] LISHIZHI, String boxid, Context context) {
        String state = Tools.byteToHexStr(LISHIZHI.length, LISHIZHI).substring(8, 10);
        return "01".equals(state);
    }

    @Override
    public boolean stateOpenSingleLock(byte[] LISHIZHI, String boxId, Context context) {
        String state = Tools.byteToHexStr(LISHIZHI.length, LISHIZHI).substring(8, 10);
        return "00".equals(state);
    }

    @Override
    public boolean[] singleCabinetLockState(byte[] LISHIZHI, String[] boxList, String assetCode, Context context) {
        return null;
    }

    @Override
    public List<Boolean> checkAllLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        return null;
    }

    @Override
    public AssetCodeBean readAssetCode(byte[] LISHIZHI) {
        return null;
    }

    @Override
    public byte[] state485PowerRead(byte[] LISHIZHI) {
        return new byte[0];
    }

    @Override
    public boolean handleDoorMagnet(byte[] LISHIZHI, Context context) {
        return false;
    }

    @Override
    public double stateGetCurrentTemp(byte[] LISHIZHI) {
        return 0;
    }

    @Override
    public int stateQueryWdbc(byte[] LISHIZHI) {
        return 0;
    }

    @Override
    public int stateQuerySdbc(byte[] LISHIZHI) {
        return 0;
    }

    @Override
    public int[] state485Set(byte[] LISHIZHI) {
        return new int[0];
    }

    @Override
    public float[] stateQueryTemp(byte[] LISHIZHI) {
        return new float[0];
    }

    @Override
    public boolean stateQueryDoorMagnet(byte[] LISHIZHI) {
        return false;
    }
}

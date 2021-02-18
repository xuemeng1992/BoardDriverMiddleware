package com.rocktech.boarddriver.coremodule.lockcontrol.hivebox;

import android.content.Context;

import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.IHandle;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.SerialPortTools;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class HiveboxHandle implements IHandle {

    @Override
    public Object checkData(byte[] LISHIZHI, byte[] buffer) {
        if (LISHIZHI.length == 1 && buffer[0] == 9) {
            return Constant.CheckResultTYPE.OVERTIME;
        }
        if (LISHIZHI.length > 4) {
            if (LISHIZHI[3] == LISHIZHI.length - 5) {
                byte xorResult = SerialPortTools.xorCalc(LISHIZHI, 0, LISHIZHI.length - 1);
                if (LISHIZHI[LISHIZHI.length - 1] == xorResult) {
                    return Constant.CheckResultTYPE.RESULT_OK;
                }
                return Constant.CheckResultTYPE.ENOUGH_LENGTH;
            }
        } else {
            return Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH;
        }
        return -1;
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
        if (LISHIZHI.length <= 1) {
            return false;
        }
        return LISHIZHI[LISHIZHI.length - 2] == 1 ? true : false;
    }

    @Override
    public boolean stateOpenSingleLock(byte[] LISHIZHI, String boxId, Context context) {
        if (LISHIZHI.length <= 1) {
            return false;
        }
        return LISHIZHI[LISHIZHI.length - 2] == 0 ? true : false;
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
    public byte[] state485PowerRead(byte[] LISHIZHI) {
        return new byte[0];
    }


    @Override
    public AssetCodeBean readAssetCode(byte[] LISHIZHI) {
        String code1;//资产编码
        String code2;//板编号
        if (LISHIZHI.length == 27) {
            try {
                String ss = new String(SerialPortTools.subBytes(LISHIZHI, 5, 21), "UTF-8");
                if (ss.endsWith("N")) { // "*** * ** * ** 00 ******* NNN"
                    code1 = ss.substring(0, 18);
                } else {
                    code1 = ss;
                }
                code2 = new String(SerialPortTools.subBytes(LISHIZHI, 5, 3), "UTF-8");
                return new AssetCodeBean(code1, code2, ss);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
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
    public float[] stateQueryTemp(byte[] LISHIZHI) {
        return new float[0];
    }

    @Override
    public boolean stateQueryDoorMagnet(byte[] LISHIZHI) {
        return false;
    }

    @Override
    public int[] state485Set(byte[] LISHIZHI) {
        return new int[0];
    }
}

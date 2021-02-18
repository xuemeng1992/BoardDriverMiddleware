package com.rocktech.boarddriver.coremodule.lockcontrol.rocktech;

import android.content.Context;
import android.util.Log;

import com.rairmmd.serialport.LibTools;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.IHandle;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RocktechHandle implements IHandle {

    @Override
    public Object checkData(byte[] LISHIZHI, byte[] buffer) {
        if (buffer.length == 1 && buffer[0] == 9) {
            return Constant.CheckResultTYPE.OVERTIME;
        }
        if ((LISHIZHI.length > 2) && ((LISHIZHI[0] != -86) || (LISHIZHI[1] != 0x55))) {
            return Constant.CheckResultTYPE.DATA_HEADE_RERROR;
        }
        if (LISHIZHI.length > 3) {
            if (LISHIZHI[2] == (LISHIZHI.length - 4)) {
                byte crc8 = SerialPortTools.calcCrc8(LISHIZHI, 0, LISHIZHI.length - 1);
                if (LISHIZHI[LISHIZHI.length - 1] == crc8) {
                    return Constant.CheckResultTYPE.RESULT_OK;
                }
            } else {
                if (LISHIZHI.length >= 154) {
                    return Constant.CheckResultTYPE._485_ENOUGH_LENGTH;
                }
            }
        } else {
            return Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH;
        }
        return -1;
    }


    @Override
    public String fcGetTempForOther(byte[] LISHIZHI, Context context) {
        if (LISHIZHI.length == 8) {
            byte t1 = LISHIZHI[5];
            byte t2 = LISHIZHI[6];
            int i1, i2;
            if (t1 < 0) {
                i1 = (256 + t1) << 8;
            } else {
                i1 = t1 << 8;
            }
            if (t2 < 0) {
                i2 = 256 + t2;
            } else {
                i2 = t2;
            }
            double f = (i1 + i2 - 500) / 10.0;
            BigDecimal b = new BigDecimal(f);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            String mock = "[\n" +
                    "{\n" +
                    "\"key\": \"temp.mainboard\",\n" +
                    "\"value\":" + f1 + "\n" +
                    "},\n" +
                    "{\n" +
                    "\"key\": \"anotherKeyName\",\n" +
                    "\"value\":\"anotherValue\"\n" +
                    "}\n" +
                    "]\n";
            return mock;
        }
        return "";
    }

    @Override
    public String flagGetChipVersion(byte[] LISHIZHI) {
        if (LISHIZHI.length == 10) {
            char c = (char) (LISHIZHI[5]);
            String ver = c + "" + LISHIZHI[6] + "." + LISHIZHI[8];
            return ver;
        } else {
            return "旧固件";
        }
    }

    @Override
    public boolean stateQuerySingleLock(byte[] bt, String boxId, Context context) {
        if (bt.length == 1) {
            return false;
        }
        int num = Tools.calculateLock(boxId) - 1;
        boolean b = ConfigureTools.getLockStat(context);

        if ("Z00".equals(boxId)) {
            return false;
        }

        switch (num / 8) {
            case 0:
                return (((bt[5] >> (num % 8)) & 1) == 0) ? (!b) : b;
            case 1:
                return (((bt[6] >> (num % 8)) & 1) == 0) ? (!b) : b;
            case 2:
                return (((bt[7] >> (num % 8)) & 1) == 0) ? (!b) : b;
            case 3:
                return (((bt[8] >> (num % 8)) & 1) == 0) ? (!b) : b;
            case 4:
                return (((bt[9] >> (num % 8)) & 1) == 0) ? (!b) : b;
            case 5:
                return (((bt[10] >> (num % 8)) & 1) == 0) ? (!b) : b;
            default:
                return false;
        }
    }

    @Override
    public boolean stateOpenSingleLock(byte[] LISHIZHI, String boxId, Context context) {
        return stateQuerySingleLock(LISHIZHI, boxId, context);
    }

    @Override
    public boolean[] singleCabinetLockState(byte[] LISHIZHI, String[] boxList, String assetCode, Context context) {
        int lockerType = ConfigureTools.getLockerTypeBean(context).getId();
        boolean[] lockStates = new boolean[boxList.length];
        for (int i = 0; i < boxList.length; i++) {
            lockStates[i] = CustomizedFactory.getCustomized(lockerType).checkLockState(LISHIZHI, boxList[i], assetCode, context);
        }
        return lockStates;
    }

    @Override
    public List<Boolean> checkAllLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        Log.e("checkAllLockState:boxId", boxId);
        int lockerType = ConfigureTools.getLockerTypeBean(context).getId();
        String[] allBox = CustomizedFactory.getCustomized(lockerType).getAllCheckStateLock(assetCode, boxId);
        Log.e("checkAllLockState", Arrays.asList(allBox).toString());
        List<Boolean> hasOpenedList = new ArrayList<>();
        for (int i = 0; i < allBox.length; i++) {
            hasOpenedList.add(CustomizedFactory.getCustomized(lockerType).checkLockState(LISHIZHI, allBox[i], assetCode, context));
        }
        return hasOpenedList;
    }

    @Override
    public byte[] state485PowerRead(byte[] LISHIZHI) {
        byte[] lishizhi = LISHIZHI;
        byte[] LINSHI485 = null;
        while (lishizhi != null && (lishizhi.length >= 7) && (lishizhi.length >= (lishizhi[2] + 4))) {
            byte[] linshi = SerialPortTools.subBytes(lishizhi, 5, lishizhi[2] - 2);
            lishizhi = SerialPortTools.subBytes(lishizhi, (lishizhi[2] + 4), lishizhi.length - (lishizhi[2] + 4));
            if (LINSHI485 == null) {
                LINSHI485 = new byte[linshi.length];
                for (int i = 0; i < linshi.length; i++) {
                    LINSHI485[i] = linshi[i];
                }
            } else {
                LINSHI485 = SerialPortTools.byteMerger(LINSHI485, linshi);
            }
            if (LINSHI485.length == 22) {
                return LINSHI485;
            }
        }
        return LINSHI485;
    }

    @Override
    public AssetCodeBean readAssetCode(byte[] LISHIZHI) {
        String assetCode;//资产编码
        String boxId;//板编号
        String allCode;//锁控板里面全部数据
        String productModel = null;
        try {
            allCode = new String(SerialPortTools.subBytes(LISHIZHI, 8, LISHIZHI.length - 8), "UTF-8");
            String originalAssetCode = new String(SerialPortTools.subBytes(LISHIZHI, 8, 21), "UTF-8");
            if (originalAssetCode.endsWith("N")) { // "*** * ** * ** 00 ******* NNN"
                assetCode = originalAssetCode.substring(0, 18);
            } else {
                assetCode = originalAssetCode;
            }
            boxId = new String(SerialPortTools.subBytes(LISHIZHI, 5, 3), "UTF-8");
            if (allCode.contains("&")) {
                String[] codesSP = allCode.split("&");
                if (codesSP.length > 1) {
                    productModel = codesSP[1];
                }
            }
            LoggerUtils.Log().e("readAssetCode: allCode == " + allCode);
            AssetCodeBean assetCodeBean = new AssetCodeBean(assetCode, boxId, originalAssetCode, productModel, boxId);
            LibTools.writeBehaviorLog("读取到的资产编码：" + assetCodeBean.toString());
            return assetCodeBean;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handleDoorMagnet(byte[] LISHIZHI, Context context) {
        if (LISHIZHI.length == 1) {
            return false;
        }
        if (LISHIZHI.length == 9) {//18路
            if (!ConfigureTools.getBuzEnableOne(context)) {
                if (((LISHIZHI[6] >> 7) & 1) == 1) {
                    return true;
                }
            } else {
                if (((LISHIZHI[6] >> 7) & 1) == 0) {
                    return true;
                }
            }
            return false;
        } else {//12路
            if ((LISHIZHI[6] == 0x0f) || (LISHIZHI[6] == 0x0b)) {
                if (!ConfigureTools.getBuzEnableOne(context)) {
                    if ((LISHIZHI[6] & 4) == 4) {
                        return true;
                    }
                } else {
                    if ((LISHIZHI[6] & 4) == 0) {
                        return true;
                    }
                }
            } else {
                if (!ConfigureTools.getBuzEnableOne(context)) {
                    if ((LISHIZHI[6] & 2) == 2) {
                        return true;
                    }
                } else {
                    if ((LISHIZHI[6] & 2) == 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public double stateGetCurrentTemp(byte[] LISHIZHI) {
        if (LISHIZHI.length == 8) {
            byte t1 = LISHIZHI[5];
            byte t2 = LISHIZHI[6];
            int i1, i2;
            if (t1 < 0) {
                i1 = (256 + t1) << 8;//乘以256
            } else {
                i1 = t1 << 8;
            }
            if (t2 < 0) {
                i2 = 256 + t2;
            } else {
                i2 = t2;
            }
            double f = (i1 + i2 - 500) / 10.0;
            BigDecimal b = new BigDecimal(f);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            /**
             *检查下温度是否正常
             */
            if (f1 > 100 || f1 < 0) {
                return -1;
            }
            return f1;
        }
        return -1;
    }

    @Override
    public int stateQueryWdbc(byte[] LISHIZHI) {
        if (LISHIZHI.length == 7) {
            byte b = LISHIZHI[5];
            if ((b & 0x80) == 0x80) {
                return -((b & 0x7F) / 10);
            } else {
                return (b / 10);
            }
        }
        return 0;
    }

    @Override
    public int stateQuerySdbc(byte[] LISHIZHI) {
        if (LISHIZHI.length == 7) {
            byte b = LISHIZHI[5];
            return b;
        }
        return 0;
    }

    @Override
    public float[] stateQueryTemp(byte[] LISHIZHI) {
        float[] temps = new float[2];
        if (LISHIZHI.length == 10) {
            DecimalFormat df = new DecimalFormat("###.0");
            String temOpen = df
                    .format((Integer.parseInt(Tools.byteToHexStr(2, SerialPortTools.subBytes(LISHIZHI, 5, 2)), 16) - 500) / 10.0);
            String temClose = df
                    .format((Integer.parseInt(Tools.byteToHexStr(2, SerialPortTools.subBytes(LISHIZHI, 7, 2)), 16) - 500) / 10.0);
            /**
             * 校验温度的有效范围，如果不正常，则重新读取
             */
            float openTemp = Float.parseFloat(temOpen);
            float closeTemp = Float.parseFloat(temClose);
            if (openTemp > 100 || openTemp < 0) {
                openTemp = -1;
            }
            if (closeTemp > 100 || closeTemp < 0) {
                closeTemp = -1;
            }
            temps[0] = openTemp;
            temps[1] = closeTemp;
        }
        return temps;
    }

    @Override
    public boolean stateQueryDoorMagnet(byte[] LISHIZHI) {
        if (LISHIZHI.length == 7) {
            if (LISHIZHI[5] == 0x00) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int[] state485Set(byte[] LISHIZHI) {
        int[] _485set = new int[2];
        if (LISHIZHI.length == 9) {
            _485set[0] = LISHIZHI[5] + 1;
            _485set[1] = LISHIZHI[6] + 1;
        }
        return _485set;
    }
}

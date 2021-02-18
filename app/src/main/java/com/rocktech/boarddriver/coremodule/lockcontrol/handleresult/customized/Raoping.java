package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;
import android.util.Log;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.AssetCodeDao;
import com.rocktech.boarddriver.bean.DoubleOpenConfig;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.DaoTools;
import com.rocktech.boarddriver.tools.Tools;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;
import com.rocktech.boarddriver.ui.adapter.CommonAssetCodeAdapter;

import java.util.ArrayList;
import java.util.List;


public class Raoping implements ICustomized {

    @Override
    public boolean checkLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        return getRaopingOpenRes(ConfigureTools.getBoardTypeBean(context).getId(), boxId, LISHIZHI, context);
    }

    public static boolean getRaopingOpenRes(int type, String boxId, byte[] data, Context context) {
        Log.e("getRaopingOpenRes:", boxId);
        String BName = boxId.substring(0, 1);
        if (BName.equals("A")) {
            return getDoubleOpenRes(type, boxId, data, context);
        } else {
            return HandleFactory.produceHandle(ConfigureTools.getBoardTypeBean(context).getId()).stateOpenSingleLock(data, boxId, context);
        }
    }

    @Override
    public String[] getAllCheckStateLock(String assetCode, String boxIndex) {
        int length;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 13;
        } else if (boxIndex.substring(0, 1).equals("A")) {
            length = 4;
        } else {
            length = 8;
        }
        String[] batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    @Override
    public String[] getAllOpenLock(String assetCode, String boxIndex) {
        int length;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 13;
        } else if (boxIndex.substring(0, 1).equals("A")) {
            length = 4;
        } else if (boxIndex.substring(0, 1).equals("B")) {
            length = 8;
        } else {
            length = 16;
        }
        String[] batchBoxs = new String[length];
        if (!"C".equals(boxIndex.substring(0, 1)) && !"D".equals(boxIndex.substring(0, 1))) {
            for (int i = 0; i < batchBoxs.length; i++) {
                if (boxIndex.substring(0, 1).equals("Z") && (i + 1) == 8) {
                    batchBoxs[i] = "Z99";
                } else if (boxIndex.substring(0, 1).equals("Z") && (i + 1) == 13) {
                    batchBoxs[i] = "Z00";
                } else {
                    batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
                }
            }
        } else {
            if ("C".equals(boxIndex.substring(0, 1))) {
                for (int i = 1; i <= batchBoxs.length; i++) {
                    if (i < 9) {
                        batchBoxs[i - 1] = "C" + String.format("%02d", i);
                    } else {
                        batchBoxs[i - 1] = "D" + String.format("%02d", (i - 8));
                    }
                }
            }
            if ("D".equals(boxIndex.substring(0, 1))) {
                for (int i = 1; i <= batchBoxs.length; i++) {
                    if (i < 9) {
                        batchBoxs[i - 1] = "E" + String.format("%02d", i);
                    } else {
                        batchBoxs[i - 1] = "F" + String.format("%02d", (i - 8));
                    }
                }
            }
        }
        return batchBoxs;
    }

    @Override
    public List<AssetCodeBean> assetCodeSort(List<AssetCodeBean> assetCodeArrays) {
        return assetCodeArrays;
    }

    @Override
    public BaseAdapter getAssetCodeAdapter() {
        return new CommonAssetCodeAdapter(R.layout.item_assetcode, new ArrayList<>());
    }

    @Override
    public int getAssetCodeLength() {
        return 18;
    }

    public static boolean getDoubleOpenRes(int type, String boxId, byte[] data, Context context) {
        DoubleOpenConfig config = ConfigureTools.getDoubleOpenConfig(context, "Rdoubleopen.config");
        boolean isFind = false;
        DoubleOpenConfig.DoubleconfigBean doubleconfigBean = null;
        int bId = Integer.parseInt(boxId.substring(1, 3));
        for (int i = 0; i < config.getDoubleconfig().size(); i++) {
            if (bId == config.getDoubleconfig().get(i).getBoxid()) {
                isFind = true;
                doubleconfigBean = config.getDoubleconfig().get(i);
                break;
            }
        }
        if (!isFind) {
            return false;
        }
        String[] boxs = doubleconfigBean.getMappingid().split(",");
        String oneBoxId = boxId.substring(0, 1) + String.format("%02d", Integer.parseInt(boxs[0]));
        String twoBoxId = boxId.substring(0, 1) + String.format("%02d", Integer.parseInt(boxs[1]));
        boolean oneState = HandleFactory.produceHandle(type).stateOpenSingleLock(data, oneBoxId, context);
        boolean twoState = HandleFactory.produceHandle(type).stateOpenSingleLock(data, twoBoxId, context);
        return (oneState && twoState);
    }

    //Z**,A**,B**,C**,C**,D**,D**
    public static String calculateBoardEx(String s, int cellIndex) {
        List<AssetCodeDao> dList = DaoTools.getAssetCodes();
        for (int i = 0; i < dList.size(); i++) {
            if (dList.get(i).getAssetCode().contains(s)) {
                if (i == 0) {
                    return "Z";
                } else {
                    if (cellIndex > 8) {
                        return Tools.numberToLetter(i + 1);
                    } else {
                        return Tools.numberToLetter(i);
                    }
                }
            }
        }
        return "Z";
    }

    public static int getNewCellIndex(String s, int cellIndex) {
        if (!s.equals("C") && !s.equals("D")) {
            if (s.equals("Z")) {
                if (cellIndex == 8) {
                    return 99;
                } else if (cellIndex == 13 || cellIndex == 14) {
                    return 0x0d;
                } else {
                    return cellIndex;
                }
            } else {
                return cellIndex;
            }
        } else {
            if (cellIndex > 8) {
                return (cellIndex - 8);
            }
        }
        return cellIndex;
    }

    @Override
    public List<AssetCodeBean> handleAssetCode(List<AssetCodeBean> assetCodeArrays) {
        return rmRepeatAssetCode(assetCodeArrays);
    }

    private List<AssetCodeBean> rmRepeatAssetCode(List<AssetCodeBean> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getBoxId().equals(list.get(i).getBoxId())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    @Override
    public int getAssetCodeDaoIndex(int index) {
        if (index == 5) {
            return 6;
        }
        return index;
    }

    @Override
    public boolean configureCheck(Context context, ILocker locker, boolean isNeedLoadAssetCode) {
        return ConfigureTools.dataCheck(context);
    }

}

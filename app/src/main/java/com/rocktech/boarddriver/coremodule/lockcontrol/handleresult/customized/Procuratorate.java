package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.DoubleOpenConfig;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;
import com.rocktech.boarddriver.ui.adapter.CommonAssetCodeAdapter;

import java.util.ArrayList;
import java.util.List;


public class Procuratorate implements ICustomized {

    @Override
    public boolean checkLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        return getDoubleOpenRes(ConfigureTools.getBoardTypeBean(context).getId(), boxId, LISHIZHI, context);
    }

    @Override
    public String[] getAllCheckStateLock(String assetCode, String boxIndex) {
        String[] batchBoxs = new String[3];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    @Override
    public String[] getAllOpenLock(String assetCode, String boxIndex) {
        String[] batchBoxs = new String[3];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    public static boolean getDoubleOpenRes(int type, String boxId, byte[] data, Context context) {
        DoubleOpenConfig config = ConfigureTools.getDoubleOpenConfig(context, "Pdoubleopen.config");
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
        if (isFind) {
            String[] boxs = doubleconfigBean.getMappingid().split(",");
            String oneBoxId = boxId.substring(0, 1) + String.format("%02d", Integer.parseInt(boxs[0]));
            String twoBoxId = boxId.substring(0, 1) + String.format("%02d", Integer.parseInt(boxs[1]));
            boolean oneState = HandleFactory.produceHandle(type).stateOpenSingleLock(data, oneBoxId, context);
            boolean twoState = HandleFactory.produceHandle(type).stateOpenSingleLock(data, twoBoxId, context);
            return (oneState && twoState);
        } else {
            doubleconfigBean = config.getDoubleconfig().get(config.getDoubleconfig().size() - 1);
            String[] boxs = doubleconfigBean.getMappingid().split(",");
            int endId = Integer.parseInt(boxs[1]);
            int currentId = endId + (bId - config.getDoubleconfig().size());
            String currentBoxId = boxId.substring(0, 1) + String.format("%02d", currentId);
            return HandleFactory.produceHandle(type).stateOpenSingleLock(data, currentBoxId, context);
        }
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

    @Override
    public List<AssetCodeBean> handleAssetCode(List<AssetCodeBean> assetCodeArrays) {
        return assetCodeArrays;
    }

    @Override
    public int getAssetCodeDaoIndex(int index) {
        return index;
    }

    @Override
    public boolean configureCheck(Context context, ILocker locker, boolean isNeedLoadAssetCode) {
        return ConfigureTools.dataCheck(context);
    }

}

package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;
import com.rocktech.boarddriver.ui.adapter.CommonAssetCodeAdapter;

import java.util.ArrayList;
import java.util.List;


public class Common implements ICustomized {

    @Override
    public boolean checkLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        return HandleFactory.produceHandle(ConfigureTools.getBoardTypeBean(context).getId()).stateOpenSingleLock(LISHIZHI, boxId, context);
    }

    @Override
    public String[] getAllCheckStateLock(String assetCode, String boxIndex) {
        String[] batchBoxs;
        int length;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 8;
        } else {
            if (assetCode.substring(9, 11).equals("03") || assetCode.substring(9, 11).equals("04")) {
                length = 45;
            } else {
                length = 22;
            }
        }
        batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    @Override
    public String[] getAllOpenLock(String assetCode, String boxIndex) {
        String[] batchBoxs;
        int length;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 8;
        } else {
            if (assetCode.substring(9, 11).equals("03") || assetCode.substring(9, 11).equals("04")) {
                length = 45;
            } else {
                length = 22;
            }
        }
        batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    public static String[] getCommQueryLock(String boxIndex) {
        String[] batchBoxs;
        int length;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 12;
        } else {
            length = 22;
        }
        batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
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
        return true;
    }
}

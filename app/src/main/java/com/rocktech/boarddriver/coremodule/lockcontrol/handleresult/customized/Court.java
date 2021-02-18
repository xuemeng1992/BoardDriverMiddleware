package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;
import com.rocktech.boarddriver.ui.adapter.CourtAssetCodeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Court implements ICustomized {

    @Override
    public boolean checkLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context) {
        return HandleFactory.produceHandle(ConfigureTools.getBoardTypeBean(context).getId()).stateOpenSingleLock(LISHIZHI, boxId, context);
    }

    @Override
    public String[] getAllCheckStateLock(String assetCode, String boxIndex) {
        int length = 22;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 14;
        } else {
            if (assetCode.substring(3, 5).equals("20")) {
                length = 32;
            } else if (assetCode.substring(3, 5).equals("28")) {
                length = 16;
            }
        }
        String[] batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    @Override
    public String[] getAllOpenLock(String assetCode, String boxIndex) {
        int length = 22;
        if (boxIndex.substring(0, 1).equals("Z")) {
            length = 14;
        } else {
            if (assetCode.substring(3, 5).equals("20")) {
                length = 32;
            } else if (assetCode.substring(3, 5).equals("28")) {
                length = 16;
            }
        }
        String[] batchBoxs = new String[length];
        for (int i = 0; i < batchBoxs.length; i++) {
            batchBoxs[i] = boxIndex.substring(0, 1) + String.format("%02d", i + 1);
        }
        return batchBoxs;
    }

    @Override
    public List<AssetCodeBean> assetCodeSort(List<AssetCodeBean> assetCodeArrays) {
        ArrayList<AssetCodeBean> list = new ArrayList<>();
        AssetCodeBean s1 = null;
        for (AssetCodeBean s : assetCodeArrays) {
            if (s.getAssetCode().substring(0, 1).equals("Z")) {
                if (assetCodeArrays.size() == 1) {
                    list.add(s);
                } else {
                    s1 = s;
                }
            } else {
                if (s.getAssetCode().substring(0, 1).equals("R")) {
                    if (s1 != null) {
                        list.add(s1);
                        s1 = null;
                    }
                    list.add(s);
                } else {
                    list.add(s);
                }
            }
        }
        if (s1 != null) {
            list.add(s1);
            s1 = null;
        }
        Collections.sort(list, comparator);
        return list;
    }


    private static Comparator<AssetCodeBean> comparator = new Comparator<AssetCodeBean>() {
        @Override
        public int compare(AssetCodeBean s1, AssetCodeBean s2) {
            if (s1.getAssetCode().contains("Z") && s2.getAssetCode().contains("R")) {
                return -(s1.getAssetCode().compareTo(s2.getAssetCode()));
            } else if (s1.getAssetCode().contains("R") && s2.getAssetCode().contains("Z")) {
                return -(s1.getAssetCode().compareTo(s2.getAssetCode()));
            } else {
                if (s1.getAssetCode().contains("L") && s2.getAssetCode().contains("L")) {
                    return -(s1.getAssetCode().compareTo(s2.getAssetCode()));
                } else {
                    return s1.getAssetCode().compareTo(s2.getAssetCode());
                }
            }
        }
    };

    @Override
    public BaseAdapter getAssetCodeAdapter() {
        return new CourtAssetCodeAdapter(R.layout.item_court_assetcode, new ArrayList<>());
    }

    @Override
    public int getAssetCodeLength() {
        return 15;
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

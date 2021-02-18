package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;

import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;

import java.util.List;


public interface ICustomized {

    boolean checkLockState(byte[] LISHIZHI, String boxId, String assetCode, Context context);

    //根据传入的板id获取需要检测状态的锁id
    String[] getAllCheckStateLock(String assetCode, String boxIndex);

    //根据出入的逻辑板id获取所有物理上要打开的锁id
    String[] getAllOpenLock(String assetCode, String boxIndex);

    List<AssetCodeBean> assetCodeSort(List<AssetCodeBean> assetCodeArrays);

    BaseAdapter getAssetCodeAdapter();

    int getAssetCodeLength();

    int getAssetCodeDaoIndex(int index);

    List<AssetCodeBean> handleAssetCode(List<AssetCodeBean> assetCodeArrays);

    boolean configureCheck(Context context, ILocker locker, boolean isNeedLoadAssetCode);

}

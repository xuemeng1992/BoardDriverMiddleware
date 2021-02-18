package com.rocktech.boarddriver.tools;

import com.rocktech.boarddriver.BoardDriverApp;
import com.rocktech.boarddriver.bean.AssetCodeDao;

import java.util.List;

public class DaoTools {

    public static List<AssetCodeDao> getAssetCodes() {
        return BoardDriverApp.getDaoSession().getAssetCodeDaoDao().loadAll();
    }

    public static void addAssetCode(AssetCodeDao assetCodeDao) {
        BoardDriverApp.getDaoSession().getAssetCodeDaoDao().insert(assetCodeDao);
    }

    public static void clearAssetCodeDao() {
        BoardDriverApp.getDaoSession().getAssetCodeDaoDao().deleteAll();
    }

    public static AssetCodeDao getAssetCodeDao(String colId) {
        List<AssetCodeDao> dList = getAssetCodes();
        for (int i = 0; i < dList.size(); i++) {
            if (dList.get(i).getAssetCode().contains(colId)) {
                return dList.get(i);
            }
        }
        return new AssetCodeDao();
    }

    public static AssetCodeDao getAssetCodeDao(int index) {
        List<AssetCodeDao> dList = getAssetCodes();
        return dList.get(index - 1);
    }
}

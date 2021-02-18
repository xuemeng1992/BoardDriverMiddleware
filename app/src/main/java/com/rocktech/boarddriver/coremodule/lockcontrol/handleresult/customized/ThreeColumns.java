package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.AssetCodeDao;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.DaoTools;
import com.rocktech.boarddriver.tools.Tools;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;
import com.rocktech.boarddriver.ui.adapter.CommonAssetCodeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ThreeColumns implements ICustomized {

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
    public int getAssetCodeDaoIndex(int index) {
        return index;
    }

    @Override
    public List<AssetCodeBean> handleAssetCode(List<AssetCodeBean> assetCodeArrays) {
        return assetCodeArrays;
    }

    public static String getRealBoxName(String boxName) {
        return getLockId(boxName);
    }

    //add by xuemeng
    private static String getLockId(String boxName) {
        //板id
        String BId = boxName.substring(0, 1);
        //锁id
        String LId = boxName.substring(1, 3);
        //Z主柜
        if (BId.equals("Z")) {
            return boxName;
        }
        //副柜
        List<AssetCodeDao> boxIdArrays = DaoTools.getAssetCodes();
        if (boxIdArrays.size() == 0) {
            return boxName;
        }
        int BIndex = getIndex(BId, boxIdArrays);
        if (BIndex == -1) {
            return boxName;
        }
        String newBid;
        if (Integer.parseInt(LId) <= 22) {
            newBid = Tools.numberToLetter(BIndex) + LId;
        } else {
            if (getBoxCount(BId, boxIdArrays) == 2) {
                int newLid = Integer.parseInt(LId) - 22;
                newBid = Tools.numberToLetter(BIndex + 1) + String.format("%02d", newLid);
            } else {
                newBid = Tools.numberToLetter(BIndex) + String.format("%02d", Integer.parseInt(LId));
            }
        }
        Log.e("getRealBoxName", newBid);
        return newBid;
    }

    //add by xuemeng
    private static int getIndex(String BId, List<AssetCodeDao> boxIdArrays) {
        for (int i = 0; i < boxIdArrays.size(); i++) {
            if (boxIdArrays.get(i).getBoxId().contains(BId)) {
                return i;
            }
        }
        return -1;
    }

    private static int getBoxCount(String BId, List<AssetCodeDao> boxIdArrays) {
        int count = 0;
        for (int i = 0; i < boxIdArrays.size(); i++) {
            if (boxIdArrays.get(i).getBoxId().contains(BId)) {
                count++;
            }
        }
        return count;
    }

    public static List<AssetCodeBean> rmRepeatAssetCode(List<AssetCodeBean> list) {
        List<AssetCodeBean> newlist = new ArrayList<>();
        newlist.addAll(list);
        for (int i = 0; i < newlist.size() - 1; i++) {
            for (int j = newlist.size() - 1; j > i; j--) {
                if (newlist.get(j).getBoxId().equals(newlist.get(i).getBoxId())) {
                    newlist.remove(j);
                }
            }
        }
        return newlist;
    }

    @Override
    public boolean configureCheck(Context context, ILocker locker, boolean isNeedLoadAssetCode) {
        String qrcode = ConfigureTools.getQrCode(context);
        if (TextUtils.isEmpty(qrcode)) {
            ConfigureTools.setQrCode(context, Constant.ScannerTYPE.DEWO);
        }
        String qrcodeCom = ConfigureTools.getQrCodeTty(context);
        if (TextUtils.isEmpty(qrcodeCom)) {
            if (ConfigureTools.getQrCode(context).equals(Constant.ScannerTYPE.HONEYWELL_USB) ||
                    ConfigureTools.getQrCode(context).equals(Constant.ScannerTYPE._4102S) ||
                    ConfigureTools.getQrCode(context).equals(Constant.ScannerTYPE.FM50)) {
                ConfigureTools.setQrCodeTty(context, "/dev/ttyACM0");
            } else {
                ConfigureTools.setQrCodeTty(context, "/dev/ttymxc2");
            }
        }
        String printer = ConfigureTools.getPrinter(context);
        if (TextUtils.isEmpty(printer)) {
            ConfigureTools.setPrinter(context, Constant.PrinterTYPE.QR);
        }
        String printerCom = ConfigureTools.getPrinterTty(context);
        if (TextUtils.isEmpty(printerCom)) {
            ConfigureTools.setPrinterTty(context, "/dev/ttymxc4");
        }
        if (isNeedLoadAssetCode) {
            if (DaoTools.getAssetCodes().size() == 0) {
                locker.saveCodes();
            }
        }
        return true;
    }

}

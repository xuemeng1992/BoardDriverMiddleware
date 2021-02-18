package com.rocktech.boarddriver.coremodule.lockcontrol.rocktech;

import android.text.TextUtils;

import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.ICMD;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

public class RocktechCMD implements ICMD {

    @Override
    public byte[] setMCCheck(boolean b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x71;
        if (b) {
            lockOrder[5] = 0x00;
        } else {
            lockOrder[5] = 0x01;
        }
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] setMCLamp(boolean b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x55;
        if (b) {
            lockOrder[5] = 0x00;
        } else {
            lockOrder[5] = 0x01;
        }
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] set485(String check, String baudrate) {
        byte[] lockOrder = new byte[8];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x04;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x61;
        lockOrder[5] = (byte) (Byte.parseByte(check) - 1);
        lockOrder[6] = (byte) (Byte.parseByte(baudrate) - 1);
        lockOrder[7] = SerialPortTools.calcCrc8(lockOrder, 0, 7);
        return lockOrder;
    }

    @Override
    public byte[] getHumidity() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x5C;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] getTemp() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x5B;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] getTempEx() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x5B;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] setWDBC(byte b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x6C;
        lockOrder[5] = b;
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] querWDBC() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x6E;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] querySDBC() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x6F;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] setSDBC(byte b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x6D;
        lockOrder[5] = b;
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] getControl(boolean b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x5D;
        if (b) {
            lockOrder[5] = 0x00; // 0 = 获取控制权
        } else {
            lockOrder[5] = 0x01;
        }
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] doDebug(int channal, boolean b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        switch (channal) {
            case 12:
                lockOrder[4] = 0x52;
                break;
            case 25:
                lockOrder[4] = 0x53;
                break;
            case 123:
                lockOrder[4] = 0x57;
                break;
        }
        lockOrder[5] = (byte) (b ? 0x00 : 0x01); // 0 代表 打开
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] queryPower() {
        byte[] byte1 = {(byte) 0xAA, 0x55, 0x15, 0x00, 0x64};
        byte[] byte2 = {(byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, 0x68, (byte) 0xAA,
                (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, 0x68, 0x01, 0x02, 0x43, (byte) 0xC3,
                (byte) 0xD5, 0x16};
        byte[] byte3 = SerialPortTools.byteMerger(byte1, byte2);
        byte[] byte4 = {0x00};
        byte[] byte5 = SerialPortTools.byteMerger(byte3, byte4);
        byte5[byte5.length - 1] = SerialPortTools.calcCrc8(byte5, 0, byte5.length - 1);
        return byte5;
    }

    @Override
    public byte[] openLock(String str) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = Tools.calculateBoard(str);
        lockOrder[4] = 0x50;
        lockOrder[5] = (byte) (Tools.calculateLock(str) - 1);
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] queryLock(String str) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = Tools.calculateBoard(str);
        lockOrder[4] = 0x51;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] querySingleCabinetLock(String str) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = Tools.calculateBoard(str);
        lockOrder[4] = 0x51;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] controlMainDoor(String str) {
        return new byte[0];
    }

    @Override
    public byte[] controlLamp(int board, boolean open) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = (byte) board; // 板地址
        lockOrder[4] = 0x54;
        lockOrder[5] = open ? (byte) 0x00 : (byte) 0x01;
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] readCode(int board) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = (byte) board;
        lockOrder[4] = 0x60;
        lockOrder[5] = 0x18;
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] setBuzzer(boolean b) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x03;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x5E;
        lockOrder[5] = (byte) (b ? 1 : 0);
        lockOrder[6] = SerialPortTools.calcCrc8(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] setControl(int i, String open, String close) {
        int btO = (int) (Double.parseDouble(open) * 10 + 500);
        int btC = (int) (Double.parseDouble(close) * 10 + 500);
        if (btO >= 0 && btO <= 1500 && btC >= 0 && btC <= 1500) {
            byte[] lockOrder = new byte[10];
            lockOrder[0] = (byte) 0xAA;
            lockOrder[1] = 0x55;
            lockOrder[2] = 0x06;
            lockOrder[3] = 0;
            lockOrder[4] = (byte) i;
            lockOrder[5] = (byte) (btO >> 8);
            lockOrder[6] = (byte) (btO & 0xFF);
            lockOrder[7] = (byte) (btC >> 8);
            lockOrder[8] = (byte) (btC & 0xFF);
            lockOrder[9] = SerialPortTools.calcCrc8(lockOrder, 0, 9);
            return lockOrder;
        }
        return new byte[0];
    }

    @Override
    public byte[] query485Setting() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x70;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] querySetting(int i) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = (byte) i;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] updateBoardId() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = (byte) 0xFF;
        lockOrder[4] = 0x67;
        lockOrder[5] = (byte) 0xFF;
        return lockOrder;
    }

    @Override
    public byte[] writeAssetCode(AssetCodeBean bean) {
        int size = 0;
        String ac = String.format("%-21s", bean.getAssetCode().trim());
        String assetCode = ac.replaceAll("\\s", "N");
        LoggerUtils.Log().e("writeAssetCode: " + assetCode);
        LoggerUtils.Log().e("writeAssetCode: " + bean.toString());
        size += assetCode.length();
        byte[] assetCodebyt = assetCode.getBytes();
        byte[] data = null;
        if (!TextUtils.isEmpty(bean.getProductModel())) {
            size += bean.getProductModel().length();
            byte[] productModel = bean.getProductModel().getBytes();
            data = SerialPortTools.byteMerger(assetCodebyt, productModel);
        }
        size += 3;
        LoggerUtils.Log().e("writeAssetCode   size: " + size);
        byte[] cmd = {(byte) 0xAA, 0x55, (byte) (size + 2), Tools.calculateBoard(bean.getRealBoxId()), 0x5F,
                bean.getBoxId().substring(0, 1).getBytes()[0],
                bean.getBoxId().substring(1, 2).getBytes()[0],
                bean.getBoxId().substring(2, 3).getBytes()[0]};
        byte[] comData;
        if (data == null) {
            comData = SerialPortTools.byteMerger(cmd, assetCodebyt);
        } else {
            comData = SerialPortTools.byteMerger(cmd, data);
        }
        LoggerUtils.Log().e("writeAssetCode comData: " + Tools.byteToHexStr(comData.length, comData));
        byte[] codeCrc = {SerialPortTools.calcCrc8(comData, 0, comData.length)};
        byte[] codes_fl = SerialPortTools.byteMerger(comData, codeCrc);
        return codes_fl;
    }

    @Override
    public byte[] getChipVersion(String boardAddr) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = Tools.calculateBoard(boardAddr);
        lockOrder[4] = (byte) 0x98;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] queryConnectedBoardList(String str) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = Tools.calculateBoard(str);
        lockOrder[4] = 0x51;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] queryMCsetting() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x72;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }

    @Override
    public byte[] ifDoorOpened() {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = (byte) 0xAA;
        lockOrder[1] = 0x55;
        lockOrder[2] = 0x02;
        lockOrder[3] = 0x00;
        lockOrder[4] = 0x56;
        lockOrder[5] = SerialPortTools.calcCrc8(lockOrder, 0, 5);
        return lockOrder;
    }
}

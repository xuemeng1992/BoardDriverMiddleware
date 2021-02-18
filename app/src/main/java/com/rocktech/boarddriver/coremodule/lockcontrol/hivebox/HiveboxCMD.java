package com.rocktech.boarddriver.coremodule.lockcontrol.hivebox;

import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.ICMD;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

public class HiveboxCMD implements ICMD {

    @Override
    public byte[] setMCCheck(boolean b) {
        return new byte[0];
    }

    @Override
    public byte[] setMCLamp(boolean b) {
        return new byte[0];
    }

    @Override
    public byte[] set485(String check, String baudrate) {
        return new byte[0];
    }

    @Override
    public byte[] getHumidity() {
        return new byte[0];
    }

    @Override
    public byte[] getTemp() {
        return new byte[0];
    }

    @Override
    public byte[] getTempEx() {
        return new byte[0];
    }

    @Override
    public byte[] setWDBC(byte b) {
        return new byte[0];
    }

    @Override
    public byte[] querWDBC() {
        return new byte[0];
    }

    @Override
    public byte[] querySDBC() {
        return new byte[0];
    }

    @Override
    public byte[] setSDBC(byte b) {
        return new byte[0];
    }

    @Override
    public byte[] getControl(boolean b) {
        return new byte[0];
    }

    @Override
    public byte[] doDebug(int channal, boolean b) {
        return new byte[0];
    }

    @Override
    public byte[] queryPower() {
        return new byte[0];
    }

    @Override
    public byte[] openLock(String str) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = 0x48;
        lockOrder[1] = 0x42;
        lockOrder[2] = (byte) 0x90;
        lockOrder[3] = 0x02;
        lockOrder[4] = Tools.calculateBoard(str);
        lockOrder[5] = Tools.calculateLock(str);
        lockOrder[6] = SerialPortTools.xorCalc(lockOrder, 0, 7);
        return lockOrder;
    }

    @Override
    public byte[] queryLock(String str) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = 0x48;
        lockOrder[1] = 0x42;
        lockOrder[2] = (byte) 0x96;
        lockOrder[3] = 0x02;
        lockOrder[4] = Tools.calculateBoard(str);
        lockOrder[5] = Tools.calculateLock(str);
        lockOrder[6] = SerialPortTools.xorCalc(lockOrder, 0, 7);
        return lockOrder;
    }

    @Override
    public byte[] querySingleCabinetLock(String str) {
        return new byte[0];
    }

    @Override
    public byte[] controlMainDoor(String str) {

        return new byte[0];
    }

    @Override
    public byte[] controlLamp(int board, boolean open) {
        byte[] lockOrder = new byte[7];
        lockOrder[0] = 0x48;
        lockOrder[1] = 0x42;
        lockOrder[2] = (byte) 0xA3;
        lockOrder[3] = 0x02;
        lockOrder[4] = (byte) board;//板地址 从0开始
        lockOrder[5] = open ? (byte) 0x01 : (byte) 0x00;//1-开 0-关
        lockOrder[6] = SerialPortTools.xorCalc(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] readCode(int board) {
        byte[] lockOrder = new byte[6];
        lockOrder[0] = 0x48;
        lockOrder[1] = 0x42;
        lockOrder[2] = (byte) 0x9d;
        lockOrder[3] = 0x01;
        lockOrder[4] = (byte) board;
        lockOrder[5] = SerialPortTools.xorCalc(lockOrder, 0, 6);
        return lockOrder;
    }

    @Override
    public byte[] writeAssetCode(AssetCodeBean bean) {
        byte[] code1 = {0x48, 0x42, (byte) 0x9c, 0x19, Tools.calculateBoard(bean.getBoxId()),
                bean.getBoxId().substring(0, 1).getBytes()[0],
                bean.getBoxId().substring(1, 2).getBytes()[0],
                bean.getBoxId().substring(2, 3).getBytes()[0]};
        String newCode;
        if (bean.getAssetCode().endsWith("N")) {
            newCode = bean.getAssetCode();
        } else {
            newCode = bean.getAssetCode() + "NNN";
        }
        byte[] code2 = newCode.getBytes();
        byte[] codes = SerialPortTools.byteMerger(code1, code2);
        byte[] code3 = {SerialPortTools.xorCalc(codes, 0, codes.length)};
        byte[] codes_fl = SerialPortTools.byteMerger(codes, code3);
        return codes_fl;
    }

    @Override
    public byte[] setBuzzer(boolean b) {
        return new byte[0];
    }

    @Override
    public byte[] setControl(int i, String open, String close) {
        return new byte[0];
    }

    @Override
    public byte[] query485Setting() {
        return new byte[0];
    }

    @Override
    public byte[] querySetting(int i) {
        return new byte[0];
    }

    @Override
    public byte[] updateBoardId() {
        return new byte[0];
    }

    @Override
    public byte[] getChipVersion(String boardAddr) {
        return new byte[0];
    }

    @Override
    public byte[] queryConnectedBoardList(String str) {
        return new byte[0];
    }

    @Override
    public byte[] queryMCsetting() {
        return new byte[0];
    }

    @Override
    public byte[] ifDoorOpened() {
        return new byte[0];
    }
}

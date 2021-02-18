package com.rocktech.boarddriver.coremodule.lockcontrol.sudiyi;

import com.rairmmd.serialport.CRC16Verify;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.ICMD;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

public class SudiyiCMD implements ICMD {

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
    public byte[] updateBoardId() {
        return new byte[0];
    }

    @Override
    public byte[] getChipVersion(String boardAddr) {
        return new byte[0];
    }

    @Override
    public byte[] getTemp() {
        return new byte[0];
    }

    @Override
    public byte[] writeAssetCode(AssetCodeBean bean) {
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
        byte[] b = new byte[]{
                (byte) 0xFF,
                Tools.sudiyiBoardId(str),
                (byte) 0x04,
                (byte) 0x02,
                (byte) Integer.parseInt(str.substring(1, 3))};
        return SerialPortTools.byteMerger(b, CRC16Verify.CRC_XModem(b));
    }

    @Override
    public byte[] queryLock(String str) {
        byte[] b = new byte[]{
                (byte) 0xFF,
                Tools.sudiyiBoardId(str),
                (byte) 0x04,
                (byte) 0x03,
                (byte) Integer.parseInt(str.substring(1, 3))};
        return SerialPortTools.byteMerger(b, CRC16Verify.CRC_XModem(b));
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
    public byte[] ifDoorOpened() {
        return new byte[0];
    }

    @Override
    public byte[] controlLamp(int board, boolean open) {
        return new byte[0];
    }

    @Override
    public byte[] queryConnectedBoardList(String str) {
        return new byte[0];
    }

    @Override
    public byte[] readCode(int board) {
        return new byte[0];
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
    public byte[] queryMCsetting() {
        return new byte[0];
    }
}

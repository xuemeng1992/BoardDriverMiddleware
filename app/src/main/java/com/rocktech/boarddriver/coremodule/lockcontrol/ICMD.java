package com.rocktech.boarddriver.coremodule.lockcontrol;

import com.rocktech.boarddriver.bean.AssetCodeBean;

public interface ICMD {
    //设置门磁
    byte[] setMCCheck(boolean b);

    //门磁小灯控制
    byte[] setMCLamp(boolean b);

    //设置485抄表校验位和波特率
    byte[] set485(String check, String baudrate);

    //获取当前湿度
    byte[] getHumidity();

    //重新将拨码开关对应的板Id写到flash中
    byte[] updateBoardId();

    //获取板版本
    byte[] getChipVersion(String boardAddr);

    //获取当前温度值
    byte[] getTemp();

    //烧写资产编码
    byte[] writeAssetCode(AssetCodeBean bean);

    //获取主板温度值
    byte[] getTempEx();

    //设置温度补偿
    byte[] setWDBC(byte b);

    //查询温度补偿
    byte[] querWDBC();

    //查询当前适度补偿
    byte[] querySDBC();

    //设置湿度补偿
    byte[] setSDBC(byte b);

    //获取锁孔板风扇，加热器控制权，用于调试这些接口
    byte[] getControl(boolean b);

    //风扇A，风扇B，加热器 手动控制(风扇A=12，风扇B=25，加热器=123)
    byte[] doDebug(int channal, boolean b);

    //485抄表
    byte[] queryPower();

    //开某个箱
    byte[] openLock(String str);

    byte[] querySingleCabinetLock(String str);

    //查询某个箱
    byte[] queryLock(String str);

    //面板锁控制
    byte[] controlMainDoor(String str);

    byte[] ifDoorOpened();

    //开灯
    byte[] controlLamp(int board, boolean open);

    //查询连接的板状态
    byte[] queryConnectedBoardList(String str);

    //查询资产编码
    byte[] readCode(int board);

    //打开/关闭 蜂鸣器
    byte[] setBuzzer(boolean b);

    //设置 温度上阀值，下阀值
    byte[] setControl(int i, String open, String close);

    byte[] query485Setting();

    byte[] querySetting(int i);

    byte[] queryMCsetting();
}

package com.rocktech.boarddriver.coremodule.lockcontrol;

import com.rocktech.boarddriver.base.myinterface.RockInterface;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.AssetCodeDao;
import com.rocktech.boarddriver.bean.CommonDataBean;

import java.util.ArrayList;
import java.util.List;

public interface ILocker {
    //设置门磁
    void setMCCheck(boolean b);

    //门磁小灯控制
    void setMCLamp(boolean b);

    //设置485抄表校验位和波特率
    void set485(String check, String baudrate);

    //获取当前湿度
    void getHumidity();

    //重新将拨码开关对应的板Id写到flash中
    void updateBoardId();

    //获取当前温度值
    void getTemp();

    //烧写资产编码
    void writeCode(final List<AssetCodeBean> list);

    //获取主板温度值
    void getTempEx();

    //设置温度补偿
    void setWDBC(byte b);

    //查询温度补偿
    void querWDBC();

    //查询当前适度补偿
    void querySDBC();

    //设置湿度补偿
    void setSDBC(byte b);

    //获取锁孔板风扇，加热器控制权，用于调试这些接口
    void getControl(boolean b);

    //风扇A，风扇B，加热器 手动控制(风扇A=12，风扇B=25，加热器=123)
    void doDebug(int channal, boolean b);

    //485抄表
    void queryPower();

    //开某个箱
    boolean openLock(String str);

    //查询某个箱
    void queryLock(String str);

    //面板锁控制
    void controlMainDoor(String str);

    void ifDoorOpened();

    void openLock(final String[] batchboxid);

    void queryLock(final String[] batchboxid);

    //查询单柜里指定隔口状态
    void querySingleCabinetLock(final String[] batchboxid);

    void queryAll(String boxids);

    //开灯
    void controlLamp(boolean isOpen);

    void openZlamp();

    void closeZlamp();

    void readCodes();

    boolean saveCodes();

    //打开/关闭 蜂鸣器
    void setBuzzer(boolean b);

    //设置 温度上阀值，下阀值
    void setControl(int i, String open, String close);

    void query485Setting();

    void querySetting(int i);

    void queryMCsetting();

    void getChipVersion(final String[] boardAddr);

    void initRockInterface(RockInterface rockInterface);

    /**
     * 同步方法
     *
     * @return
     */
    //同步读取资产编码
    List<AssetCodeBean> readCodesSyn();

    //同步写入资产编码
    List<Boolean> writeCodeSyn(List<AssetCodeBean> list);

    //同步查询所有隔口状态
    List<Boolean> queryAllLockSyn(String cabinet, String assetCode);

    //查询单隔口同步方法
    boolean queryLockSyn(String str, int boxIndex, AssetCodeDao assetCodeDao);

    //开多个门同步方法
    CommonDataBean openLocksSyn(final String[] batchboxid);

    //开门同步方法
    boolean openLockSyn(String str, int boxIndex, AssetCodeDao assetCodeDao);

    //获取版本同步方法
    ArrayList<String> getChipVersionSyn(final String[] boardAddr);
}

package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.locker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rairmmd.serialport.LibTools;
import com.rairmmd.serialport.OnDataReceiverListener;
import com.rocktech.boarddriver.base.myinterface.RockInterface;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.AssetCodeDao;
import com.rocktech.boarddriver.bean.CommonDataBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.CMDFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.HandleFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.Common;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.ThreeColumns;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.AssetCodeObservable;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.BoxDataObservable;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.ChipVersionObservable;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.CommonDataObservable;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.DataObserver;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer.WriteAssetCodeObservable;
import com.rocktech.boarddriver.tools.Action;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.tools.SPHelper;
import com.rocktech.boarddriver.tools.SerialPortTools;
import com.rocktech.boarddriver.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android_serialport_api.LockerControl;


public class CommonLocker implements ILocker, OnDataReceiverListener {

    private LockerControl lockerControl;
    private byte[] LISHIZHI = null;
    private int type;
    private Context context;
    private RockInterface mRockInterface;
    private BoxDataObservable boxDataObservable;
    private AssetCodeObservable assetCodeObservable;
    private ChipVersionObservable chipVersionObservable;
    private WriteAssetCodeObservable writeAssetCodeObservable;
    private CommonDataObservable commonDataObservable;

    @SuppressLint("StaticFieldLeak")
    private static volatile CommonLocker commonLocker;

    public static CommonLocker getInstance(Context context, int type) {
        if (commonLocker == null) {
            synchronized (CommonLocker.class) {
                if (commonLocker == null) {
                    commonLocker = new CommonLocker(context, type);
                }
            }
        }
        return commonLocker;
    }

    private CommonLocker(Context context, int type) {
        lockerControl = new LockerControl(ConfigureTools.getCustomerConfigBean(context, Constant.CustomerTYPE.DEFAULT).getCom(), Constant.LOCKER_BAUDRATE);
        if (lockerControl.openCOM()) {
            lockerControl.setOnDataReceiverListener(this);
        }
        this.type = type;
        this.context = context;
        this.boxDataObservable = new BoxDataObservable();
        this.assetCodeObservable = new AssetCodeObservable();
        this.chipVersionObservable = new ChipVersionObservable();
        this.writeAssetCodeObservable = new WriteAssetCodeObservable();
        this.commonDataObservable = new CommonDataObservable();
    }

    @Override
    public int onDataReceiver(byte[] buffer, int size, int handle) {
        if (LISHIZHI == null) {
            LISHIZHI = SerialPortTools.subBytes(buffer, 0, size);
        } else {
            LISHIZHI = SerialPortTools.byteMerger(LISHIZHI, SerialPortTools.subBytes(buffer, 0, size));
        }
        LoggerUtils.Log().i("接收到的数据>>" + LISHIZHI.length + ">>>" + Tools.byteToHexStr(LISHIZHI.length, LISHIZHI));
        //瑞迅锁控板
        if (type == Constant.BoardTYPE.ROCKTECH) {
            int checkResultTYPE = (int) HandleFactory.produceHandle(type).checkData(LISHIZHI, buffer);
            LoggerUtils.Log().i("checkResultTYPE>>>>>>>" + checkResultTYPE);
            if (checkResultTYPE == Constant.CheckResultTYPE.DATA_HEADE_RERROR) {
                LISHIZHI = null;
                return checkResultTYPE;
            }
            if (checkResultTYPE != Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH && checkResultTYPE != -1) {
                toHandle(LISHIZHI, checkResultTYPE, handle);
                LISHIZHI = null;
            }
            return checkResultTYPE;
        }
        //丰巢锁控板
        if (type == Constant.BoardTYPE.HIVEBOX) {
            int checkResultTYPE = (int) HandleFactory.produceHandle(type).checkData(LISHIZHI, buffer);
            LoggerUtils.Log().i("checkResultTYPE>>>>>>>" + checkResultTYPE);
            if (checkResultTYPE != Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH && checkResultTYPE != -1) {
                toHandle(LISHIZHI, checkResultTYPE, handle);
                LISHIZHI = null;
            }
            return checkResultTYPE;
        }
        if (type == Constant.BoardTYPE.SUDIYI) {
            int checkResultTYPE = (int) HandleFactory.produceHandle(type).checkData(LISHIZHI, buffer);
            LoggerUtils.Log().i("checkResultTYPE>>>>>>>" + checkResultTYPE);
            if (checkResultTYPE != Constant.CheckResultTYPE.NOT_ENOUGH_LENGTH && checkResultTYPE != -1) {
                toHandle(LISHIZHI, checkResultTYPE, handle);
                LISHIZHI = null;
            }
            return checkResultTYPE;
        }
        return 0;
    }


    private void toHandle(byte[] data, int checkResultTYPE, int handle) {
        LibTools.writeBehaviorLog("toHandle>>>" + handle + ">>正在处理的数据:" + Tools.byteToHexStr(data.length, data));
        switch (handle) {
            case Constant.FC_GET_TEMP_FOR_OTHER:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                String mock = HandleFactory.produceHandle(type).fcGetTempForOther(data, context);
                Intent getTempForOtherIntent = new Intent();
                getTempForOtherIntent.setAction("android.intent.action.hal.sys.result");
                getTempForOtherIntent.putExtra("result", mock);
                context.sendBroadcast(getTempForOtherIntent);
                break;
            case Constant.FLAG_GET_CHIP_VERSION:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    chipVersionObservable.setIndex(99);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    chipVersionObservable.addBoardV(Tools.getBoxAddr(HandleFactory.getCurrentBoxId()) + "：" + HandleFactory.produceHandle(type).flagGetChipVersion(data));
                    chipVersionObservable.setCourtBoardV(HandleFactory.produceHandle(type).flagGetChipVersion(data));
                    chipVersionObservable.setIndex(chipVersionObservable.getIndex() + 1);
                }
                break;
            case Constant.STATE_QUERY_SINGLE_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    commonDataObservable.setData(new CommonDataBean(HandleFactory.produceHandle(type).stateQuerySingleLock(data, HandleFactory.getCurrentBoxId(), context)));
                } else if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    commonDataObservable.setData(new CommonDataBean(null));
                }
                break;
            case Constant.STATE_OPEN_SINGLE_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    commonDataObservable.setData(new CommonDataBean(HandleFactory.produceHandle(type).stateOpenSingleLock(data, ThreeColumns.getRealBoxName(HandleFactory.getCurrentBoxId()), context)));
                } else if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    commonDataObservable.setData(new CommonDataBean(null));
                }
                break;
            case Constant.STATE_QUERY_ALL_LOCK_FOR_COURT:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    commonDataObservable.setData(new CommonDataBean(HandleFactory.produceHandle(type).checkAllLockState(data, HandleFactory.getCurrentBoxId(), HandleFactory.getCurrentAssetCode(), context)));
                } else if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    commonDataObservable.setData(new CommonDataBean(null));
                }
                break;
            case Constant.STATE_OPEN_MUTI_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    boxDataObservable.setIndex(99);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    boxDataObservable.addHasOpened(HandleFactory.produceHandle(type).stateOpenSingleLock(data, HandleFactory.getCurrentBoxId(), context));
                    boxDataObservable.setIndex(boxDataObservable.getIndex() + 1);
                }
                break;
            case Constant.STATE_QUERY_MUTI_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    boxDataObservable.setIndex(99);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    boxDataObservable.addHasOpened(HandleFactory.produceHandle(type).stateQuerySingleLock(data, HandleFactory.getCurrentBoxId(), context));
                    boxDataObservable.setIndex(boxDataObservable.getIndex() + 1);
                }
                break;
            case Constant.STATE_QUERY_SINGLE_CABINET_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    Intent lockStatesIntent = new Intent("android.intent.action.hal.iocontroller.querySingleCabinetRes");
                    lockStatesIntent.putExtra("boxList", HandleFactory.getCurrentBoxList());
                    lockStatesIntent.putExtra("lockStates", HandleFactory.produceHandle(type).singleCabinetLockState(data, HandleFactory.getCurrentBoxList(), null, context));
                    context.sendBroadcast(lockStatesIntent);
                }
                break;
            case Constant.STATE_QUERY_OPENED_LOCK:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    boxDataObservable.setIndex(99);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    String[] needQueryLock = Common.getCommQueryLock(HandleFactory.getCurrentBoxId());
                    for (String s : needQueryLock) {
                        boxDataObservable.addbatchBoxId(s);
                        boxDataObservable.addHasOpened(HandleFactory.produceHandle(type).stateQuerySingleLock(data, s, context));
                    }
                    boxDataObservable.setIndex(boxDataObservable.getIndex() + 1);
                }
                break;
            case Constant.FLAG_WRITE_ASSET_CODE:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    writeAssetCodeObservable.addResult("板地址" + writeAssetCodeObservable.getIndex() + "：写入成功");
                    writeAssetCodeObservable.addResult1(true);
                    writeAssetCodeObservable.setIndex(writeAssetCodeObservable.getIndex() + 1);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    writeAssetCodeObservable.addResult("板地址" + writeAssetCodeObservable.getIndex() + "：写入失败");
                    writeAssetCodeObservable.addResult1(false);
                    writeAssetCodeObservable.setIndex(writeAssetCodeObservable.getIndex() + 1);
                }
                break;
            case Constant.STATE_HITESTER_READ_ASSETCODE:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    assetCodeObservable.setIndex(-1);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    if (HandleFactory.produceHandle(type).readAssetCode(data) != null) {
                        LoggerUtils.Log().e(HandleFactory.produceHandle(type).readAssetCode(data).toString());
                        assetCodeObservable.addCodeBean(HandleFactory.produceHandle(type).readAssetCode(data));
                        assetCodeObservable.setIndex(assetCodeObservable.getIndex() + 1);
                    }
                }
                break;
            case Constant.STATE_DOOR_MAGNET_CHECK:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                if (HandleFactory.produceHandle(type).handleDoorMagnet(data, context)) {
                    Intent intent = new Intent("android.intent.action.hal.guard.event");
                    intent.putExtra("eventType", "BoxCoreOpened");
                    intent.putExtra("eventArg", "");
                    context.sendBroadcast(intent);
                }
                break;
            case Constant.STATE_485_POWER_READ:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                if (checkResultTYPE == Constant.CheckResultTYPE._485_ENOUGH_LENGTH) {
                    byte[] LINSHI485 = HandleFactory.produceHandle(type).state485PowerRead(data);
                    LoggerUtils.Log().i("LINSHI485>>" + LINSHI485.length + ">>>" + Tools.byteToHexStr(LINSHI485.length, LINSHI485));
                    Intent intent485 = new Intent(Action.Query485);
                    if ((LINSHI485 != null) && (LINSHI485.length == 22)) {
                        if ((LINSHI485[0] != -2) || (LINSHI485[21] != 0x16)) {
                            intent485.putExtra("meter", "not enough data");
                        } else {
                            intent485.putExtra("meter", SerialPortTools.bcd2Str(SerialPortTools.subBytes(LINSHI485, 16, 4)));
                        }
                    } else {
                        intent485.putExtra("meter", "not enough data");
                    }
                    context.sendBroadcast(intent485);
                }
                break;
            case Constant.STATE_GET_CURRENT_TEMP:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                double f1 = HandleFactory.produceHandle(type).stateGetCurrentTemp(data);
                if (f1 != -1) {
                    Intent intent = new Intent(Action.Temp);
                    intent.putExtra("temp", f1 + "");
                    context.sendBroadcast(intent);
                }
                break;
            case Constant.STATE_GET_CURRENT_HUMIDITY:
                break;
            case Constant.STATE_QUERY_WDBC:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                Intent intent = new Intent(Action.queryTemp);
                intent.putExtra("temp", HandleFactory.produceHandle(type).stateQueryWdbc(data) + "");
                context.sendBroadcast(intent);
                break;
            case Constant.STATE_QUERY_SDBC:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                Intent sdintent = new Intent(Action.queryShidu);
                sdintent.putExtra("shidu", HandleFactory.produceHandle(type).stateQuerySdbc(data) + "");
                context.sendBroadcast(sdintent);
                break;
            case Constant.STATE_485_SET:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    Intent intent485 = new Intent(Action.query485SET);
                    Bundle b = new Bundle();
                    b.putIntArray("state_485_set", HandleFactory.produceHandle(type).state485Set(data));
                    intent485.putExtras(b);
                    context.sendBroadcast(intent485);
                }
                break;
            case Constant.STATE_QUERY_TEMP:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    //获取温度重发机制
                    querySetting(HandleFactory.getCurrentCMD());
                    break;
                }
                float[] temps = HandleFactory.produceHandle(type).stateQueryTemp(data);
                switch (data[4]) {
                    case 0x69:
                        if (temps == null) {
                            //获取温度重发机制
                            querySetting(HandleFactory.getCurrentCMD());
                            break;
                        }
                        intent = new Intent(Action.query12SET);
                        intent.putExtra("temOpen", temps[0] + "");
                        intent.putExtra("temClose", temps[1] + "");
                        context.sendBroadcast(intent);
                        break;
                    case 0x6A:
                        if (temps == null) {
                            //获取温度重发机制
                            querySetting(HandleFactory.getCurrentCMD());
                            break;
                        }
                        intent = new Intent(Action.query25SET);
                        intent.putExtra("temOpen", temps[0] + "");
                        intent.putExtra("temClose", temps[1] + "");
                        context.sendBroadcast(intent);
                        break;
                    case 0x6B:
                        if (temps == null) {
                            //获取温度重发机制
                            querySetting(HandleFactory.getCurrentCMD());
                            break;
                        }
                        intent = new Intent(Action.query26SET);
                        intent.putExtra("temOpen", temps[0] + "");
                        intent.putExtra("temClose", temps[1] + "");
                        context.sendBroadcast(intent);
                        break;
                    default:
                        break;
                }
                break;
            case Constant.STATE_QUERY_DOOR_MAGNET:
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    break;
                }
                Intent mcintent = new Intent(Action.queryMCSET);
                mcintent.putExtra("mcSet", HandleFactory.produceHandle(type).stateQueryDoorMagnet(data));
                context.sendBroadcast(mcintent);
                break;
            default:
                if (checkResultTYPE == Constant.CheckResultTYPE.RESULT_OK) {
                    Intent defIntent = new Intent(Action.OVER);
                    defIntent.putExtra("handle", handle);
                    context.sendBroadcast(defIntent);
                }
                if (checkResultTYPE == Constant.CheckResultTYPE.OVERTIME) {
                    Intent defIntent = new Intent(Action.OVERTIME);
                    defIntent.putExtra("handle", handle);
                    context.sendBroadcast(defIntent);
                }
                break;
        }
    }

    @Override
    public void onSendComplete(byte[] buffer, int size, int handle) {
        Intent sendCompleteIntent = new Intent(Action.sendComplete);
        sendCompleteIntent.putExtra("handle", handle);
        sendCompleteIntent.putExtra("data", buffer);
        context.sendBroadcast(sendCompleteIntent);
    }

    @Override
    public void setMCCheck(boolean b) {
        lockerControl.addData(CMDFactory.produceCMD(type).setMCCheck(b), false, Constant.MCCHECK);
    }

    @Override
    public void setMCLamp(boolean b) {
        lockerControl.addData(CMDFactory.produceCMD(type).setMCLamp(b), false, Constant.MCLAMP);
    }

    @Override
    public void set485(String check, String baudrate) {
        lockerControl.addData(CMDFactory.produceCMD(type).set485(check, baudrate), false, Constant.SET485);
    }

    @Override
    public void getHumidity() {
        lockerControl.addData(CMDFactory.produceCMD(type).getHumidity(), false, Constant.STATE_GET_CURRENT_HUMIDITY);
    }

    @Override
    public void updateBoardId() {
        lockerControl.addData(CMDFactory.produceCMD(type).updateBoardId(), false, Constant.FLAG_UPDATE_BOARD_ADDR);
    }

    @Override
    public void getChipVersion(final String[] boardAddr) {
        chipVersionObservable.initData();
        HandleFactory.setCurrentBoxId(boardAddr[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).getChipVersion(boardAddr[0]), false, Constant.FLAG_GET_CHIP_VERSION);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataChipVersion(new DataObserver.IUpdataChipVersion() {
            @Override
            public void updataChipVersion(ChipVersionObservable chipVersionObservable) {
                if (chipVersionObservable.getIndex() < boardAddr.length) {
                    HandleFactory.setCurrentBoxId(boardAddr[chipVersionObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).getChipVersion(boardAddr[chipVersionObservable.getIndex()]), false, Constant.FLAG_GET_CHIP_VERSION);
                } else {
                    if (mRockInterface != null) {
                        mRockInterface.getChipVer(chipVersionObservable.getBoardVs());
                    }
                    chipVersionObservable.deleteObservers();
                }
            }
        });
        chipVersionObservable.addObserver(dataObserver);
    }

    @Override
    public void getTemp() {
        lockerControl.addData(CMDFactory.produceCMD(type).getTemp(), false, Constant.STATE_GET_CURRENT_TEMP);
    }

    @Override
    public void getTempEx() {
        lockerControl.addData(CMDFactory.produceCMD(type).getTempEx(), false, Constant.FC_GET_TEMP_FOR_OTHER);
    }

    @Override
    public void setWDBC(byte b) {
        lockerControl.addData(CMDFactory.produceCMD(type).setWDBC(b), false, Constant.SETWDBC);
    }

    @Override
    public void querWDBC() {
        lockerControl.addData(CMDFactory.produceCMD(type).querWDBC(), false, Constant.STATE_QUERY_WDBC);
    }

    @Override
    public void querySDBC() {
        lockerControl.addData(CMDFactory.produceCMD(type).querySDBC(), false, Constant.STATE_QUERY_SDBC);
    }

    @Override
    public void setSDBC(byte b) {
        lockerControl.addData(CMDFactory.produceCMD(type).setSDBC(b), false, Constant.SETSDBC);
    }

    @Override
    public void getControl(boolean b) {
        lockerControl.addData(CMDFactory.produceCMD(type).getControl(b), false, Constant.GETCONTROL);
    }

    @Override
    public void doDebug(int channal, boolean b) {
        lockerControl.addData(CMDFactory.produceCMD(type).doDebug(channal, b), false, Constant.DODEBUG);
    }

    @Override
    public void queryPower() {
        lockerControl.addData(CMDFactory.produceCMD(type).queryPower(), false, Constant.STATE_485_POWER_READ);
    }

    @Override
    public void controlMainDoor(String str) {
        lockerControl.addData(CMDFactory.produceCMD(type).controlMainDoor(str), false, Constant.CONTROLMAINDOOR);
    }

    @Override
    public synchronized boolean openLock(String str) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandleFactory.setCurrentBoxId(str);
        lockerControl.addData(CMDFactory.produceCMD(type).openLock(ThreeColumns.getRealBoxName(str)), false, Constant.STATE_OPEN_SINGLE_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            Intent openSingleIntent = new Intent("android.intent.action.hal.iocontroller.querydata");
            openSingleIntent.putExtra("boxid", HandleFactory.getCurrentBoxId());
            openSingleIntent.putExtra("isopened", (boolean) bean.getO1());
            context.sendBroadcast(openSingleIntent);
            return (boolean) bean.getO1();
        } else {
            commonDataObservable.deleteObservers();
            return false;
        }
    }


    @Override
    public void openLock(final String[] batchboxid) {
        boxDataObservable.initData(batchboxid[0]);
        HandleFactory.setCurrentBoxId(batchboxid[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).openLock(batchboxid[0]), false, Constant.STATE_OPEN_MUTI_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataBox(new DataObserver.IUpdataBox() {
            @Override
            public void updataBox(BoxDataObservable boxDataObservable) {
                if (boxDataObservable.getIndex() < batchboxid.length) {
                    boxDataObservable.addbatchBoxId(batchboxid[boxDataObservable.getIndex()]);
                    HandleFactory.setCurrentBoxId(batchboxid[boxDataObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).openLock(batchboxid[boxDataObservable.getIndex()]), false, Constant.STATE_OPEN_MUTI_LOCK);
                } else {
                    sendOpenLockResult(boxDataObservable);
                }
            }
        });
        boxDataObservable.addObserver(dataObserver);
    }

    private void sendOpenLockResult(BoxDataObservable boxDataObservable) {
        boolean[] result = new boolean[boxDataObservable.getHasOpeneds().size()];
        for (int i = 0; i < boxDataObservable.getHasOpeneds().size(); i++) {
            result[i] = boxDataObservable.getHasOpeneds().get(i);
        }
        Intent intent = new Intent("android.intent.action.hal.iocontroller.batchopen.result");
        intent.putExtra("batchboxid", boxDataObservable.getBatchBoxIds().toArray(new String[boxDataObservable.getBatchBoxIds().size()]));
        intent.putExtra("opened", result);
        context.sendBroadcast(intent);
        boxDataObservable.deleteObservers();
    }


    @Override
    public synchronized void queryLock(String str) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandleFactory.setCurrentBoxId(str);
        lockerControl.addData(CMDFactory.produceCMD(type).queryLock(str), false, Constant.STATE_QUERY_SINGLE_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            Intent openSingleIntent = new Intent("android.intent.action.hal.iocontroller.querydata");
            openSingleIntent.putExtra("boxid", HandleFactory.getCurrentBoxId());
            openSingleIntent.putExtra("isopened", (boolean) bean.getO1());
            context.sendBroadcast(openSingleIntent);
        }
        commonDataObservable.deleteObservers();
    }

    @Override
    public void queryLock(final String[] batchboxid) {
        boxDataObservable.initData(batchboxid[0]);
        HandleFactory.setCurrentBoxId(batchboxid[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).queryLock(batchboxid[0]), false, Constant.STATE_QUERY_MUTI_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataBox(new DataObserver.IUpdataBox() {
            @Override
            public void updataBox(BoxDataObservable boxDataObservable) {
                if (boxDataObservable.getIndex() < batchboxid.length) {
                    boxDataObservable.addbatchBoxId(batchboxid[boxDataObservable.getIndex()]);
                    HandleFactory.setCurrentBoxId(batchboxid[boxDataObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).queryLock(batchboxid[boxDataObservable.getIndex()]), false, Constant.STATE_QUERY_MUTI_LOCK);
                } else {
                    sendQueryLockResult(boxDataObservable);
                }
            }
        });
        boxDataObservable.addObserver(dataObserver);
    }

    private void sendQueryLockResult(BoxDataObservable boxDataObservable) {
        boolean[] result = new boolean[boxDataObservable.getHasOpeneds().size()];
        for (int i = 0; i < boxDataObservable.getHasOpeneds().size(); i++) {
            result[i] = boxDataObservable.getHasOpeneds().get(i);
        }
        Intent intent = new Intent("android.intent.action.hal.iocontroller.batchopen.result");
        intent.putExtra("batchboxid", boxDataObservable.getBatchBoxIds().toArray(new String[boxDataObservable.getBatchBoxIds().size()]));
        intent.putExtra("opened", result);
        context.sendBroadcast(intent);
        boxDataObservable.deleteObservers();
    }

    @Override
    public void querySingleCabinetLock(String[] batchboxid) {
        HandleFactory.setCurrentBoxList(batchboxid);
        lockerControl.addData(CMDFactory.produceCMD(type).querySingleCabinetLock(batchboxid[0]), false, Constant.STATE_QUERY_SINGLE_CABINET_LOCK);
    }

    @Override
    public void queryAll(String boxids) {
        if (boxids.length() % 3 != 0) {
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String[] batchboxid = new String[boxids.length() / 3];
        for (int i = 0; i < batchboxid.length; i++) {
            batchboxid[i] = boxids.substring(i * 3, i * 3 + 3);
        }
        boxDataObservable.initData(null);
        HandleFactory.setCurrentBoxId(batchboxid[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).queryLock(batchboxid[0]), false, Constant.STATE_QUERY_OPENED_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataBox(new DataObserver.IUpdataBox() {
            @Override
            public void updataBox(BoxDataObservable boxDataObservable) {
                if (boxDataObservable.getIndex() < batchboxid.length) {
                    boxDataObservable.addbatchBoxId(batchboxid[boxDataObservable.getIndex()]);
                    HandleFactory.setCurrentBoxId(batchboxid[boxDataObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).queryLock(batchboxid[boxDataObservable.getIndex()]), false, Constant.STATE_QUERY_OPENED_LOCK);
                } else {
                    countDownLatch.countDown();
                }
            }
        });
        boxDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queryAllResult(boxDataObservable);
    }

    private void queryAllResult(BoxDataObservable boxDataObservable) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < boxDataObservable.getHasOpeneds().size(); i++) {
            if (boxDataObservable.getHasOpeneds().get(i)) {
                list.add(boxDataObservable.getBatchBoxIds().get(i));
            }
        }
        String[] boxOpened = list.toArray(new String[list.size()]);
        Intent intent = new Intent("android.intent.action.hal.iocontroller.queryAllData");
        intent.putExtra("openedBoxes", boxOpened);
        context.sendBroadcast(intent);
        boxDataObservable.deleteObservers();
    }


    @Override
    public void ifDoorOpened() {
        lockerControl.addData(CMDFactory.produceCMD(type).ifDoorOpened(), false, Constant.STATE_DOOR_MAGNET_CHECK);
    }

    @Override
    public void openZlamp() {
        lockerControl.addData(CMDFactory.produceCMD(type).controlLamp(0x00, true), false, Constant.OPENZLAMP);
    }

    @Override
    public void closeZlamp() {
        lockerControl.addData(CMDFactory.produceCMD(type).controlLamp(0x00, false), false, Constant.CLOSEZLAMP);
    }

    @Override
    public void controlLamp(boolean isOpen) {
        int currentHandle;
        if (isOpen) {
            currentHandle = Constant.OPENLAMP;
        } else {
            currentHandle = Constant.CLOSELAMP;
        }
        int connectBoxSize = SPHelper.getInstance(context).getInt(Constant.SPKEY.CONNECTBOXSIZE, 13);
        for (int iNum = 1; iNum < connectBoxSize; iNum++) {
            lockerControl.addData(CMDFactory.produceCMD(type).controlLamp(iNum, isOpen), false, currentHandle);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void writeCode(final List<AssetCodeBean> list) {
        writeAssetCodeObservable.initData();
        lockerControl.addData(CMDFactory.produceCMD(type).writeAssetCode(list.get(0)), false, Constant.FLAG_WRITE_ASSET_CODE);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiWriteAssetCode(new DataObserver.IWriteAssetCode() {
            @Override
            public void writeAssetCode(WriteAssetCodeObservable writeAssetCodeObservable) {
                if (writeAssetCodeObservable.getIndex() < list.size()) {
                    lockerControl.addData(CMDFactory.produceCMD(type).writeAssetCode(list.get(writeAssetCodeObservable.getIndex())), false, Constant.FLAG_WRITE_ASSET_CODE);
                } else {
                    sendResult(writeAssetCodeObservable);
                }
            }
        });
        writeAssetCodeObservable.addObserver(dataObserver);
    }

    private void sendResult(WriteAssetCodeObservable writeAssetCodeObservable) {
        Intent intent = new Intent("android.intent.action.writeAssetCode.result");
        intent.putExtra("writeAssetCode_result", writeAssetCodeObservable.getResultList());
        context.sendBroadcast(intent);
        writeAssetCodeObservable.deleteObservers();
    }

    @Override
    public void readCodes() {
        assetCodeObservable.initData();
        lockerControl.addData(CMDFactory.produceCMD(type).readCode(0), false, Constant.STATE_HITESTER_READ_ASSETCODE);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataAssetCode(new DataObserver.IUpdataAssetCode() {
            @Override
            public void updataAssetCode(AssetCodeObservable assetCodeObservable) {
                LoggerUtils.Log().e("-------->index:" + assetCodeObservable.getIndex());
                if (assetCodeObservable.getIndex() != -1) {
                    lockerControl.addData(CMDFactory.produceCMD(type).readCode(assetCodeObservable.getIndex()), false, Constant.STATE_HITESTER_READ_ASSETCODE);
                } else {
                    sendCodes(assetCodeObservable);
                }
            }
        });
        assetCodeObservable.addObserver(dataObserver);
    }

    private void sendCodes(AssetCodeObservable assetCodeObservable) {
        ArrayList<String> code1 = new ArrayList<>();//资产编码
        ArrayList<String> code2 = new ArrayList<>();//板编号
        for (int i = 0; i < assetCodeObservable.getCodeBeans().size(); i++) {
            code1.add(assetCodeObservable.getCodeBeans().get(i).getAssetCode());
            code2.add(assetCodeObservable.getCodeBeans().get(i).getBoxId());
        }
        String[] assetCodeArrays = code1.toArray(new String[code1.size()]);
        String[] boxIdArrays = code2.toArray(new String[code2.size()]);
        Intent intent = new Intent("android.intent.action.hal.boxinfo.result");
        if (assetCodeArrays != null && assetCodeArrays.length != 0) {
            intent.putExtra("array.assetCode", assetCodeArrays);
        }
        if (boxIdArrays != null && boxIdArrays.length != 0) {
            intent.putExtra("array.boxId", boxIdArrays);
        }
        context.sendBroadcast(intent);
        Intent innerIntent = new Intent("android.intent.action.hal.boxinfo.result.inner");
        innerIntent.putExtra("assetCode_list", assetCodeObservable.getCodeBeans());
        context.sendBroadcast(innerIntent);
        assetCodeObservable.deleteObservers();
    }

    @Override
    public boolean saveCodes() {
        return true;
    }

    @Override
    public void setBuzzer(boolean b) {
        lockerControl.addData(CMDFactory.produceCMD(type).setBuzzer(b), false, Constant.SETBUZZER);
    }

    @Override
    public void setControl(int i, String open, String close) {
        lockerControl.addData(CMDFactory.produceCMD(type).setControl(i, open, close), false, Constant.SETCONTROL);
    }

    @Override
    public void query485Setting() {
        lockerControl.addData(CMDFactory.produceCMD(type).query485Setting(), false, Constant.STATE_485_SET);
    }

    @Override
    public void querySetting(int i) {
        HandleFactory.setCurrentCMD(i);
        lockerControl.addData(CMDFactory.produceCMD(type).querySetting(i), false, Constant.STATE_QUERY_TEMP);
    }

    @Override
    public void queryMCsetting() {
        lockerControl.addData(CMDFactory.produceCMD(type).queryMCsetting(), false, Constant.STATE_QUERY_DOOR_MAGNET);
    }

    //获取版本同步方法
    @Override
    public synchronized ArrayList<String> getChipVersionSyn(final String[] boardAddr) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        chipVersionObservable.initData();
        HandleFactory.setCurrentBoxId(boardAddr[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).getChipVersion(boardAddr[0]), false, Constant.FLAG_GET_CHIP_VERSION);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataChipVersion(new DataObserver.IUpdataChipVersion() {
            @Override
            public void updataChipVersion(ChipVersionObservable chipVersionObservable) {
                if (chipVersionObservable.getIndex() < boardAddr.length) {
                    HandleFactory.setCurrentBoxId(boardAddr[chipVersionObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).getChipVersion(boardAddr[chipVersionObservable.getIndex()]), false, Constant.FLAG_GET_CHIP_VERSION);
                } else {
                    countDownLatch.countDown();
                }
            }
        });
        chipVersionObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chipVersionObservable.deleteObservers();
        return chipVersionObservable.getCourtBoardVs();
    }

    //开门同步方法
    @Override
    public synchronized boolean openLockSyn(String str, int boxIndex, AssetCodeDao assetCodeDao) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String bName;
        if (boxIndex != -1) {
            String bIndex = String.format("%02d", boxIndex);
            bName = str + bIndex;
        } else {
            bName = str;
        }
        lockerControl.addData(CMDFactory.produceCMD(type).openLock(bName), false, Constant.STATE_OPEN_SINGLE_LOCK);
        HandleFactory.setCurrentBoxId(bName);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (boolean) bean.getO1();
        } else {
            commonDataObservable.deleteObservers();
            return false;
        }
    }

    //开多个门同步方法
    @Override
    public synchronized CommonDataBean openLocksSyn(final String[] batchboxid) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        boxDataObservable.initData(batchboxid[0]);
        HandleFactory.setCurrentBoxId(batchboxid[0]);
        lockerControl.addData(CMDFactory.produceCMD(type).openLock(batchboxid[0]), false, Constant.STATE_OPEN_MUTI_LOCK);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataBox(new DataObserver.IUpdataBox() {
            @Override
            public void updataBox(BoxDataObservable boxDataObservable) {
                if (boxDataObservable.getIndex() < batchboxid.length) {
                    boxDataObservable.addbatchBoxId(batchboxid[boxDataObservable.getIndex()]);
                    HandleFactory.setCurrentBoxId(batchboxid[boxDataObservable.getIndex()]);
                    lockerControl.addData(CMDFactory.produceCMD(type).openLock(batchboxid[boxDataObservable.getIndex()]), false, Constant.STATE_OPEN_MUTI_LOCK);
                } else {
                    countDownLatch.countDown();
                }
            }
        });
        boxDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boxDataObservable.deleteObservers();
        return new CommonDataBean(boxDataObservable.getHasOpeneds());
    }

    //查询单隔口同步方法
    @Override
    public synchronized boolean queryLockSyn(String str, int boxIndex, AssetCodeDao assetCodeDao) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String bName;
        if (boxIndex != -1) {
            String bIndex = String.format("%02d", boxIndex);
            bName = str + bIndex;
        } else {
            bName = str;
        }
        lockerControl.addData(CMDFactory.produceCMD(type).queryLock(bName), false, Constant.STATE_QUERY_SINGLE_LOCK);
        HandleFactory.setCurrentBoxId(bName);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (boolean) bean.getO1();
        } else {
            commonDataObservable.deleteObservers();
            return false;
        }
    }

    //同步查询所有隔口状态
    @Override
    public synchronized List<Boolean> queryAllLockSyn(String cabinet, String assetCode) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String boxId = cabinet + "01";
        HandleFactory.setCurrentBoxId(boxId);
        HandleFactory.setCurrentAssetCode(assetCode);
        lockerControl.addData(CMDFactory.produceCMD(type).queryLock(boxId), false, Constant.STATE_QUERY_ALL_LOCK_FOR_COURT);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiCommonDataObservable(new DataObserver.ICommonDataObservable() {
            @Override
            public void commonDataObservable(CommonDataObservable commonDataObservable) {
                countDownLatch.countDown();
            }
        });
        commonDataObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonDataBean bean = commonDataObservable.getData();
        if (bean.getO1() != null) {
            commonDataObservable.deleteObservers();
            return (List<Boolean>) bean.getO1();
        }
        commonDataObservable.deleteObservers();
        return null;
    }

    //同步写入资产编码
    @Override
    public synchronized List<Boolean> writeCodeSyn(final List<AssetCodeBean> list) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        writeAssetCodeObservable.initData();
        lockerControl.addData(CMDFactory.produceCMD(type).writeAssetCode(list.get(0)), false, Constant.FLAG_WRITE_ASSET_CODE);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiWriteAssetCode(new DataObserver.IWriteAssetCode() {
            @Override
            public void writeAssetCode(WriteAssetCodeObservable writeAssetCodeObservable) {
                if (writeAssetCodeObservable.getIndex() < list.size()) {
                    lockerControl.addData(CMDFactory.produceCMD(type).writeAssetCode(list.get(writeAssetCodeObservable.getIndex())), false, Constant.FLAG_WRITE_ASSET_CODE);
                } else {
                    writeAssetCodeObservable.deleteObservers();
                    countDownLatch.countDown();
                }
            }
        });
        writeAssetCodeObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return writeAssetCodeObservable.getResultList1();
    }

    //同步读取资产编码
    @Override
    public synchronized List<AssetCodeBean> readCodesSyn() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        assetCodeObservable.initData();
        lockerControl.addData(CMDFactory.produceCMD(type).readCode(0), false, Constant.STATE_HITESTER_READ_ASSETCODE);
        DataObserver dataObserver = new DataObserver();
        dataObserver.setiUpdataAssetCode(new DataObserver.IUpdataAssetCode() {
            @Override
            public void updataAssetCode(AssetCodeObservable assetCodeObservable) {
                if (assetCodeObservable.getIndex() != -1) {
                    lockerControl.addData(CMDFactory.produceCMD(type).readCode(assetCodeObservable.getIndex()), false, Constant.STATE_HITESTER_READ_ASSETCODE);
                } else {
                    assetCodeObservable.deleteObservers();
                    countDownLatch.countDown();
                }
            }
        });
        assetCodeObservable.addObserver(dataObserver);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return assetCodeObservable.getCodeBeans();
    }

    @Override
    public void initRockInterface(RockInterface rockInterface) {
        mRockInterface = rockInterface;
    }

}

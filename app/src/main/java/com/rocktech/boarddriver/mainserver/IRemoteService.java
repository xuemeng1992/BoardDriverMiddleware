package com.rocktech.boarddriver.mainserver;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.TextUtils;

import com.fcbox.locker.driver.ICardReaderCallback;
import com.fcbox.locker.driver.IComparisonFingerprintCallback;
import com.fcbox.locker.driver.IEnterFingerprintCallback;
import com.fcbox.locker.driver.ILockerDriver;
import com.fcbox.locker.driver.IScannerCallback;
import com.google.gson.Gson;
import com.rairmmd.serialport.LibTools;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.bean.AssetCodeDao;
import com.rocktech.boarddriver.bean.BaseRes;
import com.rocktech.boarddriver.bean.CommonDataBean;
import com.rocktech.boarddriver.bean.FingerprintBean;
import com.rocktech.boarddriver.bean.FirmwareVBean;
import com.rocktech.boarddriver.bean.GetVersionBean;
import com.rocktech.boarddriver.bean.QueryAllForCourtRes;
import com.rocktech.boarddriver.bean.QueryRes;
import com.rocktech.boarddriver.bean.ReadLockerRes;
import com.rocktech.boarddriver.bean.ScannerResBean;
import com.rocktech.boarddriver.coremodule.cardreader.CardReader;
import com.rocktech.boarddriver.coremodule.fingerprint.DevComm;
import com.rocktech.boarddriver.coremodule.fingerprint.FingerprintTools;
import com.rocktech.boarddriver.coremodule.fingerprint.HostLib;
import com.rocktech.boarddriver.coremodule.fingerprint.IFPListener;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.LockerFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_QR;
import com.rocktech.boarddriver.coremodule.scanner.CourtScanner;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.DaoTools;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.tools.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IRemoteService extends Service {

    private ILocker locker;
    private CourtScanner scanner;
    private UsbManager musbManager = null;
    private final String ACTION_USB_PERMISSION = "com.rocktech.boarddriver.rfid.USB_PERMISSION";
    boolean m_bForce = true;
    private static final int VID = 1024;    //IDR VID
    private static final int PID = 50010;     //IDR PID
    private static Printer printer;

    @Override
    public void onCreate() {
        super.onCreate();
        locker = LockerFactory.getLocker(ConfigureTools.getLockerTypeBean(this).getId(), this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    ILockerDriver.Stub mBinder = new ILockerDriver.Stub() {
        @Override
        public synchronized String burnColId(int colIndex, String colId) throws RemoteException {
            LibTools.writeBehaviorLog("Get into burnColId==" + " colIndex:" + colIndex + " colId:" + colId);
            if (TextUtils.isEmpty(colId)) {
                return null;
            }
            List<AssetCodeBean> newList = new ArrayList<>();
            int lockerType = ConfigureTools.getLockerTypeBean(IRemoteService.this).getId();
            int newcolIndex = CustomizedFactory.getCustomized(lockerType).getAssetCodeDaoIndex(colIndex);
            AssetCodeBean codeBean = new AssetCodeBean();
            codeBean.setAssetCode(DaoTools.getAssetCodeDao(newcolIndex).getAssetCodeTitle().trim() + colId.trim());
            codeBean.setBoxId(DaoTools.getAssetCodeDao(newcolIndex).getBoxId());
            codeBean.setRealBoxId(DaoTools.getAssetCodeDao(newcolIndex).getBoxId());
            newList.add(codeBean);
            List<Boolean> booleanList = locker.writeCodeSyn(newList);
            BaseRes baseRes = new BaseRes();
            if (booleanList.get(0)) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x10F0);
            } else {
                baseRes.setError_msg("Burning failure");
                baseRes.setResult_code(0x10F2);
            }
            LibTools.writeBehaviorLog("burnColId:==" + " colIndex:" + colIndex + " colId:" + colId + "===" + new Gson().toJson(baseRes));
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String readLockerLayout(boolean refreshCache) throws RemoteException {
            LibTools.writeBehaviorLog("Get into readLockerLayout");
            int lockerType = ConfigureTools.getLockerTypeBean(IRemoteService.this).getId();
            List<AssetCodeBean> newAssetCodes;
            if (refreshCache) {
                List<AssetCodeBean> codeBeanList = locker.readCodesSyn();
                if (codeBeanList != null && codeBeanList.size() > 0) {
                    newAssetCodes = CustomizedFactory.getCustomized(lockerType).assetCodeSort(codeBeanList);
                    DaoTools.clearAssetCodeDao();
                    for (int i = 0; i < newAssetCodes.size(); i++) {
                        AssetCodeDao assetCodeDao = new AssetCodeDao();
                        int assetCodeLength = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).getAssetCodeLength();
                        assetCodeDao.setAssetCode(newAssetCodes.get(i).getAssetCode().substring(3, assetCodeLength));
                        assetCodeDao.setBoxId(newAssetCodes.get(i).getBoxId());
                        assetCodeDao.setAssetCodeTitle(newAssetCodes.get(i).getAssetCode().substring(0, 3));
                        DaoTools.addAssetCode(assetCodeDao);
                    }
                } else {
                    newAssetCodes = new ArrayList<>();
                }
            } else {
                newAssetCodes = new ArrayList<>();
                List<AssetCodeDao> dList = DaoTools.getAssetCodes();
                for (int i = 0; i < dList.size(); i++) {
                    AssetCodeBean bean = new AssetCodeBean();
                    bean.setAssetCode(dList.get(i).getAssetCodeTitle() + dList.get(i).getAssetCode());
                    bean.setBoxId(dList.get(i).getBoxId());
                    newAssetCodes.add(bean);
                }
            }
            List<AssetCodeBean> handleAfterCodes = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).handleAssetCode(newAssetCodes);
            ReadLockerRes res = new ReadLockerRes();
            if (handleAfterCodes.size() > 0) {
                res.setCol_count(handleAfterCodes.size());
                res.setError_msg("success");
                res.setResult_code(0x1060);
                List<ReadLockerRes.ColsInfoBean> list = new ArrayList<>();
                for (int i = 0; i < handleAfterCodes.size(); i++) {
                    ReadLockerRes.ColsInfoBean bean = new ReadLockerRes.ColsInfoBean();
                    int assetCodeLength = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).getAssetCodeLength();
                    bean.setCol_id(handleAfterCodes.get(i).getAssetCode().substring(3, assetCodeLength));
                    bean.setCol_index("" + (i + 1));
                    bean.setError_msg("success");
                    bean.setResult_code(0x1061);
                    list.add(bean);
                }
                res.setCols_info(list);
            } else {
                res.setCol_count(0);
                res.setError_msg("Burning failure");
                res.setResult_code(0x1063);
                res.setCols_info(new ArrayList<>());
            }
            LibTools.writeBehaviorLog("readLockerLayout: " + new Gson().toJson(res));
            return new Gson().toJson(res);
        }

        @Override
        public synchronized String openCell(String colId, int cellIndex) throws RemoteException {
            LibTools.writeBehaviorLog("Get into openCell：colId:" + colId + " cellIndex:" + cellIndex);
            List<AssetCodeDao> dList = DaoTools.getAssetCodes();
            BaseRes baseRes = new BaseRes();
            if (TextUtils.isEmpty(colId)) {
                baseRes.setResult_code(0x1001);
                baseRes.setError_msg("parameter violation");
                return new Gson().toJson(baseRes);
            }
            if (dList.size() == 0) {
                baseRes.setResult_code(0x1004);
                baseRes.setError_msg("The layout has not been initialized");
                return new Gson().toJson(baseRes);
            }
            AssetCodeDao assetCodeDao = DaoTools.getAssetCodeDao(colId);
            String boxId = assetCodeDao.getBoxId().substring(0, 1);
            if (TextUtils.isEmpty(boxId)) {
                baseRes.setResult_code(0x1001);
                baseRes.setError_msg("not found 'colId'");
                return new Gson().toJson(baseRes);
            }
            boolean isOpen;
            if (TextUtils.equals("Z", boxId)) {
                if (cellIndex == 1) {
                    isOpen = locker.openLockSyn("Z00", -1, assetCodeDao);
                } else {
                    isOpen = locker.openLockSyn(boxId, cellIndex - 1, assetCodeDao);
                }
            } else {
                isOpen = locker.openLockSyn(boxId, cellIndex, assetCodeDao);
            }
            LoggerUtils.Log().e("openCell：isOpen:" + isOpen);
            if (isOpen) {
                baseRes.setResult_code(0x1000);
                baseRes.setError_msg("success");
            } else {
                baseRes.setResult_code(0x1002);
                baseRes.setError_msg("error");
            }
            LibTools.writeBehaviorLog("openCell：colId:" + colId + " cellIndex:" + cellIndex + "====" + new Gson().toJson(baseRes));
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String openAllCells(String colId) throws RemoteException {
            LibTools.writeBehaviorLog("Get into openAllCells：colId:" + colId);
            List<AssetCodeDao> dList = DaoTools.getAssetCodes();
            BaseRes baseRes = new BaseRes();
            if (dList.size() == 0) {
                baseRes.setResult_code(0x1074);
                baseRes.setError_msg("The layout has not been initialized");
                return new Gson().toJson(baseRes);
            }
            AssetCodeDao assetCodeDao = DaoTools.getAssetCodeDao(colId);
            String boxId = assetCodeDao.getBoxId().substring(0, 1);
            if (TextUtils.isEmpty(boxId)) {
                baseRes.setResult_code(0x1071);
                baseRes.setError_msg("not found 'colId'");
                return new Gson().toJson(baseRes);
            }
            int lockerType = ConfigureTools.getLockerTypeBean(IRemoteService.this).getId();
            CommonDataBean bean = locker.openLocksSyn(CustomizedFactory.getCustomized(lockerType).getAllOpenLock(assetCodeDao.getAssetCode(), boxId));
            List<Boolean> result = (List<Boolean>) bean.getO1();
            for (int i = 0; i < result.size(); i++) {
                if (!result.get(i)) {
                    baseRes.setResult_code(0x1072);
                    baseRes.setError_msg("One cell is not open");
                    return new Gson().toJson(baseRes);
                }
            }
            baseRes.setResult_code(0x1070);
            baseRes.setError_msg("success");
            LibTools.writeBehaviorLog("openAllCells：colId:" + colId + "====" + new Gson().toJson(baseRes));
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String readCellInfo(String colId, int cellIndex) throws RemoteException {
            LibTools.writeBehaviorLog("Get into readCellInfo：colId:" + colId + " cellIndex:" + cellIndex);
            List<AssetCodeDao> dList = DaoTools.getAssetCodes();
            QueryRes queryRes = new QueryRes();
            if (TextUtils.isEmpty(colId) || cellIndex < 1 || cellIndex > 32) {
                queryRes.setResult_code(0x1011);
                queryRes.setError_msg("parameter violation");
                return new Gson().toJson(queryRes);
            }
            if (dList.size() == 0) {
                queryRes.setResult_code(0x1014);
                queryRes.setError_msg("The layout has not been initialized");
                return new Gson().toJson(queryRes);
            }
            AssetCodeDao assetCodeDao = DaoTools.getAssetCodeDao(colId);
            String boxId = assetCodeDao.getBoxId().substring(0, 1);
            if (TextUtils.isEmpty(boxId)) {
                queryRes.setResult_code(0x1011);
                queryRes.setError_msg("not found 'colId'");
                return new Gson().toJson(queryRes);
            }
            boolean isOpen;
            if (TextUtils.equals("Z", boxId)) {
                if (cellIndex == 1) {
                    isOpen = locker.queryLockSyn("Z00", -1, assetCodeDao);
                } else {
                    isOpen = locker.queryLockSyn(boxId, cellIndex - 1, assetCodeDao);
                }
            } else {
                isOpen = locker.queryLockSyn(boxId, cellIndex, assetCodeDao);
            }
            if (isOpen) {
                queryRes.setLock_status(0x0002);
            } else {
                queryRes.setLock_status(0x0001);
            }
            queryRes.setResult_code(0x1010);
            queryRes.setError_msg("success");
            LibTools.writeBehaviorLog("return readCellInfo：colId:" + colId + " cellIndex:" + cellIndex + "====" + new Gson().toJson(queryRes));
            return new Gson().toJson(queryRes);
        }

        @Override
        public synchronized String readAllCellsInfo(String colId) throws RemoteException {
            LibTools.writeBehaviorLog("Get into readAllCellsInfo：colId:" + colId);
            List<AssetCodeDao> dList = DaoTools.getAssetCodes();
            QueryAllForCourtRes queryRes = new QueryAllForCourtRes();
            if (TextUtils.isEmpty(colId)) {
                queryRes.setResult_code(0x1091);
                queryRes.setError_msg("parameter violation");
                return new Gson().toJson(queryRes);
            }
            if (dList.size() == 0) {
                queryRes.setResult_code(0x1094);
                queryRes.setError_msg("The layout has not been initialized");
                return new Gson().toJson(queryRes);
            }
            AssetCodeDao assetCodeDao = DaoTools.getAssetCodeDao(colId);
            String boxId = assetCodeDao.getBoxId().substring(0, 1);
            if (TextUtils.isEmpty(boxId)) {
                queryRes.setResult_code(0x1091);
                queryRes.setError_msg("not found 'colId'");
                return new Gson().toJson(queryRes);
            }
            List<Boolean> isOpens = locker.queryAllLockSyn(boxId, colId);
            queryRes.setResult_code(0x1090);
            queryRes.setError_msg("success");
            if (isOpens != null && isOpens.size() > 0) {
                List<QueryAllForCourtRes.CellsInfoBean> cellsInfoBeanList = new ArrayList<>();
                for (int i = 0; i < isOpens.size(); i++) {
                    QueryAllForCourtRes.CellsInfoBean cellsInfoBean = new QueryAllForCourtRes.CellsInfoBean();
                    if (boxId.startsWith("Z")) {
                        cellsInfoBean.setCell_index(i + 2);
                    } else {
                        cellsInfoBean.setCell_index(i + 1);
                    }
                    cellsInfoBean.setLock_status(isOpens.get(i) ? 0x0002 : 0x0001);
                    cellsInfoBean.setResult_code(0x1010);
                    cellsInfoBean.setError_msg("success");
                    cellsInfoBean.setGoods_status(0x0001);
                    cellsInfoBeanList.add(cellsInfoBean);
                }
                queryRes.setCells_info(cellsInfoBeanList);
                queryRes.setLock_count(isOpens.size());
                LibTools.writeBehaviorLog("readAllCellsInfo：colId:" + colId + "====" + new Gson().toJson(queryRes));
                return new Gson().toJson(queryRes);
            } else {
                queryRes.setResult_code(0x1092);
                queryRes.setError_msg("Error.time out");
                return new Gson().toJson(queryRes);
            }
        }

        @Override
        public synchronized String readLockBoardFirmwareVersion(String colId) throws RemoteException {
            LibTools.writeBehaviorLog("Get into readLockBoardFirmwareVersion：colId:" + colId);
            List<AssetCodeDao> dList = DaoTools.getAssetCodes();
            FirmwareVBean queryRes = new FirmwareVBean();
            if (TextUtils.isEmpty(colId)) {
                queryRes.setResult_code(0x1161);
                queryRes.setError_msg("parameter violation");
                return new Gson().toJson(queryRes);
            }
            if (dList.size() == 0) {
                queryRes.setResult_code(0x1164);
                queryRes.setError_msg("The layout has not been initialized");
                return new Gson().toJson(queryRes);
            }
            AssetCodeDao assetCodeDao = DaoTools.getAssetCodeDao(colId);
            String boxId = assetCodeDao.getBoxId().substring(0, 1);
            if (TextUtils.isEmpty(boxId)) {
                queryRes.setResult_code(0x1161);
                queryRes.setError_msg("not found 'colId'");
                return new Gson().toJson(queryRes);
            }
            ArrayList<String> cbvs = locker.getChipVersionSyn(new String[]{boxId});
            if (cbvs.size() > 0) {
                queryRes.setResult_code(0x1160);
                queryRes.setError_msg("success");
                queryRes.setFirmware_version(cbvs.get(0));
                return new Gson().toJson(queryRes);
            }
            LibTools.writeBehaviorLog("readLockBoardFirmwareVersion：colId:" + colId + "====" + new Gson().toJson(queryRes));
            return new Gson().toJson(queryRes);
        }

        @Override
        public void initScanner(int duration) throws RemoteException {
            try {
                if (scanner == null) {
                    scanner = new CourtScanner(duration, IRemoteService.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String readScannerInfo() throws RemoteException {
            ScannerResBean scannerResBean = new ScannerResBean();
            if (scanner != null) {
                scannerResBean.setResult_code(0x1150);
                scannerResBean.setError_msg("success");
                scannerResBean.setOnline(true);
            } else {
                scannerResBean.setResult_code(0x1150);
                scannerResBean.setError_msg("error");
                scannerResBean.setOnline(false);
            }
            return new Gson().toJson(scannerResBean);
        }

        @Override
        public String turnOnScanner(IScannerCallback callback) throws RemoteException {
            if (scanner == null) {
                try {
                    scanner = new CourtScanner(3, IRemoteService.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            scanner.startScanner(callback);
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1020);
            return new Gson().toJson(baseRes);
        }

        @Override
        public String turnOffScanner() throws RemoteException {
            if (scanner == null) {
                try {
                    scanner = new CourtScanner(3, IRemoteService.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            scanner.cancelScanner();
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1030);
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String controlLamp(int majorLampCmd, int minorLampCmd) throws RemoteException {
            LibTools.writeBehaviorLog("Get into controlLamp");
            if (3 == majorLampCmd) {
                locker.openZlamp();
            } else if (2 == majorLampCmd) {
                locker.closeZlamp();
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            if (1 == minorLampCmd) {
                locker.controlLamp(true);
            } else if (0 == minorLampCmd) {
                locker.controlLamp(false);
            }
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1100);
            LibTools.writeBehaviorLog("controlLamp：majorLampCmd:" + majorLampCmd + "minorLampCmd: " + minorLampCmd + "====" + new Gson().toJson(baseRes));
            return new Gson().toJson(baseRes);
        }

        @Override
        public void installApk(String apkPath, boolean silent, String pkgName, String clsName) throws RemoteException {
            if (!TextUtils.isEmpty(apkPath)) {
                File apkFile = new File(apkPath);
                if (apkFile.exists()) {
                    IRemoteService.this.sendBroadcast(new Intent("android.intent.action.ACTION_SILENCE_INSTALL")
                            .putExtra("com.fcbox.locker.extra.APK_PATH", apkPath)
                            .putExtra("com.fcbox.locker.extra.PKG_NAME", pkgName)
                            .putExtra("com.fcbox.locker.extra.CLS_NAME", clsName)
                            .putExtra("com.fcbox.locker.extra.SILENT", false));
                }
            }
        }


        @Override
        public String turnOnCardReader(int cardType, int duration, ICardReaderCallback callback) throws RemoteException {
            BaseRes baseRes = new BaseRes();
            if (cardType == 0 && duration > 1 && callback != null) {
                try {
                    if (!CardReader.getInstance().bopen) {
                        CardReader.getInstance().openCardReader(IRemoteService.this);
                    }
                    RequestDevicePermission();
                    CardReader.getInstance().setCallback(callback, duration);
                    CardReader.getInstance().OnBnBegin(IRemoteService.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                baseRes.setResult_code(0x1300);
                baseRes.setError_msg("success");
            } else {
                baseRes.setResult_code(0x1302);
                baseRes.setError_msg("parameter violation.cardType:" + cardType + ",duration:"
                        + duration + ",callback is null" + (callback == null));
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String turnOffCardReader() throws RemoteException {
            CardReader.getInstance().OnBnStop();
            CardReader.getInstance().distory();
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1310);
            return new Gson().toJson(baseRes);
        }

        @Override
        public String controlRelay(int device, int cmd, int rebootInterval) throws RemoteException {
            if (device == 0x0003) {
                if (cmd == 0x0002) {
                    ((PowerManager) IRemoteService.this.getSystemService(Context.POWER_SERVICE)).reboot("");
                }
            }
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1040);
            return new Gson().toJson(baseRes);
        }

        @Override
        public String initLabelPrinter() throws RemoteException {
            BaseRes baseRes = new BaseRes();
            printer = ConfigureTools.creatPrinter(ConfigureTools.getPrinter(IRemoteService.this), IRemoteService.this, printer);
            if (printer != null) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x1200);
            } else {
                baseRes.setError_msg("error");
                baseRes.setResult_code(0x1201);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String destroyLabelPrinter() throws RemoteException {
            if (printer != null) {
                printer.closeSerialPort();
            }
            printer = null;
            BaseRes baseRes = new BaseRes();
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x1210);
            return new Gson().toJson(baseRes);
        }

        @Override
        public String printLabelDirectly(String cmds, int copies) throws RemoteException {
            BaseRes baseRes = new BaseRes();
            boolean printOk = false;
            if (printer != null) {
                int i = 0;
                while (i < copies) {
                    if (ConfigureTools.getPrinter(IRemoteService.this).equals(Constant.PrinterTYPE.QR)) {
                        printOk = printer.printSyn(cmds, Printer_QR.FYG_CODE);
                    } else {
                        printOk = printer.printSyn(cmds, 0);
                    }
                    i++;
                }
                if (printOk) {
                    baseRes.setError_msg("success");
                    baseRes.setResult_code(0x12E0);
                } else {
                    baseRes.setError_msg("Fail");
                    baseRes.setResult_code(0x12E2);
                }
            } else {
                baseRes.setError_msg("Fail");
                baseRes.setResult_code(0x12E1);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String readLabelPrinterInfo() throws RemoteException {
            GetVersionBean baseRes = new GetVersionBean();
            if (printer != null) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x12C0);
                baseRes.setVersion(printer.getVersion());
            } else {
                baseRes.setError_msg("error");
                baseRes.setResult_code(0x12C1);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String readLabelPrinterPaperInfo() throws RemoteException {
            BaseRes baseRes = new BaseRes();
            if (printer != null) {
                if (printer.hasPaperSyn()) {
                    baseRes.setError_msg("success");
                    baseRes.setResult_code(0x12D0);
                } else {
                    baseRes.setError_msg("error");
                    baseRes.setResult_code(0x12D2);
                }
            } else {
                baseRes.setError_msg("error");
                baseRes.setResult_code(0x12D1);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String setLabelPrinterArea(int width, int height) throws RemoteException {
            return null;
        }

        @Override
        public String drawTextToLabel(int x, int y, int language, String text, int rotationAngle, int fontSizeGroup, int fontSize, int fontStyle) throws RemoteException {
            return null;
        }

        @Override
        public String drawBarcodeToLabel(int x, int y, String text, int height, int multiple) throws RemoteException {
            return null;
        }

        @Override
        public String drawQrCodeToLabel(int x, int y, String text, int multiple) throws RemoteException {
            return null;
        }

        @Override
        public String drawRectangleToLabel(int x, int y, int width, int height, int thickness) throws RemoteException {
            return null;
        }

        @Override
        public String drawLineToLabel(int startX, int startY, int endX, int endY, int thickness) throws RemoteException {
            return null;
        }

        @Override
        public String downloadBmpToLabelFlash(int bmpIndex, String bmpPath) throws RemoteException {

            return null;
        }

        @Override
        public String drawBmpToLabel(int x, int y, int bmpIndex) throws RemoteException {
            return null;
        }

        @Override
        public String cleanBmpFromLabelFlash(int bmpIndex) throws RemoteException {
            return null;
        }

        @Override
        public String printLabel(int copies) throws RemoteException {
            return null;
        }

        @Override
        public synchronized String initFingerprintReader(List<String> ids, List<String> fingerprintInfos) throws RemoteException {
            LoggerUtils.Log().e("initFingerprintReader>>>>>>" + ids.toString() + "||||||" + fingerprintInfos.toString());
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            BaseRes baseRes = new BaseRes();
            int res = HostLib.getInstance(IRemoteService.this).FPCmdProc().OpenDevice("", 0);
            if (res == 0) {
                HostLib.getInstance(IRemoteService.this).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {
                    @Override
                    public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2) {
                        if (p_nCmdCode == (short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE) {
                            if (p_nRetCode != (short) DevComm.ERR_SUCCESS) {
                                baseRes.setError_msg("failure");
                                baseRes.setResult_code(0x1332);
                                countDownLatch.countDown();
                            }
                        }
                    }

                    @Override
                    public void cmdProcReturnData(byte[] p_pData, int p_nSize) {

                    }

                    @Override
                    public void cmdProcShowText(String p_szInfo) {

                    }

                    @Override
                    public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1) {
                        if (p_nCode == (short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE) {
                            if (m_bCmdDone) {
                                if (ids.size() > 0) {
                                    if (TextUtils.isEmpty(ids.get(0))) {
                                        ids.remove(0);
                                        fingerprintInfos.remove(0);
                                        if (ids.size() == 0) {
                                            baseRes.setError_msg("success");
                                            baseRes.setResult_code(0x1330);
                                            countDownLatch.countDown();
                                            return;
                                        }
                                    }
                                    List<FingerprintBean> fingerprintBeanList = new ArrayList<>();
                                    for (int i = 0; i < ids.size(); i++) {
                                        fingerprintBeanList.add(new FingerprintBean(ids.get(i), fingerprintInfos.get(i)));
                                    }
                                    List<FingerprintBean> newFingerprintList = Tools.rmRepeatFingerprint(fingerprintBeanList);
                                    LoggerUtils.Log().e("initFingerprintReader>>>>>>newFingerprintList:" + newFingerprintList.toString());
                                    LoggerUtils.Log().e(newFingerprintList.toString());
                                    int j = 0;
                                    for (int i = 0; i < newFingerprintList.size(); i++) {
                                        j = HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdWriteTemplate(
                                                Integer.parseInt(Tools.analysisId(newFingerprintList.get(i).getId())),
                                                Tools.hexToByteArray(newFingerprintList.get(i).getFingerprintInfo()),
                                                Tools.hexToByteArray(newFingerprintList.get(i).getFingerprintInfo()).length,
                                                true);
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (j == 0) {
                                        baseRes.setError_msg("success");
                                        baseRes.setResult_code(0x1330);
                                    } else {
                                        baseRes.setError_msg("failure");
                                        baseRes.setResult_code(0x1332);
                                    }
                                    countDownLatch.countDown();
                                } else {
                                    baseRes.setError_msg("success");
                                    baseRes.setResult_code(0x1330);
                                    countDownLatch.countDown();
                                }
                            }
                        }
                    }
                }, new IFPListener.FPCancelListener() {
                    @Override
                    public void cancelReturn(int p_nRetCode) {

                    }
                }, new IFPListener.FPUSBPermissionListener() {
                    @Override
                    public void usbPermissionAllowed() {

                    }

                    @Override
                    public void usbPermissionDenied() {

                    }
                });
                //删除本地存储的指纹信息，待回调结果放回成功后，倒入数据
                HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdDeleteAll(m_bForce);
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                baseRes.setError_msg("Open failure");
                baseRes.setResult_code(0x1332);
            }
            LoggerUtils.Log().e("initFingerprintReader>>>>>>" + new Gson().toJson(baseRes));
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String destroyFingerprintReader() throws RemoteException {
            LoggerUtils.Log().e("destroyFingerprintReader");
            BaseRes baseRes = new BaseRes();
            if (!HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x1340);
                return new Gson().toJson(baseRes);
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            HostLib.getInstance(IRemoteService.this).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {
                @Override
                public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2) {
                    if (p_nCmdCode == (short) DevComm.CMD_CLEAR_ALLTEMPLATE_CODE) {
                        if (p_nRetCode == (short) DevComm.ERR_SUCCESS) {
                            HostLib.getInstance(IRemoteService.this).FPCmdProc().CloseDevice();
                            baseRes.setError_msg("success");
                            baseRes.setResult_code(0x1340);
                        } else {
                            baseRes.setError_msg("Delete failure");
                            baseRes.setResult_code(0x1341);
                        }
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void cmdProcReturnData(byte[] p_pData, int p_nSize) {

                }

                @Override
                public void cmdProcShowText(String p_szInfo) {

                }

                @Override
                public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1) {

                }
            }, new IFPListener.FPCancelListener() {
                @Override
                public void cancelReturn(int p_nRetCode) {

                }
            }, new IFPListener.FPUSBPermissionListener() {
                @Override
                public void usbPermissionAllowed() {

                }

                @Override
                public void usbPermissionDenied() {

                }
            });
            HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdDeleteAll(m_bForce);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String enterFingerprint(String id, IEnterFingerprintCallback callback) throws RemoteException {
            LoggerUtils.Log().e("enterFingerprint  id:" + id);
            BaseRes baseRes = new BaseRes();
            if (!HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
                baseRes.setError_msg("failure");
                baseRes.setResult_code(0x1352);
                return new Gson().toJson(baseRes);
            }
            if (TextUtils.isEmpty(id)) {
                baseRes.setError_msg("failure");
                baseRes.setResult_code(0x1351);
                return new Gson().toJson(baseRes);
            }
            HostLib.getInstance(IRemoteService.this).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {

                @Override
                public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2) {
                    if (p_nCmdCode == (short) DevComm.CMD_ENROLL_CODE) {
                        if (p_nRetCode == (short) DevComm.ERR_SUCCESS) {
                            FingerprintTools.GetSuccessMsg(IRemoteService.this, p_nParam1, callback);
                        } else {
                            FingerprintTools.GetErrorMsg((short) p_nParam1, callback);
                        }
                    }
                }

                @Override
                public void cmdProcReturnData(byte[] p_pData, int p_nSize) {

                }

                @Override
                public void cmdProcShowText(String p_szInfo) {

                }

                @Override
                public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1) {

                }
            }, new IFPListener.FPCancelListener() {
                @Override
                public void cancelReturn(int p_nRetCode) {

                }
            }, new IFPListener.FPUSBPermissionListener() {
                @Override
                public void usbPermissionAllowed() {

                }

                @Override
                public void usbPermissionDenied() {

                }
            });
            if (HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdEnroll(Integer.parseInt(Tools.analysisId(id)), m_bForce) == 0) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x1350);
                return new Gson().toJson(baseRes);
            }
            baseRes.setError_msg("failure");
            baseRes.setResult_code(0x1352);
            return new Gson().toJson(baseRes);
        }


        @Override
        public synchronized String comparisonFingerprint(IComparisonFingerprintCallback callback) throws RemoteException {
            LoggerUtils.Log().e("comparisonFingerprint");
            BaseRes baseRes = new BaseRes();
            if (!HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
                baseRes.setError_msg("failure");
                baseRes.setResult_code(0x1373);
                return new Gson().toJson(baseRes);
            }
            HostLib.getInstance(IRemoteService.this).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {
                @Override
                public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2) {
                    if (p_nCmdCode == (short) DevComm.CMD_IDENTIFY_CODE) {
                        if (p_nRetCode == (short) DevComm.ERR_SUCCESS) {
                            FingerprintTools.GetSuccessMsg(p_nParam1, callback);
                        } else {
                            FingerprintTools.GetErrorMsg2((short) p_nParam1, callback);
                        }
                    }
                }

                @Override
                public void cmdProcReturnData(byte[] p_pData, int p_nSize) {

                }

                @Override
                public void cmdProcShowText(String p_szInfo) {

                }

                @Override
                public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1) {

                }
            }, new IFPListener.FPCancelListener() {
                @Override
                public void cancelReturn(int p_nRetCode) {

                }
            }, new IFPListener.FPUSBPermissionListener() {
                @Override
                public void usbPermissionAllowed() {

                }

                @Override
                public void usbPermissionDenied() {

                }
            });
            if (HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdIdentify(m_bForce) == 0) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x1370);
                return new Gson().toJson(baseRes);
            } else {
                baseRes.setError_msg("failure");
                baseRes.setResult_code(0x1373);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String deleteFingerprint(String id) throws RemoteException {
            LoggerUtils.Log().e("deleteFingerprint");
            BaseRes baseRes = new BaseRes();
            if (!HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
                baseRes.setError_msg("failure");
                baseRes.setResult_code(0x1392);
                return new Gson().toJson(baseRes);
            }
            if (TextUtils.isEmpty(id)) {
                baseRes.setError_msg("0x1391");
                baseRes.setResult_code(0x1351);
                return new Gson().toJson(baseRes);
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            HostLib.getInstance(IRemoteService.this).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {
                @Override
                public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2) {
                    if (p_nCmdCode == (short) DevComm.CMD_CLEAR_TEMPLATE_CODE) {
                        if (p_nRetCode == (short) DevComm.ERR_SUCCESS) {
                            baseRes.setError_msg("success");
                            baseRes.setResult_code(0x1390);
                        } else {
                            baseRes.setError_msg("failure");
                            baseRes.setResult_code(0x1392);
                        }
                        countDownLatch.countDown();
                    }
                }

                @Override
                public void cmdProcReturnData(byte[] p_pData, int p_nSize) {

                }

                @Override
                public void cmdProcShowText(String p_szInfo) {

                }

                @Override
                public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1) {

                }
            }, new IFPListener.FPCancelListener() {
                @Override
                public void cancelReturn(int p_nRetCode) {

                }
            }, new IFPListener.FPUSBPermissionListener() {
                @Override
                public void usbPermissionAllowed() {

                }

                @Override
                public void usbPermissionDenied() {

                }
            });
            HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdDeleteID(Integer.parseInt(Tools.analysisId(id)), m_bForce);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public synchronized String stopFingerprint() throws RemoteException {
            LoggerUtils.Log().e("stopFingerprint");
            BaseRes baseRes = new BaseRes();
            if (HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
                HostLib.getInstance(IRemoteService.this).FPCmdProc().Run_CmdCancel();
            }
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            baseRes.setError_msg("success");
            baseRes.setResult_code(0x13A0);
            return new Gson().toJson(baseRes);
        }

        @Override
        public String controlFillLight(int cmd) throws RemoteException {
            return null;
        }

        @Override
        public String readAllChannelsInfo(String msgId, String colId, boolean refreshCache) throws RemoteException {
            return null;
        }

        @Override
        public String channelsShipping(String msgId, String colId, String ordersJson) throws RemoteException {
            return null;
        }

        @Override
        public String controlChannelsPilotLamp(String msgId, String colId, String channelsPilotLampJson) throws RemoteException {
            return null;
        }

        @Override
        public String readChannelCabinetInfo(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String controlDrawersPilotLamp(String msgId, String colId, String drawersPilotLampJson) throws RemoteException {
            return null;
        }

        @Override
        public String openChannelCabinetDoor(String msgId, String colId, String doorType) throws RemoteException {
            return null;
        }

        @Override
        public String controlLamplight(String msgId, String colId, String lampType, String lampColor, String action) throws RemoteException {
            return null;
        }

        @Override
        public String readPcbFirmwareVersion(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String setPcbTime(String msgId, String colId, String time) throws RemoteException {
            return null;
        }

        @Override
        public String eventCollection(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String setEventTimeConfigs(String msgId, String colId, String timeConfigsJson) throws RemoteException {
            return null;
        }

        @Override
        public String readEventTimeConfigs(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String maintenance(String msgId, String colId, boolean on) throws RemoteException {
            return null;
        }

        @Override
        public String readUpsInfo(String msgId) throws RemoteException {
            return null;
        }

        @Override
        public String shutdownAndRestoreUps(String msgId, String shutdownTime, String restoreTime) throws RemoteException {
            return null;
        }

        @Override
        public String readDrawerCabinetInfo(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String rebootChannelCabinetPcb(String msgId, String colId) throws RemoteException {
            return null;
        }

        @Override
        public String burn(String msgId, int colIndex, String colType, String colId) throws RemoteException {
            LoggerUtils.Log().e("burn:" + "msgId:" + msgId + ";colIndex:" + colIndex + ";colType:" + colType + ";colId:" + colId);
            if (TextUtils.isEmpty(colId)) {
                return null;
            }
            int lockerType = ConfigureTools.getLockerTypeBean(IRemoteService.this).getId();
            int newcolIndex = CustomizedFactory.getCustomized(lockerType).getAssetCodeDaoIndex(colIndex);
            List<AssetCodeBean> newList = new ArrayList<>();
            AssetCodeBean codeBean = new AssetCodeBean();
            codeBean.setAssetCode(DaoTools.getAssetCodeDao(newcolIndex).getAssetCodeTitle().trim() + colId.trim());
            codeBean.setBoxId(DaoTools.getAssetCodeDao(newcolIndex).getBoxId());
            codeBean.setRealBoxId(DaoTools.getAssetCodeDao(newcolIndex).getBoxId());
            codeBean.setProductModel("&" + colType + "&");
            newList.add(codeBean);
            List<Boolean> booleanList = locker.writeCodeSyn(newList);
            BaseRes baseRes = new BaseRes();
            if (booleanList.get(0)) {
                baseRes.setError_msg("success");
                baseRes.setResult_code(0x14D0);
            } else {
                baseRes.setError_msg("Burning failure");
                baseRes.setResult_code(0x14D2);
            }
            return new Gson().toJson(baseRes);
        }

        @Override
        public String readLayout(String msgId, boolean refreshCache) throws RemoteException {
            LoggerUtils.Log().e("readLayout");
            int lockerType = ConfigureTools.getLockerTypeBean(IRemoteService.this).getId();
            List<AssetCodeBean> newAssetCodes;
            if (refreshCache) {
                List<AssetCodeBean> codeBeanList = locker.readCodesSyn();
                if (codeBeanList != null && codeBeanList.size() > 0) {
                    newAssetCodes = CustomizedFactory.getCustomized(lockerType).assetCodeSort(codeBeanList);
                    DaoTools.clearAssetCodeDao();
                    for (int i = 0; i < newAssetCodes.size(); i++) {
                        AssetCodeDao assetCodeDao = new AssetCodeDao();
                        int assetCodeLength = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).getAssetCodeLength();
                        assetCodeDao.setAssetCode(newAssetCodes.get(i).getAssetCode().substring(3, assetCodeLength));
                        assetCodeDao.setBoxId(newAssetCodes.get(i).getBoxId());
                        assetCodeDao.setAssetCodeTitle(newAssetCodes.get(i).getAssetCode().substring(0, 3));
                        assetCodeDao.setProductModel(newAssetCodes.get(i).getProductModel());
                        DaoTools.addAssetCode(assetCodeDao);
                    }
                } else {
                    newAssetCodes = new ArrayList<>();
                }
            } else {
                newAssetCodes = new ArrayList<>();
                List<AssetCodeDao> dList = DaoTools.getAssetCodes();
                for (int i = 0; i < dList.size(); i++) {
                    AssetCodeBean bean = new AssetCodeBean();
                    bean.setAssetCode(dList.get(i).getAssetCodeTitle() + dList.get(i).getAssetCode());
                    bean.setBoxId(dList.get(i).getBoxId());
                    bean.setProductModel(dList.get(i).getProductModel());
                    newAssetCodes.add(bean);
                }
            }
            List<AssetCodeBean> handleAfterCodes = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).handleAssetCode(newAssetCodes);
            ReadLockerRes res = new ReadLockerRes();
            if (handleAfterCodes.size() > 0) {
                res.setCol_count(handleAfterCodes.size());
                res.setError_msg("success");
                res.setResult_code(0x14E0);
                List<ReadLockerRes.ColsInfoBean> list = new ArrayList<>();
                for (int i = 0; i < handleAfterCodes.size(); i++) {
                    ReadLockerRes.ColsInfoBean bean = new ReadLockerRes.ColsInfoBean();
                    int assetCodeLength = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(IRemoteService.this).getId()).getAssetCodeLength();
                    bean.setCol_id(handleAfterCodes.get(i).getAssetCode().substring(3, assetCodeLength));
                    bean.setCol_index("" + (i + 1));
                    bean.setError_msg("success");
                    bean.setCol_type(handleAfterCodes.get(i).getProductModel());
                    bean.setResult_code(0x14F0);
                    list.add(bean);
                }
                res.setCols_info(list);
            } else {
                res.setCol_count(0);
                res.setError_msg("Burning failure");
                res.setResult_code(0x1062);
                res.setCols_info(new ArrayList<>());
            }
            return new Gson().toJson(res);
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        if (HostLib.getInstance(IRemoteService.this).FPCmdProc().IsInit()) {
            HostLib.getInstance(IRemoteService.this).FPCmdProc().CloseDevice();
        }
        return super.onUnbind(intent);
    }

    private void RequestDevicePermission() {
        musbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        for (UsbDevice device : musbManager.getDeviceList().values()) {
            if (device.getVendorId() == VID && device.getProductId() == PID) {
                Intent intent = new Intent(ACTION_USB_PERMISSION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                musbManager.requestPermission(device, pendingIntent);
            }
        }
    }


}

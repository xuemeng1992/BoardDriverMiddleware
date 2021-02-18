// ILockerDriver.aidl
package com.fcbox.locker.driver;

// Declare any non-default types here with import statements

import com.fcbox.locker.driver.IScannerCallback;
import com.fcbox.locker.driver.ICardReaderCallback;
import com.fcbox.locker.driver.IEnterFingerprintCallback;
import com.fcbox.locker.driver.IComparisonFingerprintCallback;

interface ILockerDriver {

    String burnColId(int colIndex, String colId);

    String readLockerLayout(boolean refreshCache);

    String openCell(String colId, int cellIndex);

    String openAllCells(String colId);

    String readCellInfo(String colId, int cellIndex);

    String readAllCellsInfo(String colId);

    String readLockBoardFirmwareVersion(String colId);

	void initScanner(int duration);

	String readScannerInfo();

	String turnOnScanner(IScannerCallback callback);

	String turnOffScanner();

	String controlLamp(int majorLampCmd, int minorLampCmd);

	void installApk(String apkPath, boolean silent, String pkgName, String clsName);

	String initLabelPrinter();

	String destroyLabelPrinter();

	String setLabelPrinterArea(int width, int height);

	String drawTextToLabel(int x, int y, int language, String text, int rotationAngle, int fontSizeGroup, int fontSize, int fontStyle);

	String drawBarcodeToLabel(int x, int y, String text, int height, int multiple);

	String drawQrCodeToLabel(int x, int y, String text, int multiple);

	String drawRectangleToLabel(int x, int y, int width, int height, int thickness);

	String drawLineToLabel(int startX, int startY, int endX, int endY, int thickness);

	String downloadBmpToLabelFlash(int bmpIndex, String bmpPath);

	String drawBmpToLabel(int x, int y, int bmpIndex);

	String cleanBmpFromLabelFlash(int bmpIndex);

	String printLabel(int copies);

	String readLabelPrinterInfo();

	String readLabelPrinterPaperInfo();

	String turnOnCardReader(int cardType, int duration, ICardReaderCallback callback);

	String turnOffCardReader();

	String controlRelay(int device, int cmd, int rebootInterval);

	String initFingerprintReader(in List<String> ids, in List<String> fingerprintInfos);

	String destroyFingerprintReader();

	String enterFingerprint(String id, IEnterFingerprintCallback callback);

    String comparisonFingerprint(IComparisonFingerprintCallback callback);

    String deleteFingerprint(String id);

    String stopFingerprint();

    String controlFillLight(int cmd);

	String printLabelDirectly(String cmds, int copies);

    String readAllChannelsInfo(String msgId, String colId, boolean refreshCache);

	String channelsShipping(String msgId, String colId, String ordersJson);

	String controlChannelsPilotLamp(String msgId, String colId, String channelsPilotLampJson);

	String readChannelCabinetInfo(String msgId, String colId);

	String controlDrawersPilotLamp(String msgId, String colId, String drawersPilotLampJson);

	String openChannelCabinetDoor(String msgId, String colId, String doorType);

	String controlLamplight(String msgId, String colId, String lampType, String lampColor, String action);

	String readPcbFirmwareVersion(String msgId, String colId);

	String setPcbTime(String msgId, String colId, String time);

	String eventCollection(String msgId, String colId);

	String setEventTimeConfigs(String msgId, String colId, String timeConfigsJson);

	String readEventTimeConfigs(String msgId, String colId);

	String maintenance(String msgId, String colId, boolean on);

	String readUpsInfo(String msgId);

	String shutdownAndRestoreUps(String msgId, String shutdownTime, String restoreTime);

	String readDrawerCabinetInfo(String msgId, String colId);

	String rebootChannelCabinetPcb(String msgId, String colId);

	String burn(String msgId, int colIndex, String colType, String colId);

	String readLayout(String msgId, boolean refreshCache);

}

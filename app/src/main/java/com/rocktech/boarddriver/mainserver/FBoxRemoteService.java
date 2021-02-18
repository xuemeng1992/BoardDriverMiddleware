package com.rocktech.boarddriver.mainserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.remote.aidl.IRemoteAIDL;
import com.rocktech.boarddriver.tools.Action;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;

public class FBoxRemoteService extends Service {

    public FBoxRemoteService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    IRemoteAIDL.Stub mBinder = new IRemoteAIDL.Stub() {
        @Override
        public void onoffMainLamp(int onoff) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.LAMP_MAIN_ONOFF_S);
            in.putExtra("onoff", onoff);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void onoffAssistantLamp(int onoff) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.LAMP_ONOFF_S);
            in.putExtra("onoff", onoff);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void onoffScanner(int onoff) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_ONOFF_S);
            in.putExtra("onoff", onoff);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void startScanner() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_SCAN_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void cancelScanner() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_CANCEL_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void onoffPrinter(int onoff) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.PRINTER_ONOFF_S);
            in.putExtra("onoff", onoff);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void getPrinterSize() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.PRINTER_SUPPORTSIZE_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void printerHasPager() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.PRINTER_HASPAPER_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void printerMorePager() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.PRINTER_NEEDMORE_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void printer(String str) throws RemoteException {
            if (ConfigureTools.getPrinter(FBoxRemoteService.this).equals(Constant.PrinterTYPE.SPRT)) {
                Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
                in.putExtra("action", Action.PRINTER_PRINT_S);
                in.putExtra("pstr", str);
                FBoxRemoteService.this.startService(in);
            } else {
                Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
                in.putExtra("action", Action.PRINTER_PRINT_S);
                in.putExtra("json", str);
                FBoxRemoteService.this.startService(in);
            }
        }

        @Override
        public void batchOpenDoor(String[] boxIds) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_BATCH_OPEN_S);
            in.putExtra("batchboxid", boxIds);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public boolean openSingleDoor(String boxId) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_OPEN_S);
            in.putExtra("boxid", boxId);
            FBoxRemoteService.this.startService(in);
            return true;
        }

        @Override
        public void simpleBatchQuery(String[] boxIds) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_SIMPLEBATCHQUERY_S);
            in.putExtra("batchboxid", boxIds);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void queryAll(String boxCount) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_QUERYALL_S);
            in.putExtra("boxCount", boxCount);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void querySingleDoor(String boxId) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_QUERY_S);
            in.putExtra("boxid", boxId);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void queryAssetCode() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.BOXINFO_QUERY_S);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void setAssetCode(String[] boxIdArray, String[] assetCodeArray) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.BOXINFO_WRITE_S);
            in.putExtra("array.assetCode", assetCodeArray);
            in.putExtra("array.boxId", boxIdArray);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void getMidModifyInfo() throws RemoteException {
            Intent intent2 = new Intent("android.intent.action.hal.cfgtable.result");
            intent2.putExtra("halVersion", "V2.7.5");
            intent2.putExtra("vendorName", "陕西瑞迅电子信息技术有限公司");
            intent2.putExtra("vendorAppVersion", "20200619");
            intent2.putExtra("vendorAppName", "通用中间层软件");
            FBoxRemoteService.this.sendBroadcast(intent2);
        }

        @Override
        public void reboot(long delayMs) throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.APP_REBOOT_S);
            in.putExtra("delayMs", delayMs);
            FBoxRemoteService.this.startService(in);
        }

        @Override
        public void getMainBoardTemp() throws RemoteException {
            Intent in = new Intent(FBoxRemoteService.this, MainServer.class);
            in.putExtra("action", Action.SYS_QUERY_S);
            in.putExtra("attributes", "temp.mainboard");
            FBoxRemoteService.this.startService(in);
        }
    };

}

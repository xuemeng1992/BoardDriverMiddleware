package com.rocktech.boarddriver.mainserver;

import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.LockerFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.sf.module.edms.hal.aidl.IAIDLChannel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

public class AIDLService extends Service {

    private static String msgClientIdOld = "";

    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final IAIDLChannel.Stub mBinder = new IAIDLChannel.Stub() {

        @Override
        public boolean openDoor(String boxId, String msgClientId, String msgTime) throws RemoteException {
            if (!TextUtils.isEmpty(boxId) && boxId.length() == 3) {
                ILocker locker = LockerFactory.getLocker(ConfigureTools.getLockerTypeBean(AIDLService.this).getId(),
                        AIDLService.this);
                if (CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(AIDLService.this).getId()).configureCheck(AIDLService.this, locker, true)) {
                    return locker.openLock(boxId);
                }
            }
            return false;
        }
    };
}

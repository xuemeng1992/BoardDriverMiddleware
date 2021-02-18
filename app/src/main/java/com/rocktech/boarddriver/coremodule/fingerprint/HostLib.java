package com.rocktech.boarddriver.coremodule.fingerprint;

import android.content.Context;

public class HostLib {

    private static HostLib m_hHostLib;
    private static Context m_hActivity;

    public HostLib(Context parentActivity) {
        this.m_hActivity = parentActivity;
    }

    public static HostLib getInstance(Context parentActivity) {
        if (m_hHostLib == null) {
            synchronized (HostLib.class) {
                if (m_hHostLib == null) {
                    m_hHostLib = new HostLib(parentActivity);
                }
            }
        }
        return m_hHostLib;
    }

    public CommandProc FPCmdProc() {
        return CommandProc.getInstance(m_hActivity);
    }

    public DevComm FPDevComm() {
        return DevComm.getInstance(m_hActivity);
    }

}

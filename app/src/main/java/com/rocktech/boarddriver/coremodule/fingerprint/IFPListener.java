package com.rocktech.boarddriver.coremodule.fingerprint;

public abstract interface IFPListener {

    public static abstract interface FPCommandListener {
        public abstract void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1, int p_nParam2);

        public abstract void cmdProcReturnData(byte[] p_pData, int p_nSize);

        public abstract void cmdProcShowText(String p_szInfo);

        public abstract void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet, int p_nParam1);
    }

    public static abstract interface FPCancelListener {
        public abstract void cancelReturn(int p_nRetCode);
    }

    public static abstract interface FPCancelWaitListener {
        public abstract void runProcReturn(int p_nRetCode);
    }

    public static abstract interface FPUSBPermissionListener {
        public abstract void usbPermissionAllowed();

        public abstract void usbPermissionDenied();
    }

}

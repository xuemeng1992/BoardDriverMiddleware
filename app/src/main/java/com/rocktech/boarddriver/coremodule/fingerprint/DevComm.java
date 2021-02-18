package com.rocktech.boarddriver.coremodule.fingerprint;

import android.content.Context;
import android.widget.Spinner;

import com.rocktech.boarddriver.tools.LoggerUtils;

import java.util.Arrays;

public class DevComm {

    private static DevComm m_hDevComm;
    private static Context m_hActivity;

    // Packet Prefix
    public static final int CMD_PREFIX_CODE = (0xAA55);
    public static final int RCM_PREFIX_CODE = (0x55AA);
    public static final int CMD_DATA_PREFIX_CODE = (0xA55A);
    public static final int RCM_DATA_PREFIX_CODE = (0x5AA5);

    // Command
    public static final int CMD_VERIFY_CODE = (0x0101);
    public static final int CMD_IDENTIFY_CODE = (0x0102);
    public static final int CMD_ENROLL_CODE = (0x0103);
    public static final int CMD_ENROLL_ONETIME_CODE = (0x0104);
    public static final int CMD_CLEAR_TEMPLATE_CODE = (0x0105);
    public static final int CMD_CLEAR_ALLTEMPLATE_CODE = (0x0106);
    public static final int CMD_GET_EMPTY_ID_CODE = (0x0107);
    public static final int CMD_GET_BROKEN_TEMPLATE_CODE = (0x0109);
    public static final int CMD_READ_TEMPLATE_CODE = (0x010A);
    public static final int CMD_WRITE_TEMPLATE_CODE = (0x010B);
    public static final int CMD_GET_FW_VERSION_CODE = (0x0112);
    public static final int CMD_FINGER_DETECT_CODE = (0x0113);
    public static final int CMD_FEATURE_OF_CAPTURED_FP_CODE = (0x011A);
    public static final int CMD_IDENTIFY_TEMPLATE_WITH_FP_CODE = (0x011C);
    public static final int CMD_SLED_CTRL_CODE = (0x0124);
    public static final int CMD_IDENTIFY_FREE_CODE = (0x0125);
    public static final int CMD_SET_DEVPASS_CODE = (0x0126);
    public static final int CMD_VERIFY_DEVPASS_CODE = (0x0127);
    public static final int CMD_GET_ENROLL_COUNT_CODE = (0x0128);
    public static final int CMD_CHANGE_TEMPLATE_CODE = (0x0129);
    public static final int CMD_UP_IMAGE_CODE = (0x012C);
    public static final int CMD_VERIFY_WITH_DOWN_TMPL_CODE = (0x012D);
    public static final int CMD_IDENTIFY_WITH_DOWN_TMPL_CODE = (0x012E);
    public static final int CMD_FP_CANCEL_CODE = (0x0130);
    public static final int CMD_ADJUST_SENSOR_CODE = (0x0137);
    public static final int CMD_IDENTIFY_WITH_IMAGE_CODE = (0x0138);
    public static final int CMD_VERIFY_WITH_IMAGE_CODE = (0x0139);
    public static final int CMD_SET_PARAMETER_CODE = (0x013A);
    public static final int CMD_EXIT_DEVPASS_CODE = (0x013B);
    public static final int CMD_TEST_CONNECTION_CODE = (0x0150);
    public static final int CMD_ENTERSTANDBY_CODE = (0x0155);
    public static final int RCM_INCORRECT_COMMAND_CODE = (0x0160);
    public static final int CMD_ENTER_ISPMODE_CODE = (0x0171);

    // Error Code
    public static final int ERR_SUCCESS = (0);
    public static final int ERR_FAIL = (1);
    public static final int ERR_CONTINUE = (2);
    public static final int ERR_COMM_FAIL = (3);
    public static final int ERR_VERIFY = (0x11);
    public static final int ERR_IDENTIFY = (0x12);
    public static final int ERR_TMPL_EMPTY = (0x13);
    public static final int ERR_TMPL_NOT_EMPTY = (0x14);
    public static final int ERR_ALL_TMPL_EMPTY = (0x15);
    public static final int ERR_EMPTY_ID_NOEXIST = (0x16);
    public static final int ERR_BROKEN_ID_NOEXIST = (0x17);
    public static final int ERR_INVALID_TMPL_DATA = (0x18);
    public static final int ERR_DUPLICATION_ID = (0x19);
    public static final int ERR_TOO_FAST = (0x20);
    public static final int ERR_BAD_QUALITY = (0x21);
    public static final int ERR_SMALL_LINES = (0x22);
    public static final int ERR_TIME_OUT = (0x23);
    public static final int ERR_NOT_AUTHORIZED = (0x24);
    public static final int ERR_GENERALIZE = (0x30);
    public static final int ERR_COM_TIMEOUT = (0x40);
    public static final int ERR_FP_CANCEL = (0x41);
    public static final int ERR_INTERNAL = (0x50);
    public static final int ERR_MEMORY = (0x51);
    public static final int ERR_EXCEPTION = (0x52);
    public static final int ERR_INVALID_TMPL_NO = (0x60);
    public static final int ERR_INVALID_PARAM = (0x70);
    public static final int ERR_NO_RELEASE = (0x71);
    public static final int ERR_INVALID_OPERATION_MODE = (0x72);
    public static final int ERR_NOT_SET_PWD = (0x74);
    public static final int ERR_FP_NOT_DETECTED = (0x75);
    public static final int ERR_ADJUST_SENSOR = (0x76);

    // Return Value
    public static final int NEED_FIRST_SWEEP = (0xFFF1);
    public static final int NEED_SECOND_SWEEP = (0xFFF2);
    public static final int NEED_THIRD_SWEEP = (0xFFF3);
    public static final int NEED_RELEASE_FINGER = (0xFFF4);
    public static final int TEMPLATE_NOT_EMPTY = (0x01);
    public static final int TEMPLATE_EMPTY = (0x00);
    public static final int DETECT_FINGER = (0x01);
    public static final int NO_DETECT_FINGER = (0x00);
    public static final int DOWNLOAD_SUCCESS = (0xA1);

    // Packet
    public static final int MAX_DATA_LEN = (610);
    public static final int CMD_PACKET_LEN = (22);
    public static final int ST_COMMAND_LEN = (66);
    public static final int IMAGE_RECEIVE_UINT = (498);
    public static final int DATA_SPLIT_UNIT = (498);
    public static final int OLD_USER_TEMPLATE_SIZE = (498);

    // Template
    public static final int MAX_RECORD_COUNT = (5000);
    public static final int SZ_TEMPLATE_SIZE = (570);
    public static final int SZ_MAX_RECORD_SIZE = (900);

    // Image
    public static final int SZ_IMG_WIDTH = (256);
    public static final int SZ_IMG_HEIGHT = (288);

    public static final int SCSI_TIMEOUT = (3000);
    public static final int SZ_MAX_FP_TIME_OUT = (60);
    public static final int COMM_SLEEP_TIME = (100);
    public static final int ONCE_UP_IMAGE_UINT = (60000);
    public static final int COMM_TIMEOUT = (15000);

    public int m_nPacketSize;
    public byte m_bySrcDeviceID = 1, m_byDstDeviceID = 1;
    public byte[] m_abyPacket = new byte[64 * 1024];
    public byte[] m_abyPacket2 = new byte[MAX_DATA_LEN + 10];
    public byte[] m_abyPacketTmp = new byte[64 * 1024];

    private final Context mApplicationContext;
    private static final int VID = 0x2009;
    private static final int PID = 0x7638;

    public static boolean m_bSendPacketWork = false;

    // USB
    private UsbController m_usbBase;
    public IFPListener.FPUSBPermissionListener m_lsUSBPermission = null;

    public DevComm(Context parentActivity) {

        this.m_hActivity = parentActivity;
        mApplicationContext = parentActivity;

        // USB Init
        m_usbBase = new UsbController(parentActivity, m_IConnectionHandler, VID, PID);

    }

    public static DevComm getInstance(Context parentActivity) {
        if (m_hDevComm == null) {
            synchronized (DevComm.class) {
                if (m_hDevComm == null) {
                    m_hDevComm = new DevComm(parentActivity);
                }
            }
        }
        return m_hDevComm;
    }

    public int GetDeviceList(Spinner p_spDevice) {
        return 0;
    }

    public boolean IsInit() {
        return m_usbBase.IsInit();
    }

    public boolean OpenComm(String p_szDevice, int p_nBaudrate, IFPListener.FPUSBPermissionListener p_lsUSBPermission) {
        m_lsUSBPermission = p_lsUSBPermission;
        if (!m_usbBase.IsInit()) {
            m_usbBase.init();
        }
        if (!m_usbBase.IsInit()) {
            return false;
        }
        return true;
    }

    public boolean CloseComm() {
        m_usbBase.uninit();
        return true;
    }

    int Run_TestConnection() {
        boolean w_bRet;

        InitPacket((short) CMD_TEST_CONNECTION_CODE, true);
        AddCheckSum(true);

        w_bRet = Send_Command((short) CMD_TEST_CONNECTION_CODE);

        if (!w_bRet) {
            return ERR_COMM_FAIL;
        }

        if (GetRetCode() != ERR_SUCCESS) {
            return ERR_FAIL;
        }

        return ERR_SUCCESS;
    }

    int Run_GetDeviceInfo() {
        return ERR_SUCCESS;
    }

    public boolean GetDeviceInformation(String[] deviceInfo) {
        int[] w_nRecvLen = new int[1];
        byte[] w_abyPCCmd = new byte[6];
        byte[] w_abyData = new byte[32];

        String w_strTmp;
        boolean w_bRet;

        Arrays.fill(w_abyPCCmd, (byte) 0);

        w_abyPCCmd[2] = 0x04;

        w_bRet = SendPackage(w_abyPCCmd, w_abyData);

        if (!w_bRet) {
            return false;
        }

        w_bRet = RecvPackage(w_abyData, w_nRecvLen);

        if (!w_bRet) {
            return false;
        }

        w_strTmp = new String(w_abyData);
        deviceInfo[0] = w_strTmp;

        return true;
    }

    private boolean SendPackage(byte[] pPCCmd, byte[] pData) {
        int nDataLen;

        pPCCmd[0] = (byte) 0xEF;
        pPCCmd[1] = 0x01;

        nDataLen = (int) ((((pPCCmd[5] & 0xFF) << 8) & 0x0000FF00) | (pPCCmd[4] & 0x000000FF));

        return m_usbBase.UsbSCSIWrite(pPCCmd, 6, pData, nDataLen, 5000);
    }

    private boolean RecvPackage(byte[] pData, int[] pLevRen) {
        int w_nLen;
        byte[] w_abyPCCmd = new byte[6];
        byte[] w_abyRespond = new byte[4];
        boolean w_bRet;

        w_abyPCCmd[0] = (byte) 0xEF;
        w_abyPCCmd[1] = 0x02;
        w_abyPCCmd[2] = 0;
        w_abyPCCmd[3] = 0;
        w_abyPCCmd[4] = 0;
        w_abyPCCmd[5] = 0;

        // receive status
        w_bRet = m_usbBase.UsbSCSIRead(w_abyPCCmd, 6, w_abyRespond, 4, 5000);

        if (!w_bRet)
            return false;

        // receive data
        w_nLen = (int) ((int) ((w_abyRespond[3] << 8) & 0x0000FF00) | (int) (w_abyRespond[2] & 0x000000FF));

        if (w_nLen > 0) {
            //w_nTime = SystemClock.elapsedRealtime();

            w_abyPCCmd[1] = 0x03;
            w_bRet = m_usbBase.UsbSCSIRead(w_abyPCCmd, 6, pData, w_nLen, 5000);

            if (!w_bRet)
                return false;

            pLevRen[0] = w_nLen;
        }

        return true;
    }

    public short GetRetCode() {
        return (short) ((int) ((m_abyPacket[7] << 8) & 0x0000FF00) | (int) (m_abyPacket[6] & 0x000000FF));
    }

    public short GetDataLen() {
        return (short) (((m_abyPacket[5] << 8) & 0x0000FF00) | (m_abyPacket[4] & 0x000000FF));
    }

    public void SetDataLen(short p_wDataLen) {
        m_abyPacket[4] = (byte) (p_wDataLen & 0xFF);
        m_abyPacket[5] = (byte) (((p_wDataLen & 0xFF00) >> 8) & 0xFF);
    }

    public void SetDataLen2(short p_wDataLen) {
        m_abyPacket2[4] = (byte) (p_wDataLen & 0xFF);
        m_abyPacket2[5] = (byte) (((p_wDataLen & 0xFF00) >> 8) & 0xFF);
    }

    public void SetCmdData(short p_wData, boolean p_bFirst) {
        if (p_bFirst) {
            m_abyPacket[6] = (byte) (p_wData & 0xFF);
            m_abyPacket[7] = (byte) (((p_wData & 0xFF00) >> 8) & 0xFF);
        } else {
            m_abyPacket[8] = (byte) (p_wData & 0xFF);
            m_abyPacket[9] = (byte) (((p_wData & 0xFF00) >> 8) & 0xFF);
        }
    }

    public short GetCmdData(boolean p_bFirst) {
        if (p_bFirst) {
            return (short) (((m_abyPacket[7] << 8) & 0x0000FF00) | (m_abyPacket[6] & 0x000000FF));
        } else {
            return (short) (((m_abyPacket[9] << 8) & 0x0000FF00) | (m_abyPacket[8] & 0x000000FF));
        }
    }

    private short GetDataPacketLen() {
        return (short) (((m_abyPacket[5] << 8) & 0x0000FF00) | (m_abyPacket[4] & 0x000000FF) + 6);
    }

    void InitPacket(short p_wCmd, boolean p_bCmdData) {
        memset(m_abyPacket, (byte) 0, CMD_PACKET_LEN);

        //g_pPacketBuffer->wPrefix = p_bCmdData?CMD_PREFIX_CODE:CMD_DATA_PREFIX_CODE;
        if (p_bCmdData) {
            m_abyPacket[0] = (byte) (CMD_PREFIX_CODE & 0xFF);
            m_abyPacket[1] = (byte) (((CMD_PREFIX_CODE & 0xFF00) >> 8) & 0xFF);
        } else {
            m_abyPacket[0] = (byte) (CMD_DATA_PREFIX_CODE & 0xFF);
            m_abyPacket[1] = (byte) (((CMD_DATA_PREFIX_CODE & 0xFF00) >> 8) & 0xFF);
        }

        //g_pPacketBuffer->wCMD_RCM = p_wCMD;
        m_abyPacket[2] = (byte) (p_wCmd & 0xFF);
        m_abyPacket[3] = (byte) (((p_wCmd & 0xFF00) >> 8) & 0xFF);
    }

    void InitPacket2(short p_wCmd, boolean p_bCmdData) {
        memset(m_abyPacket2, (byte) 0, CMD_PACKET_LEN);

        //g_pPacketBuffer->wPrefix = p_bCmdData?CMD_PREFIX_CODE:CMD_DATA_PREFIX_CODE;
        if (p_bCmdData) {
            m_abyPacket2[0] = (byte) (CMD_PREFIX_CODE & 0xFF);
            m_abyPacket2[1] = (byte) (((CMD_PREFIX_CODE & 0xFF00) >> 8) & 0xFF);
        } else {
            m_abyPacket2[0] = (byte) (CMD_DATA_PREFIX_CODE & 0xFF);
            m_abyPacket2[1] = (byte) (((CMD_DATA_PREFIX_CODE & 0xFF00) >> 8) & 0xFF);
        }

        //g_pPacketBuffer->wCMD_RCM = p_wCMD;
        m_abyPacket2[2] = (byte) (p_wCmd & 0xFF);
        m_abyPacket2[3] = (byte) (((p_wCmd & 0xFF00) >> 8) & 0xFF);
    }

    short GetCheckSum(boolean p_bCmdData) {
        short w_wRet = 0;
        short w_nI = 0;

        w_wRet = 0;
        if (p_bCmdData) {
            for (w_nI = 0; w_nI < CMD_PACKET_LEN; w_nI++)
                w_wRet += (m_abyPacket[w_nI] & 0xFF);
        } else {
            for (w_nI = 0; w_nI < GetDataPacketLen(); w_nI++)
                w_wRet += (m_abyPacket[w_nI] & 0xFF);
        }
        return w_wRet;
    }

    short AddCheckSum(boolean p_bCmdData) {
        short w_wRet = 0;
        short w_wLen = 0;
        int w_nI;

        if (p_bCmdData)
            w_wLen = CMD_PACKET_LEN;
        else
            w_wLen = GetDataPacketLen();

        w_wRet = 0;
        for (w_nI = 0; w_nI < w_wLen; w_nI++)
            w_wRet += (m_abyPacket[w_nI] & 0xFF);

        m_abyPacket[w_wLen] = (byte) (w_wRet & 0xFF);
        m_abyPacket[w_wLen + 1] = (byte) (((w_wRet & 0xFF00) >> 8) & 0xFF);

        return w_wRet;
    }

    short AddCheckSum2(boolean p_bCmdData) {
        short w_wRet = 0;
        short w_wLen = 0;
        int w_nI;

        if (p_bCmdData)
            w_wLen = CMD_PACKET_LEN;
        else
            w_wLen = GetDataPacketLen();

        w_wRet = 0;
        for (w_nI = 0; w_nI < w_wLen; w_nI++)
            w_wRet += (m_abyPacket2[w_nI] & 0xFF);

        m_abyPacket2[w_wLen] = (byte) (w_wRet & 0xFF);
        m_abyPacket2[w_wLen + 1] = (byte) (((w_wRet & 0xFF00) >> 8) & 0xFF);

        return w_wRet;
    }

    boolean CheckReceive(short p_wPrefix, short p_wCmd) {
        short w_wCheckSum;
        short w_wTmpPrefix;
        short w_wTmpCmd;
        short w_wLen;

        // Check Prefix Code
        w_wTmpPrefix = (short) (((m_abyPacket[1] << 8) & 0x0000FF00) | (m_abyPacket[0] & 0x000000FF));
        w_wTmpCmd = (short) (((m_abyPacket[3] << 8) & 0x0000FF00) | (m_abyPacket[2] & 0x000000FF));

        if (w_wTmpCmd != CMD_FP_CANCEL_CODE) {
            if ((p_wPrefix != w_wTmpPrefix) || (p_wCmd != w_wTmpCmd)) {
                return false;
            }
        }

        if (p_wPrefix == RCM_PREFIX_CODE)
            w_wLen = CMD_PACKET_LEN;
        else
            w_wLen = GetDataPacketLen();

        w_wCheckSum = (short) (((m_abyPacket[w_wLen + 1] << 8) & 0x0000FF00) | (m_abyPacket[w_wLen] & 0x000000FF));

        if (w_wCheckSum != GetCheckSum(p_wPrefix == RCM_PREFIX_CODE)) {
            return false;
        }
        return true;
    }

    public boolean Send_Command(short p_wCmd) {
        return USB_SendPacket(p_wCmd);
    }

    public boolean Send_DataPacket(short p_wCmd) {
        return USB_SendDataPacket(p_wCmd);
    }

    public boolean Receive_DataPacket(short p_wCmd) {
        return USB_ReceiveDataPacket(p_wCmd);
    }

    public boolean USB_SendPacket(short wCMD) {
        byte[] btCDB = new byte[8];
        boolean w_bRet;

        Arrays.fill(btCDB, (byte) 0);

        btCDB[0] = (byte) 0xEF;
        btCDB[1] = 0x11;
        btCDB[4] = CMD_PACKET_LEN + 2;

        while (m_bSendPacketWork) {
            try {
                Thread.currentThread().sleep(3);//毫秒
            } catch (Exception e) {
            }
        }
        m_bSendPacketWork = true;
        w_bRet = m_usbBase.UsbSCSIWrite(btCDB, 8, m_abyPacket, (int) (CMD_PACKET_LEN + 2), SCSI_TIMEOUT);
        m_bSendPacketWork = false;

        if (!w_bRet)
            return false;

        return USB_ReceiveAck(wCMD);
    }

    public boolean USB_SendPacket2(short wCMD) {
        byte[] btCDB = new byte[8];
        boolean w_bRet;

        Arrays.fill(btCDB, (byte) 0);

        btCDB[0] = (byte) 0xEF;
        btCDB[1] = 0x11;
        btCDB[4] = CMD_PACKET_LEN + 2;

        while (m_bSendPacketWork) {
            try {
                Thread.currentThread().sleep(3);//毫秒
            } catch (Exception e) {
            }
        }
        m_bSendPacketWork = true;
        w_bRet = m_usbBase.UsbSCSIWrite(btCDB, 8, m_abyPacket2, (int) (CMD_PACKET_LEN + 2), SCSI_TIMEOUT);
        m_bSendPacketWork = false;

        if (!w_bRet)
            return false;

        return true;
    }

    public boolean USB_ReceiveAck(short p_wCmd) {
        int w_nLen;
        byte[] btCDB = new byte[8];
        byte[] w_abyWaitPacket = new byte[CMD_PACKET_LEN + 2];
        int w_dwTimeOut = SCSI_TIMEOUT;

        if (p_wCmd == CMD_VERIFY_CODE ||
                p_wCmd == CMD_IDENTIFY_CODE ||
                p_wCmd == CMD_IDENTIFY_FREE_CODE ||
                p_wCmd == CMD_ENROLL_CODE ||
                p_wCmd == CMD_ENROLL_ONETIME_CODE)
            w_dwTimeOut = (SZ_MAX_FP_TIME_OUT + 1) * (1000);

        Arrays.fill(btCDB, (byte) 0);

        //w_nReadCount = GetReadWaitTime(p_byCMD);

        Arrays.fill(w_abyWaitPacket, (byte) 0xAF);

        do {
            Arrays.fill(m_abyPacket, (byte) 0);

            btCDB[0] = (byte) 0xEF;
            btCDB[1] = (byte) 0x12;

            w_nLen = CMD_PACKET_LEN + 2;

            while (m_bSendPacketWork) {
                try {
                    Thread.currentThread().sleep(3);//毫秒
                } catch (Exception e) {
                }
            }
            m_bSendPacketWork = true;

            if (!m_usbBase.UsbSCSIRead(btCDB, 8, m_abyPacket, w_nLen, w_dwTimeOut))
                return false;

            m_bSendPacketWork = false;

            try {
                Thread.currentThread().sleep(COMM_SLEEP_TIME);//毫秒
            } catch (Exception e) {
            }
        } while (memcmp(m_abyPacket, w_abyWaitPacket, CMD_PACKET_LEN + 2) == true);

        m_nPacketSize = w_nLen;

        if (!CheckReceive((short) RCM_PREFIX_CODE, p_wCmd))
            return false;

        return true;
    }

    public boolean USB_ReceiveAck2(short p_wCmd) {
        int w_nLen;
        byte[] btCDB = new byte[8];
        byte[] w_abyWaitPacket = new byte[CMD_PACKET_LEN + 2];
        int w_dwTimeOut = SCSI_TIMEOUT;

        Arrays.fill(btCDB, (byte) 0);
        Arrays.fill(w_abyWaitPacket, (byte) 0xAF);

        do {
            Arrays.fill(m_abyPacket2, (byte) 0);

            btCDB[0] = (byte) 0xEF;
            btCDB[1] = (byte) 0x12;

            w_nLen = CMD_PACKET_LEN + 2;

            while (m_bSendPacketWork) {
                try {
                    Thread.currentThread().sleep(3);//毫秒
                } catch (Exception e) {
                }
            }
            m_bSendPacketWork = true;

            if (!m_usbBase.UsbSCSIRead(btCDB, 8, m_abyPacket2, w_nLen, w_dwTimeOut))
                return false;

            m_bSendPacketWork = false;

            try {
                Thread.currentThread().sleep(COMM_SLEEP_TIME);//毫秒
            } catch (Exception e) {
            }
        } while (memcmp(m_abyPacket2, w_abyWaitPacket, CMD_PACKET_LEN + 2) == true);

        m_nPacketSize = w_nLen;

        return true;
    }

    boolean USB_ReceiveDataAck(short p_wCmd) {
        byte[] btCDB = new byte[8];
        byte[] w_WaitPacket = new byte[8];
        int w_nLen;
        int w_dwTimeOut = COMM_TIMEOUT;

        if (p_wCmd == CMD_VERIFY_CODE ||
                p_wCmd == CMD_IDENTIFY_CODE ||
                p_wCmd == CMD_IDENTIFY_FREE_CODE ||
                p_wCmd == CMD_ENROLL_CODE ||
                p_wCmd == CMD_ENROLL_ONETIME_CODE)
            w_dwTimeOut = (SZ_MAX_FP_TIME_OUT + 1) * (1000);

        memset(btCDB, (byte) 0, 8);
        memset(w_WaitPacket, (byte) 0xAF, 8);
        Arrays.fill(m_abyPacketTmp, (byte) 0);

        do {
            btCDB[0] = (byte) 0xEF;
            btCDB[1] = 0x15;
            w_nLen = 6;

            if (!m_usbBase.UsbSCSIRead(btCDB, 8, m_abyPacket, w_nLen, w_dwTimeOut)) {
                return false;
            }

            try {
                Thread.currentThread().sleep(COMM_SLEEP_TIME);//毫秒
            } catch (Exception e) {
            }
        } while (memcmp(m_abyPacket, w_WaitPacket, 6) == true);

        do {
            w_nLen = GetDataLen() + 2;
            if (USB_ReceiveRawData(m_abyPacketTmp, w_nLen) == false) {
                return false;
            }
            System.arraycopy(m_abyPacketTmp, 0, m_abyPacket, 6, w_nLen);
            try {
                Thread.currentThread().sleep(COMM_SLEEP_TIME);//毫秒
            } catch (Exception e) {
            }
        } while (memcmp(m_abyPacket, w_WaitPacket, 4) == true);

        if (!CheckReceive((short) RCM_DATA_PREFIX_CODE, p_wCmd)) {
            return false;
        }

        return true;
    }

    boolean USB_SendDataPacket(short wCMD) {
        byte[] btCDB = new byte[8];
        short w_wLen = (short) (GetDataPacketLen() + 2);

        memset(btCDB, (byte) 0, 8);

        btCDB[0] = (byte) 0xEF;
        btCDB[1] = 0x13;

        btCDB[4] = (byte) (w_wLen & 0xFF);
        btCDB[5] = (byte) (((w_wLen & 0xFF00) >> 8) & 0xFF);

        if (!m_usbBase.UsbSCSIWrite(btCDB, 8, m_abyPacket, GetDataPacketLen() + 2, SCSI_TIMEOUT))
            return false;

        return USB_ReceiveDataAck(wCMD);
    }

    public boolean USB_ReceiveDataPacket(short wCMD) {
        return USB_ReceiveDataAck(wCMD);
    }

    boolean USB_ReceiveRawData(byte[] pBuffer, int nDataLen) {
        int w_nDataCnt = nDataLen;
        byte[] btCDB = new byte[8];

        memset(btCDB, (byte) 0, 8);
        btCDB[0] = (byte) 0xEF;
        btCDB[1] = (byte) 0x14;
        if (!m_usbBase.UsbSCSIRead(btCDB, 8, pBuffer, w_nDataCnt, SCSI_TIMEOUT)) {
            return false;
        }

        return true;
    }

    public boolean USB_ReceiveImage(byte[] p_pBuffer, int p_dwDataLen) {
        byte[] btCDB = new byte[8];
        byte[] w_WaitPacket = new byte[8];
        int w_nI;
        int w_nIndex;
        int w_nRemainCount;
        byte[] w_pTmpImgBuf = new byte[ONCE_UP_IMAGE_UINT];

        memset(btCDB, (byte) 0, 8);
        memset(w_WaitPacket, (byte) 0xAF, 8);

        if (p_dwDataLen == 208 * 288 || p_dwDataLen == 242 * 266 || p_dwDataLen == 202 * 258 || p_dwDataLen == 256 * 288) {
            w_nIndex = 0;
            w_nRemainCount = p_dwDataLen;
            w_nI = 0;
            while (w_nRemainCount > ONCE_UP_IMAGE_UINT) {
                btCDB[0] = (byte) 0xEF;
                btCDB[1] = 0x16;
                btCDB[2] = (byte) (w_nI & 0xFF);
                if (!m_usbBase.UsbSCSIRead(btCDB, 8, w_pTmpImgBuf, ONCE_UP_IMAGE_UINT, SCSI_TIMEOUT))
                    return false;
                System.arraycopy(w_pTmpImgBuf, 0, p_pBuffer, w_nIndex, ONCE_UP_IMAGE_UINT);
                w_nRemainCount -= ONCE_UP_IMAGE_UINT;
                w_nIndex += ONCE_UP_IMAGE_UINT;
                w_nI++;
            }
            btCDB[0] = (byte) 0xEF;
            btCDB[1] = 0x16;
            btCDB[2] = (byte) (w_nI & 0xFF);
            if (!m_usbBase.UsbSCSIRead(btCDB, 8, w_pTmpImgBuf, w_nRemainCount, SCSI_TIMEOUT))
                return false;
            System.arraycopy(w_pTmpImgBuf, 0, p_pBuffer, w_nIndex, w_nRemainCount);
        }

        return true;
    }

    public boolean USB_DownImage(byte[] pBuf, int nBufLen) {
        byte[] w_pImgBuf = new byte[ONCE_UP_IMAGE_UINT];
        int w_nI;
        int w_nIndex = 0;
        int w_nRemainCount;
        byte[] btCDB = new byte[8];

        w_nIndex = 0;
        w_nRemainCount = nBufLen;
        w_nI = 0;
        memset(btCDB, (byte) 0, 8);

        while (w_nRemainCount > ONCE_UP_IMAGE_UINT) {
            btCDB[0] = (byte) 0xEF;
            btCDB[1] = 0x17;
            btCDB[2] = 0;
            btCDB[3] = (byte) (w_nI & 0xFF);
            btCDB[4] = LOBYTE((short) (ONCE_UP_IMAGE_UINT & 0x00FF));
            btCDB[5] = HIBYTE((short) (ONCE_UP_IMAGE_UINT & 0xFF00));

            System.arraycopy(pBuf, w_nIndex, w_pImgBuf, 0, ONCE_UP_IMAGE_UINT);
            if (!m_usbBase.UsbSCSIWrite(btCDB, 6, w_pImgBuf, ONCE_UP_IMAGE_UINT, SCSI_TIMEOUT))
                return false;

            w_nRemainCount -= ONCE_UP_IMAGE_UINT;
            w_nIndex += ONCE_UP_IMAGE_UINT;
            w_nI++;
        }

        btCDB[0] = (byte) 0xEF;
        btCDB[1] = 0x17;
        btCDB[2] = 0;
        btCDB[3] = (byte) (w_nI & 0xFF);
        btCDB[4] = LOBYTE((short) (w_nRemainCount & 0x00FF));
        btCDB[5] = HIBYTE((short) (w_nRemainCount & 0xFF00));

        System.arraycopy(pBuf, w_nIndex, w_pImgBuf, 0, w_nRemainCount);
        if (!m_usbBase.UsbSCSIWrite(btCDB, 6, w_pImgBuf, w_nRemainCount, SCSI_TIMEOUT))
            return false;

        return true;
    }


    public boolean memcmp(byte[] p1, byte[] p2, int nLen) {
        int i;

        for (i = 0; i < nLen; i++) {
            if (p1[i] != p2[i])
                return false;
        }

        return true;
    }

    public void memset(byte[] p1, byte nValue, int nLen) {
        Arrays.fill(p1, 0, nLen, nValue);
    }

    public void memcpy(byte[] p1, byte[] p2, int nLen) {
        int i;

        for (i = 0; i < nLen; i++) {
            p1[i] = p2[i];
        }
    }

    public short MAKEWORD(byte low, byte high) {
        short s;
        s = (short) ((((high & 0x00FF) << 8) & 0x0000FF00) | (low & 0x000000FF));
        return s;
    }

    public byte LOBYTE(short s) {
        return (byte) (s & 0xFF);
    }

    public byte HIBYTE(short s) {
        return (byte) (((s & 0xFF00) >> 8) & 0xFF);
    }

    private final IUsbConnState m_IConnectionHandler = new IUsbConnState() {
        @Override
        public void onUsbConnected() {
            LoggerUtils.Log().e("Permission allowed!");
            if (m_lsUSBPermission != null) {
                m_lsUSBPermission.usbPermissionAllowed();
            }
        }

        @Override
        public void onUsbPermissionDenied() {
            LoggerUtils.Log().e("Permission denied!");
            if (m_lsUSBPermission != null) {
                m_lsUSBPermission.usbPermissionDenied();
            }
        }

        @Override
        public void onDeviceNotFound() {
            LoggerUtils.Log().e("Can not find usb device!");
        }
    };
}

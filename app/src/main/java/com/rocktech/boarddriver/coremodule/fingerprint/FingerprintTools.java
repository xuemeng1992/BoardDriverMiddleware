package com.rocktech.boarddriver.coremodule.fingerprint;

import android.content.Context;
import android.os.RemoteException;

import com.fcbox.locker.driver.IComparisonFingerprintCallback;
import com.fcbox.locker.driver.IEnterFingerprintCallback;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.tools.Tools;

public class FingerprintTools {

    public static String GetErrorMsg(short p_wErrorCode, IEnterFingerprintCallback callback) {
        String w_ErrMsg;
        switch (p_wErrorCode & 0xFF) {
            case DevComm.ERR_VERIFY:
                w_ErrMsg = "Verify NG";
                break;
            case DevComm.ERR_IDENTIFY:
                w_ErrMsg = "Identify NG";
                break;
            case DevComm.ERR_EMPTY_ID_NOEXIST:
                w_ErrMsg = "Empty Template no Exist";
                break;
            case DevComm.ERR_BROKEN_ID_NOEXIST:
                w_ErrMsg = "Broken Template no Exist";
                break;
            case DevComm.ERR_TMPL_NOT_EMPTY:
                w_ErrMsg = "Template of this ID Already Exist";
                try {
                    if (callback != null) {
                        callback.onNext(0x1367);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case DevComm.ERR_TMPL_EMPTY:
                w_ErrMsg = "This Template is Already Empty";
                break;
            case DevComm.ERR_INVALID_TMPL_NO:
                w_ErrMsg = "Invalid Template No";
                break;
            case DevComm.ERR_ALL_TMPL_EMPTY:
                w_ErrMsg = "All Templates are Empty";
                break;
            case DevComm.ERR_INVALID_TMPL_DATA:
                w_ErrMsg = "Invalid Template Data";
                break;
            case DevComm.ERR_DUPLICATION_ID:
                w_ErrMsg = "Duplicated ID : ";
                break;
            case DevComm.ERR_BAD_QUALITY:
                w_ErrMsg = "Bad Quality Image";
                break;
            case DevComm.ERR_SMALL_LINES:
                w_ErrMsg = "Small line Image";
                break;
            case DevComm.ERR_TOO_FAST:
                w_ErrMsg = "Too fast swiping";
                break;
            case DevComm.ERR_TIME_OUT:
                w_ErrMsg = "Time Out";
                break;
            case DevComm.ERR_GENERALIZE:
                w_ErrMsg = "Fail to Generalize";
                break;
            case DevComm.ERR_NOT_AUTHORIZED:
                w_ErrMsg = "Device not authorized.";
                break;
            case DevComm.ERR_EXCEPTION:
                w_ErrMsg = "Exception Error ";
                break;
            case DevComm.ERR_MEMORY:
                w_ErrMsg = "Memory Error ";
                break;
            case DevComm.ERR_INVALID_PARAM:
                w_ErrMsg = "Invalid Parameter";
                break;
            case DevComm.ERR_NO_RELEASE:
                w_ErrMsg = "No Release Finger Fail";
                break;
            case DevComm.ERR_INTERNAL:
                w_ErrMsg = "Internal Error.";
                break;
            case DevComm.ERR_FP_CANCEL:
                w_ErrMsg = "Canceled.";
                break;
            case DevComm.ERR_INVALID_OPERATION_MODE:
                w_ErrMsg = "Invalid Operation Mode";
                break;
            case DevComm.ERR_NOT_SET_PWD:
                w_ErrMsg = "Password was not set.";
                break;
            case DevComm.ERR_FP_NOT_DETECTED:
                w_ErrMsg = "Finger is not detected.";
                break;
            case DevComm.ERR_ADJUST_SENSOR:
                w_ErrMsg = "Failed to adjust sensor.";
                break;
            default:
                w_ErrMsg = "Fail";
                break;
        }
        try {
            if (callback != null) {
                callback.onComplete(0x1361, w_ErrMsg, "");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return w_ErrMsg;
    }

    public static String GetSuccessMsg(Context context, int p_nParam1,
                                       IEnterFingerprintCallback callback) {
        String m_strPost;
        switch (p_nParam1) {
            case (short) DevComm.NEED_RELEASE_FINGER:
                m_strPost = "Release your finger";
                break;
            case (short) DevComm.NEED_FIRST_SWEEP:
                m_strPost = "Input your finger";
                break;
            case (short) DevComm.NEED_SECOND_SWEEP:
                m_strPost = "Two More";
                try {
                    if (callback != null) {
                        callback.onNext(0x1365);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case (short) DevComm.NEED_THIRD_SWEEP:
                m_strPost = "One More";
                try {
                    if (callback != null) {
                        callback.onNext(0x1365);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    if (callback != null) {
                        callback.onNext(0x1365);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                m_strPost = String.format("Result : 录入成功\r\n模板ID : %d", p_nParam1);
                HostLib.getInstance(context).FPCmdProc().Run_CmdReadTemplate(p_nParam1, true);
                HostLib.getInstance(context).FPCmdProc().SetListener(new IFPListener.FPCommandListener() {

                    @Override
                    public void cmdProcReturn(int p_nCmdCode, int p_nRetCode, int p_nParam1,
                                              int p_nParam2) {
                        if (p_nCmdCode == (short) DevComm.CMD_READ_TEMPLATE_CODE) {
                            if (p_nRetCode != (short) DevComm.ERR_SUCCESS) {
                                try {
                                    if (callback != null) {
                                        callback.onComplete(0x1361, "录入失败", null);
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void cmdProcReturnData(byte[] p_pData, int p_nSize) {
                        try {
                            if (callback != null) {
                                callback.onComplete(0x1360, m_strPost, Tools.byteToHexStr(p_nSize
                                        , p_pData));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void cmdProcShowText(String p_szInfo) {

                    }

                    @Override
                    public void loopResponseEnd(boolean m_bCmdDone, int p_nCode, int p_nRet,
                                                int p_nParam1) {

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
                break;
        }
        return m_strPost;
    }

    public static String GetSuccessMsg(int p_nParam1, IComparisonFingerprintCallback callback) {
        String m_strPost;
        switch (p_nParam1) {
            case (short) DevComm.NEED_RELEASE_FINGER:
                m_strPost = "Release your finger";
                break;
            case (short) DevComm.NEED_FIRST_SWEEP:
                m_strPost = "Input your finger";
                break;
            case (short) DevComm.NEED_SECOND_SWEEP:
                m_strPost = "Two More";
                break;
            case (short) DevComm.NEED_THIRD_SWEEP:
                m_strPost = "One More";
                break;
            default:
                m_strPost = String.format("Result : 比对成功\r\n模板ID : %d", p_nParam1);
                try {
                    if (callback != null) {
                        LoggerUtils.Log().e("comparisonFingerprint   SuccessMsg callback:" + m_strPost);
                        callback.onComplete(0x1380, m_strPost, p_nParam1 + "", 100);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return m_strPost;
    }

    public static String GetErrorMsg2(short p_wErrorCode, IComparisonFingerprintCallback callback) {

        String w_ErrMsg;
        switch (p_wErrorCode & 0xFF) {
            case DevComm.ERR_VERIFY:
                w_ErrMsg = "Verify NG";
                break;
            case DevComm.ERR_IDENTIFY:
                w_ErrMsg = "Identify NG";
                break;
            case DevComm.ERR_EMPTY_ID_NOEXIST:
                w_ErrMsg = "Empty Template no Exist";
                break;
            case DevComm.ERR_BROKEN_ID_NOEXIST:
                w_ErrMsg = "Broken Template no Exist";
                break;
            case DevComm.ERR_TMPL_NOT_EMPTY:
                w_ErrMsg = "Template of this ID Already Exist";
                break;
            case DevComm.ERR_TMPL_EMPTY:
                w_ErrMsg = "This Template is Already Empty";
                break;
            case DevComm.ERR_INVALID_TMPL_NO:
                w_ErrMsg = "Invalid Template No";
                break;
            case DevComm.ERR_ALL_TMPL_EMPTY:
                w_ErrMsg = "All Templates are Empty";
                break;
            case DevComm.ERR_INVALID_TMPL_DATA:
                w_ErrMsg = "Invalid Template Data";
                break;
            case DevComm.ERR_DUPLICATION_ID:
                w_ErrMsg = "Duplicated ID : ";
                break;
            case DevComm.ERR_BAD_QUALITY:
                w_ErrMsg = "Bad Quality Image";
                break;
            case DevComm.ERR_SMALL_LINES:
                w_ErrMsg = "Small line Image";
                break;
            case DevComm.ERR_TOO_FAST:
                w_ErrMsg = "Too fast swiping";
                break;
            case DevComm.ERR_TIME_OUT:
                w_ErrMsg = "Time Out";
                break;
            case DevComm.ERR_GENERALIZE:
                w_ErrMsg = "Fail to Generalize";
                break;
            case DevComm.ERR_NOT_AUTHORIZED:
                w_ErrMsg = "Device not authorized.";
                break;
            case DevComm.ERR_EXCEPTION:
                w_ErrMsg = "Exception Error ";
                break;
            case DevComm.ERR_MEMORY:
                w_ErrMsg = "Memory Error ";
                break;
            case DevComm.ERR_INVALID_PARAM:
                w_ErrMsg = "Invalid Parameter";
                break;
            case DevComm.ERR_NO_RELEASE:
                w_ErrMsg = "No Release Finger Fail";
                break;
            case DevComm.ERR_INTERNAL:
                w_ErrMsg = "Internal Error.";
                break;
            case DevComm.ERR_FP_CANCEL:
                w_ErrMsg = "Canceled.";
                break;
            case DevComm.ERR_INVALID_OPERATION_MODE:
                w_ErrMsg = "Invalid Operation Mode";
                break;
            case DevComm.ERR_NOT_SET_PWD:
                w_ErrMsg = "Password was not set.";
                break;
            case DevComm.ERR_FP_NOT_DETECTED:
                w_ErrMsg = "Finger is not detected.";
                break;
            case DevComm.ERR_ADJUST_SENSOR:
                w_ErrMsg = "Failed to adjust sensor.";
                break;
            default:
                w_ErrMsg = "Fail";
                break;
        }
        try {
            if (callback != null) {
                LoggerUtils.Log().e("comparisonFingerprint   ErrorMsg callback:" + w_ErrMsg +
                        "p_wErrorCode：" + p_wErrorCode);
                callback.onComplete(0x1381, w_ErrMsg, p_wErrorCode + "", 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return w_ErrMsg;
    }
}

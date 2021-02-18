package com.rocktech.boarddriver.coremodule.cardreader;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.fcbox.locker.driver.ICardReaderCallback;
import com.google.gson.Gson;
import com.rocktech.boarddriver.bean.CardReaderResBean;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 读卡器
 */
public class CardReader {
    private final int VID = 1024;    //IDR VID
    private final int PID = 50010;     //IDR PID
    private IDCardReader idCardReader = null;
    public boolean bopen = false;
    private boolean bStoped = false;
    private int mReadCount = 0;
    private CountDownLatch countdownLatch = new CountDownLatch(1);

    private ICardReaderCallback callback;
    private int duration;

    private static CardReader cardReader;

    public static CardReader getInstance() {
        if (cardReader == null) {
            synchronized (CardReader.class) {
                if (cardReader == null) {
                    cardReader = new CardReader();
                }
            }
        }
        return cardReader;
    }

    public void openCardReader(Context context) {
        startIDCardReader(context);
    }

    public void setCallback(ICardReaderCallback c, int d) {
        this.callback = c;
        this.duration = d;
    }

    private void startIDCardReader(Context context) {
        Map idrparams = new HashMap();
        idrparams.put(ParameterHelper.PARAM_KEY_VID, VID);
        idrparams.put(ParameterHelper.PARAM_KEY_PID, PID);
        idCardReader = IDCardReaderFactory.createIDCardReader(context, TransportType.USB, idrparams);
    }

    private UsbManager musbManager = null;
    private final String ACTION_USB_PERMISSION = "com.rocktech.boarddriver.rfid.USB_PERMISSION";

    /**
     * @throws IDCardReaderException
     */
    public void OnBnBegin(Context context) throws IDCardReaderException {
        try {
            idCardReader.open(0);
            bStoped = false;
            mReadCount = 0;
            bopen = true;
            final long currTime = System.currentTimeMillis();
            new Thread(new Runnable() {
                public void run() {
                    while (!bStoped) {
                        if (System.currentTimeMillis() - currTime > duration * 1000) {
                            try {
                                if (callback != null) {
                                    callback.onComplete(0x1321, "", "");
                                }
                                OnBnStop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean bSamStatus = false;
                        try {
                            bSamStatus = idCardReader.getStatus(0);
                        } catch (IDCardReaderException e) {
                            e.printStackTrace();
                        }
                        if (!bSamStatus) {
                            try {
                                idCardReader.reset(0);
                            } catch (IDCardReaderException e) {
                                e.printStackTrace();
                            }
                        }
                        final IDCardInfo idCardInfo = new IDCardInfo();
                        boolean ret = false;
                        try {
                            idCardReader.findCard(0);
                            idCardReader.selectCard(0);
                        } catch (IDCardReaderException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            ret = idCardReader.readCard(0, 0, idCardInfo);
                        } catch (IDCardReaderException e) {
                            try {
                                if (callback != null) {
                                    callback.onComplete(0x1322, e.getMessage(), "");
                                }
                                OnBnStop();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (ret) {
                            try {
                                CardReaderResBean cardReaderResBean = new CardReaderResBean();
                                cardReaderResBean.setId(idCardInfo.getId().length() == 18 ?
                                        idCardInfo.getId().substring(0, idCardInfo.getId().length() - 1) :
                                        idCardInfo.getId());
                                cardReaderResBean.setName(idCardInfo.getName());

                                if (idCardInfo.getId().length() == 15) {
                                    String usex = idCardInfo.getId().substring(14, 15);// 用户的性别
                                    if (Integer.parseInt(usex) % 2 == 0) {
                                        cardReaderResBean.setSex("女");
                                    } else {
                                        cardReaderResBean.setSex("男");
                                    }
                                } else {
                                    if (Integer.parseInt(idCardInfo.getId().substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
                                        cardReaderResBean.setSex("女");
                                    } else {
                                        cardReaderResBean.setSex("男");
                                    }
                                }
                                cardReaderResBean.setEthnicity(idCardInfo.getNation());
                                cardReaderResBean.setEthnicity(idCardInfo.getAddress());
                                if (callback != null) {
                                    callback.onComplete(0x1320, "Success.", new Gson().toJson(cardReaderResBean));
                                }
                                OnBnStop();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    countdownLatch.countDown();
                }
            }).start();
        } catch (IDCardReaderException e) {
            try {
                callback.onComplete(0x1322, "开始读卡失败，错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n内部代码=" + e.getInternalErrorCode(), "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void OnBnStop() {
        if (!bopen) {
            return;
        }
        bStoped = true;
        mReadCount = 0;
        try {
            countdownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            idCardReader.close(0);
        } catch (IDCardReaderException e) {
            e.printStackTrace();
        }
        bopen = false;
    }

    public void distory() {
        IDCardReaderFactory.destroy(idCardReader);
    }
}

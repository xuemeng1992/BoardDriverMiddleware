package com.rocktech.boarddriver.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rocktech.boarddriver.bean.BoardTypeBean;
import com.rocktech.boarddriver.bean.CupboardFunctionBean;
import com.rocktech.boarddriver.bean.CustomerConfigBean;
import com.rocktech.boarddriver.bean.DoubleOpenConfig;
import com.rocktech.boarddriver.bean.LockerTypeBean;
import com.rocktech.boarddriver.bean.NeedTemperature;
import com.rocktech.boarddriver.coremodule.printer.Printer;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_QR;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_SHIYIN;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_SPRT;
import com.rocktech.boarddriver.coremodule.printer.manufactor.Printer_XBY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class ConfigureTools {


    public static boolean dataCheck(Context context) {
        String qrcode = ConfigureTools.getQrCode(context);
        if (TextUtils.isEmpty(qrcode)) {
            return false;
        }
        String qrcodeTty = ConfigureTools.getQrCodeTty(context);
        if (TextUtils.isEmpty(qrcodeTty)) {
            return false;
        }
        String printer = ConfigureTools.getPrinter(context);
        if (TextUtils.isEmpty(printer)) {
            return false;
        }
        String printerTty = ConfigureTools.getPrinterTty(context);
        if (TextUtils.isEmpty(printerTty)) {
            return false;
        }
        return true;
    }

    public static void setQrCode(Context context, String qrCode) {
        SPHelper.getInstance(context).putString(Constant.SPKEY.SCANNER, qrCode);
    }

    public static String getQrCode(Context context) {
        return SPHelper.getInstance(context).getString(Constant.SPKEY.SCANNER, "");
    }

    public static void setQrCodeTty(Context context, String qrCodeTty) {
        SPHelper.getInstance(context).putString(Constant.SPKEY.SCANNER_COM, qrCodeTty);
    }

    public static String getQrCodeTty(Context context) {
        return SPHelper.getInstance(context).getString(Constant.SPKEY.SCANNER_COM, "");
    }

    public static void setPrinter(Context context, String printer) {
        SPHelper.getInstance(context).putString(Constant.SPKEY.PRINTER, printer);
    }

    public static String getPrinter(Context context) {
        return SPHelper.getInstance(context).getString(Constant.SPKEY.PRINTER, "");
    }

    public static void setPrinterTty(Context context, String printerTty) {
        SPHelper.getInstance(context).putString(Constant.SPKEY.PRINTER_COM, printerTty);
    }

    public static String getPrinterTty(Context context) {
        return SPHelper.getInstance(context).getString(Constant.SPKEY.PRINTER_COM, "");
    }

    public static void setBuzzer(Context context, boolean onnoff) {
        SPHelper.getInstance(context).putBoolean(Constant.SPKEY.BUZZER, onnoff);
    }

    public static boolean getBuzzer(Context context) {
        return SPHelper.getInstance(context).getBoolean(Constant.SPKEY.BUZZER, false);
    }

    public static void setLockStat(Context context, boolean onnoff) {
        SPHelper.getInstance(context).putBoolean(Constant.SPKEY.LOCKSTAT, onnoff);
    }

    public static boolean getLockStat(Context context) {
        return SPHelper.getInstance(context).getBoolean(Constant.SPKEY.LOCKSTAT, false);
    }

    public static void setBuzEnableOne(Context context, boolean onnoff) {
        SPHelper.getInstance(context).putBoolean(Constant.SPKEY.BUZENABLEONE, onnoff);
    }

    public static boolean getBuzEnableOne(Context context) {
        return SPHelper.getInstance(context).getBoolean(Constant.SPKEY.BUZENABLEONE, false);
    }

    public static void setParity(Context context, int parity) {
        SPHelper.getInstance(context).putInt(Constant.SPKEY.PARITY, parity);
    }

    public static int getParity(Context context) {
        return SPHelper.getInstance(context).getInt(Constant.SPKEY.PARITY, 0);
    }

    public static void setBaudrate(Context context, int baudrate) {
        SPHelper.getInstance(context).putInt(Constant.SPKEY.BAUDRATE, baudrate);
    }

    public static int getBaudrate(Context context) {
        return SPHelper.getInstance(context).getInt(Constant.SPKEY.BAUDRATE, 0);
    }

    public static BoardTypeBean getBoardTypeBean(Context context) {
        if (!TextUtils.isEmpty(getConfigInfo(context, "boardtype.config"))) {
            Gson gson = new Gson();
            BoardTypeBean boardTypeBean = gson.fromJson(getConfigInfo(context, "boardtype.config"), BoardTypeBean.class);
            return boardTypeBean;
        }
        return null;
    }

    public static NeedTemperature getNeedTemperatureBean(Context context) {
        if (!TextUtils.isEmpty(getConfigInfo(context, "needTemperature.config"))) {
            Gson gson = new Gson();
            NeedTemperature needTemperature = gson.fromJson(getConfigInfo(context, "needTemperature.config"), NeedTemperature.class);
            return needTemperature;
        }
        return null;
    }

    public static LockerTypeBean getLockerTypeBean(Context context) {
        if (!TextUtils.isEmpty(getConfigInfo(context, "lockertype.config"))) {
            Gson gson = new Gson();
            LockerTypeBean boardTypeBean = gson.fromJson(getConfigInfo(context, "lockertype.config"), LockerTypeBean.class);
            return boardTypeBean;
        }
        return null;
    }

    public static String[] getCupboardFunction(Context context) {
        List<CupboardFunctionBean> list = getCupboardFunctionList(context);
        String[] cbfs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cbfs[i] = list.get(i).getName();
        }
        return cbfs;
    }

    public static String[] getCupboardProductType(Context context, int index) {
        List<CupboardFunctionBean> list = getCupboardFunctionList(context);
        List<CupboardFunctionBean.CupboardProductTypeBean> typeList = list.get(index).getCupboardProductType();
        String[] cpts = new String[typeList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            cpts[i] = typeList.get(i).getName();
        }
        return cpts;
    }

    private static List<CupboardFunctionBean> getCupboardFunctionList(Context context) {
        List<CupboardFunctionBean> cupboardFunctionBeanList = null;
        if (!TextUtils.isEmpty(getConfigInfo(context, "product.config"))) {
            Gson gson = new Gson();
            Type t = new TypeToken<List<CupboardFunctionBean>>() {
            }.getType();
            cupboardFunctionBeanList = gson.fromJson(getConfigInfo(context, "product.config"), t);
        }
        return cupboardFunctionBeanList;
    }

    public static CustomerConfigBean getCustomerConfigBean(Context context, String customer_type) {
        if (!TextUtils.isEmpty(getConfigInfo(context, "customer.config"))) {
            Gson gson = new Gson();
            Type t = new TypeToken<List<CustomerConfigBean>>() {
            }.getType();
            List<CustomerConfigBean> list = gson.fromJson(getConfigInfo(context, "customer.config"), t);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(customer_type)) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    public static DoubleOpenConfig getDoubleOpenConfig(Context context, String fileName) {
        if (!TextUtils.isEmpty(getConfigInfo(context, fileName))) {
            Gson gson = new Gson();
            DoubleOpenConfig config = gson.fromJson(getConfigInfo(context, fileName), DoubleOpenConfig.class);
            return config;
        }
        return null;
    }

    private static String getConfigInfo(Context context, String filename) {
        AssetManager am = context.getResources().getAssets();
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(am.open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Printer creatPrinter(String str, Context mContext, Printer printer) {
        if (printer == null) {
            if (str.equals(Constant.PrinterTYPE.SPRT)) {
                try {
                    printer = new Printer_SPRT(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (str.equals(Constant.PrinterTYPE.QR)) {
                try {
                    printer = new Printer_QR(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (str.equals(Constant.PrinterTYPE.XBY)) {
                try {
                    printer = new Printer_XBY(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (str.equals(Constant.PrinterTYPE.SHIYIN)) {
                try {
                    printer = new Printer_SHIYIN(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return printer;
    }

}

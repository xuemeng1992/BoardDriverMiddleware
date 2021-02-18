package com.rocktech.boarddriver.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.rocktech.boarddriver.bean.FingerprintBean;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public final static String TAG = "RocktechBoardDriver";

    public static String getBoxAddr(String num) {
        String tmp = num.substring(0, 1);
        String resultStr = "";
        switch (tmp) {
            case "Z":
                resultStr = "板Id 0";
                break;
            case "A":
                resultStr = "板Id 1";
                break;
            case "B":
                resultStr = "板Id 2";
                break;
            case "C":
                resultStr = "板Id 3";
                break;
            case "D":
                resultStr = "板Id 4";
                break;
            case "E":
                resultStr = "板Id 5";
                break;
            case "F":
                resultStr = "板Id 6";
                break;
            case "G":
                resultStr = "板Id 7";
                break;
            case "H":
                resultStr = "板Id 8";
                break;
            case "I":
                resultStr = "板Id 9";
                break;
            case "J":
                resultStr = "板Id 10";
                break;
            case "K":
                resultStr = "板Id 11";
                break;
            case "L":
                resultStr = "板Id 12";
                break;
            default:
                break;
        }
        return resultStr;
    }

    public static byte calculateBoard(String string) {
        String str = string.substring(0, 1);
        if (str.equals("Z")) {
            return 0x00;
        } else if (str.equals("A")) {
            return 0x01;
        } else if (str.equals("B")) {
            return 0x02;
        } else if (str.equals("C")) {
            return 0x03;
        } else if (str.equals("D")) {
            return 0x04;
        } else if (str.equals("E")) {
            return 0x05;
        } else if (str.equals("F")) {
            return 0x06;
        } else if (str.equals("G")) {
            return 0x07;
        } else if (str.equals("H")) {
            return 0x08;
        } else if (str.equals("I")) {
            return 0x09;
        } else if (str.equals("J")) {
            return 0x0a;
        } else if (str.equals("K")) {
            return 0x0b;
        } else if (str.equals("L")) {
            return 0x0c;
        } else {
            return 0x0f;
        }
    }

    public static byte sudiyiBoardId(String string) {
        String str = string.substring(0, 1);
        if (str.equals("Z")) {
            return 0x01;
        } else if (str.equals("A")) {
            return 0x02;
        } else if (str.equals("B")) {
            return 0x03;
        } else if (str.equals("C")) {
            return 0x04;
        } else if (str.equals("D")) {
            return 0x05;
        } else if (str.equals("E")) {
            return 0x06;
        } else if (str.equals("F")) {
            return 0x07;
        } else if (str.equals("G")) {
            return 0x08;
        } else if (str.equals("H")) {
            return 0x09;
        } else if (str.equals("I")) {
            return 0x0a;
        } else if (str.equals("J")) {
            return 0x0b;
        } else if (str.equals("K")) {
            return 0x0c;
        } else if (str.equals("L")) {
            return 0x0d;
        } else {
            return 0x0e;
        }
    }

    public static byte calculateLock(String string) {
        if (string.substring(0, 1).equalsIgnoreCase("Z")) {//主柜
            if (Integer.parseInt(string.substring(1, 3)) == 99) {
                return 8;
            }
            //18路双开，绵阳要求12路锁控板去掉CH13控制门磁功能，改为开锁
            else if (Integer.parseInt(string.substring(1, 3)) == 0) {
                return 0x0d;
            } else {
                return (byte) Integer.parseInt(string.substring(1, 3));
            }
        } else {
            return (byte) Integer.parseInt(string.substring(1, 3));
            /**
             * 绵阳2路锁编号01、02
             */
        }
    }

    public static String numberToLetter(int num) {
        if (num <= 0) {
            return "Z";
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter = ((char) (num % 26 + (int) 'A')) + letter;
            num = (int) ((num - num % 26) / 26);
        } while (num > 0);

        return letter;
    }


    public static String byteToHexStr(int length, byte[] msg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = msg[i];
            String str = Integer.toHexString(0xFF & b);
            if (str.length() == 1) {
                str = "0" + str;
            } else {
                str = "" + str;
            }

            sb.append(str);
        }
        return sb.toString();
    }

    public static List<FingerprintBean> rmRepeatFingerprint(List<FingerprintBean> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getId().equals(list.get(i).getId())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static String analysisId(String id) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(id);
        String realId = m.replaceAll("").trim();
        return realId;
    }

    public static String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String appVersion = info.versionName;
            return appVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "很抱歉，该应用未定义版本号";
        }
    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + "，");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static void deleteOverdueLog() {
        //开副柜灯的时候，检查log文件，超过30天的删除
        Log.d(TAG, "删除过期Log");
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File sdCard = Environment.getExternalStorageDirectory();
                    File logDir = new File(sdCard, "rocktech_log");
                    File crashDir = new File(sdCard, "rocktech_crash");
                    //删除过期的串口log
                    deleteExpirationFile(logDir.getPath());
                    //删除过期的crash信息
                    deleteExpirationFile(crashDir.getPath());
                    //如果老版本路径下的carsh信息，删除
                    File crashLog = new File(logDir.getPath() + "/crash");
                    if (crashLog.exists()) {
                        if (crashLog.isDirectory()) {
                            File[] files = crashLog.listFiles();
                            for (File f : files) {
                                f.delete();
                            }
                        }
                        crashLog.delete();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件排序
     *
     * @param fs
     */
    public static File[] orderByDate(File[] fs) {
        Arrays.sort(fs, new Comparator<File>() {
            public int compare(File file, File newFile) {
                if (file.lastModified() < newFile.lastModified()) {
                    return 1;
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    return -1;
                }
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
        return fs;
    }

    /*
     * 只保留30天的log信息
     */
    private static void deleteExpirationFile(String str) {
        File dir = new File(str);
        File[] files = dir.listFiles();
        Log.d(TAG, "getFileSize(dir) == " + getFileSize(dir));
        /**大于500M的删除*/
        /*if (getFileSize(dir) >= 1024) {
            while (true) {
                orderByDate(files);
                files[0].delete();
                if (getFileSize(dir) < 800) {
                    break;
                }
            }
        }*/
        while (true) {
            if (getFileSize(dir) < 500) {
                Log.d(TAG, "log小于500M，跳出循环。开始日期审核");
                break;
            }
            File[] tmpArr = orderByDate(files);
            for (File tmpFile : tmpArr) {
                System.out.println(tmpFile.getName() + " : " + tmpFile.lastModified());
            }
            System.out.println("tmpArr first == " + tmpArr[0].getName() + " : " + tmpArr[0].lastModified());
            tmpArr[0].delete();
            Log.d(TAG, "Each getFileSize(dir) == " + getFileSize(dir));
        }
        /**超过30天的log删除*/
        long expirationTime = 30 * 24 * 3600 * 1000L;//原始文件时间
        long nowTime = System.currentTimeMillis();//当前时间
        if (dir.isDirectory()) {
            for (File f : files) {
                if (f.exists()) {
                    long time = f.lastModified();
                    Log.d(TAG, "old time == " + time);
                    Log.d(TAG, "now time == " + nowTime);
                    Log.d(TAG, "now - old == " + (nowTime - time));
                    Log.d(TAG, "expirationTime time == " + expirationTime);
                    if (nowTime - time >= expirationTime) {
                        Log.d(TAG, "有Log过期");
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @return MB
     */
    private static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
            return size / 1048576;//1024x1024
        } else {
            return 0;
        }
    }

}

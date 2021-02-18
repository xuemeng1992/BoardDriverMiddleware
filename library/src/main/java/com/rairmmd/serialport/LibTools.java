package com.rairmmd.serialport;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LibTools {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd" + " " + "HH:mm:ss:SSS",
            Locale.getDefault());
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static void byteToHex(String string, int length, byte[] msg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = msg[i];
            String str = Integer.toHexString(0xFF & b);
            if (str.length() == 1) {
                // str = " 0x0" + str;
                str = " 0" + str;
            } else {
                // str = " 0x" + str;
                str = " " + str;
            }
            sb.append(str);
        }
        //保存串口收发的所有数据
        writeLog("BoardDriverMiddleware：" + string + sb.toString());
    }

    public static void writeBehaviorLog(String behavior) {
        writeLog("BoardDriverMiddleware：" + behavior);
    }

    public static void writeLog(String string) {
        Log.e("BoardDriverMiddleware", string);
        String time = formatter.format(new Date());
        String fileName = time + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard, "rocktech_log");
            try {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir.getPath() + File.separator + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write((dateFormat.format(new Date(System.currentTimeMillis())) + ":" + string + "\n").getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

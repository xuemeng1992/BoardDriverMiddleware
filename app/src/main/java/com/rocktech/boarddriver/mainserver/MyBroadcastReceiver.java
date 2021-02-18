package com.rocktech.boarddriver.mainserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.tools.Action;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 主柜开/关灯
        if (Action.LAMP_MAIN_ONOFF.equals(action)) {
            int i = intent.getExtras().getInt("onoff");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.LAMP_MAIN_ONOFF_S);
            in.putExtra("onoff", i);
            context.startService(in);
        }
        // 副柜开/关灯
        if (Action.LAMP_ONOFF.equals(action)) {
            int i = intent.getExtras().getInt("onoff");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.LAMP_ONOFF_S);
            in.putExtra("onoff", i);
            context.startService(in);
        }
        // 初始/ 关闭 扫描
        if (Action.BARCODESCANNER_ONOFF.equals(action)) {
            int i = intent.getExtras().getInt("onoff");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_ONOFF_S);
            in.putExtra("onoff", i);
            context.startService(in);
        }
        // 开始扫描
        if (Action.BARCODESCANNER_SCAN.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_SCAN_S);
            context.startService(in);
        }
        // 取消扫描
        if (Action.BARCODESCANNER_CANCEL.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.BARCODESCANNER_CANCEL_S);
            context.startService(in);
        }
        // 打开/关闭 打印机
        if (Action.PRINTER_ONOFF.equals(action)) {
            int i = intent.getExtras().getInt("onoff");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.PRINTER_ONOFF_S);
            in.putExtra("onoff", i);
            context.startService(in);
        }
        // 获取纸张尺寸
        if (Action.PRINTER_SUPPORTSIZE.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.PRINTER_SUPPORTSIZE_S);
            context.startService(in);
        }
        // 查询是否有纸
        if (Action.PRINTER_HASPAPER.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.PRINTER_HASPAPER_S);
            context.startService(in);
        }
        // 查询是否纸将近
        if (Action.PRINTER_NEEDMORE.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.PRINTER_NEEDMORE_S);
            context.startService(in);
        }
        // 打印
        if (Action.PRINTER_PRINT.equals(action)) {
            if (ConfigureTools.getPrinter(context).equals(Constant.PrinterTYPE.SPRT)) {
                String string = intent.getExtras().getString("pstr");
                Intent in = new Intent(context, MainServer.class);
                in.putExtra("action", Action.PRINTER_PRINT_S);
                in.putExtra("pstr", string);
                context.startService(in);
            } else {
                String string = intent.getExtras().getString("json");
                Intent in = new Intent(context, MainServer.class);
                in.putExtra("action", Action.PRINTER_PRINT_S);
                in.putExtra("json", string);
                context.startService(in);
            }
        }
        // 打开柜门
        if (Action.IOCONTROLLER_OPEN.equals(action)) {
            String lockNum = intent.getExtras().getString("boxid");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_OPEN_S);
            in.putExtra("boxid", lockNum);
            context.startService(in);
        }
        // 打开多个柜门
        if (Action.IOCONTROLLER_BATCH_OPEN.equals(action)) {
            String[] batchBoxId = intent.getExtras().getStringArray("batchboxid");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_BATCH_OPEN_S);
            in.putExtra("batchboxid", batchBoxId);
            context.startService(in);
        }
        // 查询单个副柜多个格口状态
        if (Action.IOCONTROLLER_SIMPLEBATCHQUERY.equals(action)) {
            String[] batchBoxId = intent.getExtras().getStringArray("batchboxid");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_SIMPLEBATCHQUERY_S);
            in.putExtra("batchboxid", batchBoxId);
            context.startService(in);
        }
        //查询已打开格口统计
        if (Action.IOCONTROLLER_QUERYALL.equals(action)) {
            String boxCount = intent.getExtras().getString("boxCount");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_QUERYALL_S);
            in.putExtra("boxCount", boxCount);
            context.startService(in);
        }
        // 查询柜门
        if (Action.IOCONTROLLER_QUERY.equals(action)) {
            String lockNum = intent.getExtras().getString("boxid");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.IOCONTROLLER_QUERY_S);
            in.putExtra("boxid", lockNum);
            context.startService(in);
        }
        // 查询资产编码
        if (Action.BOXINFO_QUERY.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.BOXINFO_QUERY_S);
            context.startService(in);
        }
        // 写入资产编码
        if (Action.BOXINFO_WRITE.equals(action)) {
            String[] assetCodeArrays = intent.getStringArrayExtra("array.assetCode");
            String[] boxIdArrays = intent.getStringArrayExtra("array.boxId");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.BOXINFO_WRITE_S);
            in.putExtra("array.assetCode", assetCodeArrays);
            in.putExtra("array.boxId", boxIdArrays);
            context.startService(in);
        }
        // 中间层程序配置属性表
        if (Action.CFGTABLE_QUERY.equals(action)) {
            Intent intent2 = new Intent("android.intent.action.hal.cfgtable.result");
            intent2.putExtra("halVersion", "V2.7.5");
            intent2.putExtra("vendorName", "陕西瑞迅电子信息技术有限公司");
            intent2.putExtra("vendorAppVersion", "20200619");
            intent2.putExtra("vendorAppName", "通用中间层软件");
            context.sendBroadcast(intent2);
        }
        // 柜体事件监控
        if (Action.GUARD_ONOFF.equals(action)) {
            int i = intent.getExtras().getInt("onoff");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.GUARD_ONOFF_S);
            in.putExtra("onoff", i);
            context.startService(in);
        }
        // 重启系统
        if (Action.APP_REBOOT.equals(action)) {
            long time = intent.getExtras().getLong("delayMs");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.APP_REBOOT_S);
            in.putExtra("delayMs", time);
            context.startService(in);
        }
        //查询系统运行属性、温度采集
        if (Action.SYS_QUERY.equals(action)) {
            String[] jsonResult = intent.getExtras().getStringArray("attributes");
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.SYS_QUERY_S);
            in.putExtra("attributes", jsonResult[0]);
            context.startService(in);
        }
        if (Action.QUERY_METER.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            in.putExtra("action", Action.QUERY_METER_S);
            context.startService(in);
        }
        //单柜锁状态查询
        if (Action.IOCONTROLLER_QUERYSINGLECABINET.equals(action)) {
            Intent in = new Intent(context, MainServer.class);
            String[] batchBoxId = intent.getExtras().getStringArray("batchboxid");
            in.putExtra("action", Action.IOCONTROLLER_QUERYSINGLECABINET_S);
            in.putExtra("batchboxid", batchBoxId);
            context.startService(in);
        }
    }
}

// IRemoteAIDL.aidl
package com.fcbox.locker.driver;

// Declare any non-default types here with import statements

interface IRemoteAIDL {
    //开关主柜灯，onoff：1，打开；0，关闭
        void onoffMainLamp(int onoff);

        //开关辅柜灯，onoff：1，打开；0，关闭
        void onoffAssistantLamp(int onoff);

        //初始化/关闭扫描，onoff：1，打开；0，关闭
        void onoffScanner(int onoff);

        //开始扫描
        void startScanner();
        //取消扫描
        void cancelScanner();

        //打开/关闭打印机，onoff：1，打开；0，关闭
        void onoffPrinter(int onoff);

        //获取打印机纸张尺寸
        //通过广播android.intent.action.hal.printer.supportsize.result返回数据
        //int:papersize：0，3寸；1，4寸
        void getPrinterSize();

        //获取打印机是否有纸
        //通过广播android.intent.action.hal.printer.result.haspaper返回数据
        //boolean:haspaper：true，有纸；false，无纸
        void printerHasPager();

        //获取打印机是否纸将尽
        //通过广播android.intent.action.hal.printer.result.needmore返回数据
        //boolean:needmore：true，纸不多；false，纸充足
        void printerMorePager();

        //打印
        //str：打印内容
        void printer(String str);

        //打开多个格口
        //boxIds格口编号数组
        //通过广播android.intent.action.hal.iocontroller.batchopen.result返回数据
        //String[]:batchboxid,打开格口的数组；boolean[]:opened,对应格口开门结果
        void batchOpenDoor(in String[] boxIds);

        // 打开单个格口
        //
        // boxId 格口编号
        // msgClientId 请求消息序列号
        // msgTime 请求时间戳
        //
        //通过广播android.intent.action.hal.iocontroller.querydata返回数据
        //String:boxid,打开格口；boolean:isopened,对应格口开门结果
        //
        // return true表示开门成功,false表示开门失败
        boolean openSingleDoor(String boxId);

        //查询单个副柜多个格口状态，与batchOpenDoor接口基本相同
        //boxIds格口编号数组
        //通过广播android.intent.action.hal.iocontroller.batchopen.result返回数据
        //String[]:batchboxid,打开格口的数组；boolean[]:opened,对应格口开门结果
        void simpleBatchQuery(in String[] boxIds);

        //查询已打开格口统计
        //boxCount内容示例：Z01A01B01，其中字母代表柜子，每个柜子的字母有一个就行，会自动检索柜子中所有格口
        //通过广播android.intent.action.hal.iocontroller.queryAllData返回数据
        //String[]:openedBoxes,打开格口的数组
        void queryAll(String boxCount);

        //查询单个格口
        //boxId：格口编号
        //通过广播android.intent.action.hal.iocontroller.querydata返回数据
        //String:boxid,打开格口的编号；String:isopened,对应格口开门结果
        void querySingleDoor(String boxId);

        //查询资产编码
        //通过广播android.intent.action.hal.boxinfo.result返回数据
        //String[]:array.boxId,柜子编号集合；String[]:array.assetCode,柜子对应的资产编码数据集合
        void queryAssetCode();

        //写入资产编码
        //无结果返回
        //boxIdArray,柜子编号集合；assetCodeArray,柜子对应的资产编码数据集合
        void setAssetCode(in String[] boxIdArray, in String[] assetCodeArray);

        //获取中间层程序配置属性
        //通过广播android.intent.action.hal.cfgtable.result返回数据
        //String:halVersion,版本名称；String:vendorName,开发者；String:vendorAppVersion,版本；String:vendorAppName,APP名称
        void getMidModifyInfo();

        //重启
        //无返回值
        void reboot(long delayMs);

        //获取主板温度
        //通过广播android.intent.action.hal.sys.result返回数据
        //返回JSONArray格式数据
        //[{"key": "temp.mainboard","value":""},{"key": "anotherKeyName","value":"anotherValue"}]"
        void getMainBoardTemp();
}

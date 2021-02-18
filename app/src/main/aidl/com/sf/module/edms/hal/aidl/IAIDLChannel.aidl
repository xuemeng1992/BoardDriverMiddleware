package com.sf.module.edms.hal.aidl;

// 辅助通信方式,用于补充广播通信方式
interface IAIDLChannel {
    // 打开格口
    //
    // boxId 格口编号
    // msgClientId 请求消息序列号
    // msgTime 请求时间戳
    //
    // return true表示开门成功,false表示开门失败
    boolean openDoor(String boxId, String msgClientId, String msgTime);
}
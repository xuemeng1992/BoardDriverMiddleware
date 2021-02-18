package com.rairmmd.serialport;

/**
 * @author Rair
 * @date 2017/10/25
 * <p>
 * desc:数据接收回调
 */

public interface OnDataReceiverListener {

    int onDataReceiver(byte[] buffer, int size, int handle);

    void onSendComplete(byte[] buffer, int size, int handle);
}

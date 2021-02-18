package com.rairmmd.serialport;

public interface CommonDataReceiverListener {
    void onDataReceiver(byte[] buffer, int size);
}

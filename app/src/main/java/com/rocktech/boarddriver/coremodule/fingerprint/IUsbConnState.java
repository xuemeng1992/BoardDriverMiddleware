package com.rocktech.boarddriver.coremodule.fingerprint;

public interface IUsbConnState {
    void onUsbConnected();

	void onUsbPermissionDenied();

	void onDeviceNotFound();
}

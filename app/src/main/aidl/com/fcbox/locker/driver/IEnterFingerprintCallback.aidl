// IEnterFingerprintCallback.aidl
package com.fcbox.locker.driver;

// Declare any non-default types here with import statements

interface IEnterFingerprintCallback {

    void onComplete(int resultCode, String errorMsg, String fingerprintInfo);

    void onNext(int resultCode);
}
// IComparisonFingerprintCallback.aidl
package com.fcbox.locker.driver;

// Declare any non-default types here with import statements

interface IComparisonFingerprintCallback {

    void onComplete(int resultCode, String errorMsg, String id, int fraction);
}

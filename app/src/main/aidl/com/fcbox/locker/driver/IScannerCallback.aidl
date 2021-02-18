// IScannerCallback.aidl
package com.fcbox.locker.driver;

// Declare any non-default types here with import statements

interface IScannerCallback {

    void onComplete(int resultCode, String errorMsg, String result);
}

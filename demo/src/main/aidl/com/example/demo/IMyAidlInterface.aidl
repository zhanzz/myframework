// IMyAidlInterface.aidl
package com.example.demo;

// Declare any non-default types here with import statements
import com.example.demo.IMyCallbackListener;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    void testMethod(String str);
    //can`t use chain resaon
    void regisListener(IMyCallbackListener listener);
    void unregisListener(IMyCallbackListener listener);
}

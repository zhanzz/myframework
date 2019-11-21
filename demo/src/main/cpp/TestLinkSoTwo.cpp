//
// Created by zhangzhiqiang on 2019/10/26.
//
#include "com_example_demo_incremental_updating_TestLinkSoTwo.h"

JNIEXPORT jstring JNICALL Java_com_example_demo_incremental_1updating_TestLinkSoTwo_getThirdString
        (JNIEnv * env, jclass){
    return env->NewStringUTF("我是TestLinkSoTwo");
}
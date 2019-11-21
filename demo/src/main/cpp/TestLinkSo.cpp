//
// Created by zhangzhiqiang on 2019/10/26.
//
#include "com_example_demo_incremental_updating_TestLinkSo.h"
#include <my.h>
#include <malloc.h>

JNIEXPORT jstring JNICALL Java_com_example_demo_incremental_1updating_TestLinkSo_getThirdString
(JNIEnv * env, jclass){
    char * name = getString();
    jstring  str = env->NewStringUTF(name);
    free(name);
    return str;
}

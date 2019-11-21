//
// Created by zhangzhiqiang on 2019/10/18.
//
#include "../header/err.h"
#include <stdio.h>
#include <stdarg.h>

void errx(int, const char * format, const char * content){
    printf(format,content);
}

void err(int num, const char * format,...){
    va_list vl;     //va_list指针，用于va_start取可变参数，为char*
    va_start(vl,format);
    printf(format,vl);
    va_end(vl);//结束标志
}

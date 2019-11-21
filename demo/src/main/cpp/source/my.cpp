//
// Created by zhangzhiqiang on 2019/10/26.
//
#include "../header/my.h"
#include <string.h>
#include <malloc.h>

char * getString(){
    char * name;
    name = (char *)malloc(10);
    strcpy(name,"张汉青");
    return name;
}

package com.example.demo.pagelist

enum class LoadingState {
    Normal, //
    Loading, //加载中
    Failed, //出错，重试
    NotMore,//没有更多数据
    Empty//空视图
}
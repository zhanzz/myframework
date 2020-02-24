package com.example.jetpack

/**
 * @author zhangzhiqiang
 * @date 2020/2/4.
 * description：
 */
object DataProviderManager {
    fun registerDataProvider(provider: Int) {
        // ……
    }

    val allDataProviders: Collection<Int> = ArrayList<Int>()
}

class MyClass {
    companion object {
        fun create(): MyClass = MyClass()
    }

    val instance = create()
}

class MyClass1 {
    companion object Named { }
}

val x = MyClass1

fun test(){
    val instance = MyClass.create()
}

inline class Name(val s: String) {
    val length: Int
        get() = s.length

    fun greet() {
        println("Hello, $s")
    }
}

interface testGet{
    public var value:Int
}

class TestGetSome :testGet{
    override var value: Int = 7
       set(value) {
           println(value)
       }
}
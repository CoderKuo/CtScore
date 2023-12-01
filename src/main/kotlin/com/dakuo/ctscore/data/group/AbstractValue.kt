package com.dakuo.ctscore.data.group

abstract class AbstractValue<T>(var origin:String): GroupValue<T> {

    fun get(): String {
        return origin
    }

    fun set(str:String):String{
        origin = str
        return origin
    }


}
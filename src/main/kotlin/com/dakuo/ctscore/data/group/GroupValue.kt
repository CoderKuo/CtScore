package com.dakuo.ctscore.data.group

interface GroupValue<T> {

    abstract fun getValue():T
    abstract fun saveToString():String

}
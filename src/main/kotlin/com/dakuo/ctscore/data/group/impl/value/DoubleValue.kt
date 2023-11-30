package com.dakuo.ctscore.data.group.impl.value

import com.dakuo.ctscore.data.group.AbstractValue

class DoubleValue(origin:String): AbstractValue<Double>(origin) {
    override fun getValue(): Double {
        return origin.toDouble() ?: 0.0
    }


    override fun saveToString(): String {
        return getValue().toString()
    }



}
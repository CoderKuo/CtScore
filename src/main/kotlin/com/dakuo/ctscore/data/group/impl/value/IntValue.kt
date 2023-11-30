package com.dakuo.ctscore.data.group.impl.value

import com.dakuo.ctscore.data.group.AbstractValue

class IntValue(origin:String) : AbstractValue<Int>(origin) {

    override fun getValue(): Int {
        return origin.toInt()
    }

    override fun saveToString(): String {
        return getValue().toString()
    }


}
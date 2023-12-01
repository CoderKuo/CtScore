package com.dakuo.ctscore.data.group.impl.value

import com.dakuo.ctscore.data.group.AbstractValue

class BooleanValue(origin:String):AbstractValue<Boolean>(origin) {




    override fun getValue(): Boolean {
        return origin.toBoolean()
    }

    override fun saveToString(): String {
        return getValue().toString()
    }
}
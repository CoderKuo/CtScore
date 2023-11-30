package com.dakuo.ctscore.data.group.impl.value

import com.dakuo.ctscore.data.group.AbstractValue

class StringValue(origin:String): AbstractValue<String>(origin) {
    override fun getValue(): String {
        return origin
    }

    override fun saveToString():String {
        return origin
    }


}
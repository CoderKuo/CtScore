package com.dakuo.ctscore.data.group.impl.value

import com.alibaba.fastjson.JSON
import com.dakuo.ctscore.data.group.AbstractValue

class JsonValue(origin:String):AbstractValue<JSON>(origin) {

    override fun getValue(): JSON {
        return JSON.parseObject(origin)
    }

    override fun saveToString(): String {
        return getValue().toJSONString()
    }


}
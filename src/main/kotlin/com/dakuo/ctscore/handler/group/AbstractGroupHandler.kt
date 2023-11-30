package com.dakuo.ctscore.handler.group

import java.util.UUID

abstract class AbstractGroupHandler {

    abstract fun get(name:String,saveKey:String):Any?


}
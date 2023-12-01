package com.dakuo.ctscore.api

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.group.GroupValue

object CtScoreGroupAPI {


    fun balance(name:String,saveKey:String):GroupValue<*>{
        return CtScore.groupHandle.balance(name, saveKey)
    }

}
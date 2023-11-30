package com.dakuo.ctscore.data.group

import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object GroupValueManager {

    val values = ConcurrentHashMap<String,Class<GroupValue<*>>>()

    fun register(name:Array<String>,group: Class<GroupValue<*>>){
        name.forEach {
            register(it,group)
        }
    }
    fun register(name:String,group: Class<GroupValue<*>>){
        values.put(name,group)
        info("组积分值对象 $name 已加载")
    }

    @Awake(LifeCycle.ENABLE)
    fun autoRegister(){
        runningClasses.filter {
            GroupValue::class.java.isAssignableFrom(it)
        }.forEach {
            val name:Array<String> = it.getAnnotation(GroupName::class.java)?.name ?: arrayOf( it.simpleName)
            register(name,it as Class<GroupValue<*>>)
        }
    }

    fun getGroupValueObject(name: String):Class<GroupValue<*>>{
        return values.get(name) ?: error("没有找到名为 $name 的值对象")
    }

    fun newInstance(){

    }


}
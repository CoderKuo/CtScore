package com.dakuo.ctscore.data.group

import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import java.util.concurrent.ConcurrentHashMap

object GroupObjectManager {

    val groups = ConcurrentHashMap<String,Class<Group<*>>>()

    fun register(name:Array<String>,group: Class<Group<*>>){
        name.forEach {
            register(it,group)
        }
    }
    fun register(name:String,group: Class<Group<*>>){
        groups.put(name,group)
        info("组积分对象 $name 已加载")
    }

    @Awake(LifeCycle.ENABLE)
    fun autoRegister(){
        runningClasses.filter {
            Group::class.java.isAssignableFrom(it)
        }.forEach {
            val name:Array<String> = it.getAnnotation(GroupName::class.java)?.name ?: arrayOf( it.simpleName)
            register(name,it as Class<Group<*>>)
        }
    }

    fun getGroupObject(name: String):Class<Group<*>>{
        return groups.get(name) ?: error("没有找到名为 $name 的组对象")
    }

    fun newInstance(){

    }



}
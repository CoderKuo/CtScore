package com.dakuo.ctscore.data.group

import com.dakuo.ctscore.data.group.impl.value.HideFunction
import com.dakuo.ctscore.data.group.impl.value.ValueFunction
import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import java.lang.invoke.MethodHandle
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object GroupValueManager {

    val values = ConcurrentHashMap<String,Class<GroupValue<*>>>()

    val methods = ConcurrentHashMap<Class<GroupValue<*>>,ConcurrentHashMap<String,Method>>()


    fun invokeFunction(functionName: String, clazz: Class<GroupValue<*>>,obj:GroupValue<*> ,vararg args:String):Any?{
        return getFunction(functionName,clazz)?.invoke(obj, args)
    }

    fun getAllFunctions(clazz:Class<GroupValue<*>>):List<String>{
        return clazz.methods.filter { it.getAnnotation(HideFunction::class.java) == null }.map {
            it.name
        }.toMutableList().apply {
            this.addAll(methods[clazz]?.map { it.key } ?: emptyList())
        }
    }

    fun getFunction(functionName:String,clazz:Class<GroupValue<*>>):Method?{
        var method = clazz.methods.find {
            it.name == functionName || it.getAnnotation(ValueFunction::class.java).alias.contains(functionName)
        }
        if (method == null){
            method = methods.get(clazz)?.get(functionName)
        }
        return method
    }

    fun registerFunction(functionName: String,name:String,function:Method){
        methods.getOrPut(getGroupValueObject(name)){
            ConcurrentHashMap<String, Method>()
        }.put(functionName,function)
    }



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
            GroupValue::class.java.isAssignableFrom(it) && it != AbstractGroup::class.java
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
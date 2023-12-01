package com.dakuo.ctscore.handler.group

import com.dakuo.ctscore.data.group.GroupScoreManager
import com.dakuo.ctscore.data.group.GroupValue
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractGroupHandler {

    /**
     * 缓存
     * key: name and saveKey
     * value: GroupValue
     */
    val cache = ConcurrentHashMap<Pair<String,String>,GroupValue<*>>()

    protected abstract fun get(name:String, saveKey:String):GroupValue<*>?

    protected abstract fun insert(name:String,saveKey: String,value: GroupValue<*>)

    fun balance(name:String,saveKey: String):GroupValue<*>{
        return if (cache.contains(Pair(name,saveKey))){
            cache[Pair(name,saveKey)]!!
        }else{
            val getResult = get(name, saveKey)
            return if (getResult == null){
                val config = GroupScoreManager.getFromId(name) ?: error("没有找到id名为name的组积分")
                insert(name,saveKey,GroupScoreManager.getDefaultValue(config))
                return balance(name,saveKey)
            }else{
                cache[Pair(name,saveKey)] = getResult
                getResult
            }
        }
    }

    fun set(name:String,saveKey: String,value:GroupValue<*>){
        if (cache.contains(Pair(name,saveKey))){
            cache[Pair(name,saveKey)] = value
        }else{
            balance(name, saveKey)
            set(name, saveKey, value)
        }
    }




}
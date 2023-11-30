package com.dakuo.ctscore.data.group

import com.dakuo.ctscore.CtScore
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.util.getStringColored

object GroupScoreManager {

    val groupScoreInConfig = mutableListOf<GroupScoreConfig>()

    @Awake(LifeCycle.ENABLE)
    fun loadFromConfig(){
        groupScoreInConfig.clear()
        CtScore.config.getConfigurationSection("Group_Score")?.apply {
            this.getKeys(false).forEach {
                val section = this.getConfigurationSection(it)!!
                val name = section.getStringColored("name") ?: it
                val group = section.getString("group")?.let {
                    return@let GroupObjectManager.getGroupObject(it)
                }
                val save = section.getBoolean("save")
                val value = section.getString("value")?.let {
                    return@let GroupValueManager.getGroupValueObject(it)
                }
                val default = section.get("default")
                groupScoreInConfig.add(GroupScoreConfig(it,name,group!!,save,value!!,default))
            }
        }
    }


}

data class GroupScoreConfig(val id:String,val name:String,val group:Class<Group<*>>,val save:Boolean,val value:Class<GroupValue<*>>,val default:Any?)
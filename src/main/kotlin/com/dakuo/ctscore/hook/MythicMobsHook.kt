package com.dakuo.ctscore.hook

import com.dakuo.ctscore.api.CtScoreAPI
import ink.ptms.um.Mob
import ink.ptms.um.event.MobDeathEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.format
import taboolib.module.chat.colored
import kotlin.random.Random

object MythicMobsHook {

    @SubscribeEvent
    fun monitorMobsDeath(event:MobDeathEvent){
        event.killer?.let { execute(event.mob, it) }
    }

    fun execute(mob:Mob,killer:LivingEntity){
        val config = mob.config
        val ctScoreSection = config.getConfigurationSection("CtScore")
        val option = ctScoreSection?.let {
            val mapList = ctScoreSection.getMapList("score")
            mapList.map {
                val type = it.get("type")
                val number = it.get("number")
                val lang = it.get("lang")
                MobOption(type.toString(),number.toString(),lang.toString())
            }
        }

        option?.forEach {
            if (killer !is Player){
                return
            }
            val player = killer as Player

            val number:Double = if (it.number.contains("-")){
                Random.nextDouble(it.number.split("-")[0].toDouble(),it.number.split("-")[1].toDouble()).format(2)
            }else{
                it.number.toDouble()
            }

            CtScoreAPI.deposit(it.type,player.name,number)
            it.lang.colored().replace("%mob%",mob.displayName).replace("%number%",number.toString()).apply {
                player.sendMessage(this)
            }
        }
    }

}

data class MobOption(val type:String,val number:String,val lang:String)
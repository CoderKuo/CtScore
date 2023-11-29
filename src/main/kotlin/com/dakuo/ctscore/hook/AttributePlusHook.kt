package com.dakuo.ctscore.hook

import ink.ptms.um.Mythic
import org.bukkit.Bukkit
import org.serverct.ersha.api.event.AttrEntityAttackEvent
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object AttributePlusHook {

    val isEnable: Boolean
        get() = Bukkit.getPluginManager().isPluginEnabled("AttributePlus")

    init {
        if (isEnable){
            info("MythicMobs掉落积分AttributePlus兼容已加载")
        }
    }

    @SubscribeEvent(bind = "org.serverct.ersha.api.event.AttrEntityAttackEvent")
    fun monitorAttr(ope: OptionalEvent){
        val e = ope.get<AttrEntityAttackEvent>()

        Mythic.API.getMob(e.entity)?.let {
            if (e.attributeHandle.getDamage(e.entity) >= e.entity.health){
                MythicMobsHook.execute(it,e.attackerOrKiller)
            }
        }
    }

}
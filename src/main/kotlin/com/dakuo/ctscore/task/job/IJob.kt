package com.dakuo.ctscore.task.job

import org.bukkit.entity.Player
import org.quartz.JobExecutionContext
import taboolib.common.io.runningClasses
import taboolib.common.platform.function.onlinePlayers
import taboolib.platform.type.BukkitPlayer

abstract class IJob :org.quartz.Job{

    abstract val name:String
    abstract fun run(player:Player,data:List<String>)

    override fun execute(context: JobExecutionContext?) {
        onlinePlayers().forEach {
            run((it as BukkitPlayer).player,
                context?.jobDetail?.jobDataMap?.get("data") as List<String> ?: emptyList())
        }
    }

}
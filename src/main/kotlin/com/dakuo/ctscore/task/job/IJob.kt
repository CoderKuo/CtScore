package com.dakuo.ctscore.task.job

import com.dakuo.ctscore.CtScore
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.quartz.JobExecutionContext
import taboolib.common.io.getClasses
import taboolib.common.io.runningClasses
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submit
import taboolib.platform.type.BukkitPlayer
import java.util.*

abstract class IJob :org.quartz.Job{

     abstract val name:String
    abstract fun run(player:Player,data:List<String>)

    override fun execute(context: JobExecutionContext?) {
        val async = context?.jobDetail?.jobDataMap?.get("async") as? Boolean ?: false
        val data = context?.jobDetail?.jobDataMap?.get("data") as? List<String> ?: emptyList()
        val playerProcessor: (Any) -> Unit = { player ->
            run((player as BukkitPlayer).player, data)
        }

        if (async || !Bukkit.isPrimaryThread()) {
            submit {
                onlinePlayers().forEach(playerProcessor)
            }
        } else {
            onlinePlayers().forEach(playerProcessor)
        }
    }

    companion object{
        val classes = CtScore::class.java.protectionDomain.codeSource.location.getClasses()
        val jobs = LinkedList(classes.values).filter {
            IJob::class.java.isAssignableFrom(it) && it != IJob::class.java
        }.associate{
            val instance = (it as Class<IJob>).newInstance()
            Pair(instance.name,it)
        }
    }

}
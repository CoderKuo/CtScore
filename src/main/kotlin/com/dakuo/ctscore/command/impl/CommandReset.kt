package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.api.CtScoreAPI
import com.dakuo.ctscore.data.ScoreManager
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang

object CommandReset {
    val command = subCommand {
        dynamic {
            suggestion<ProxyCommandSender>{ _, _ ->
                Bukkit.getOfflinePlayers().map { it.name!! }
            }
            dynamic {
                suggestion<ProxyCommandSender>{ _, _ ->
                    ScoreManager.cache.map{it.id}
                }
                    execute<ProxyCommandSender>{ sender, context, argument ->
                        val scoreById = ScoreManager.getScoreById(argument)
                        if (scoreById != null) {
                            CtScoreAPI.reset(argument,context.argument(-1))
                            sender.sendLang("Reset",context.argument(-1),scoreById.name)
                        }else{
                            sender.sendLang("ScoreNull")
                        }
                    }
            }
        }
    }
}
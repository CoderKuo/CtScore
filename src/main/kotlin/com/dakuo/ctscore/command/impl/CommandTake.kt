package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.ScoreManager
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang

object CommandTake {
    val command = subCommand {
        dynamic {
            suggestion<ProxyCommandSender>{ _, _ ->
                Bukkit.getOfflinePlayers().map { it.name!! }
            }
            dynamic {
                suggestion<ProxyCommandSender>{ _, _ ->
                    ScoreManager.cache.map{it.id}
                }
                dynamic {
                    restrict<ProxyCommandSender>{ sender, context, argument ->
                        Coerce.asDouble(argument).isPresent
                    }
                    execute<ProxyCommandSender>{ sender, context, argument ->
                        val scoreById = ScoreManager.getScoreById(context.argument(-1))
                        if (scoreById != null) {
                            CtScore.handler.withdraw(scoreById, Bukkit.getOfflinePlayer(context.argument(-2)).uniqueId,argument.toDouble())
                            sender.sendLang("Take",context.argument(-2),scoreById.name,argument.toDouble())
                        }else{
                            sender.sendLang("ScoreNull")
                        }
                    }
                }
            }
        }
    }
}
package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.ScoreManager
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.platform.function.onlinePlayers
import taboolib.module.lang.sendLang

object CommandLook {
    val command = subCommand {
            dynamic(comment = "score") {
                suggestion<ProxyPlayer>{ _, _ ->
                    ScoreManager.cache.map { it.id }
                }
                execute<ProxyPlayer>{ sender, _, argument ->
                    val scoreById = ScoreManager.getScoreById(argument)
                    if (scoreById != null){
                        sender.sendLang("Look",scoreById.name,CtScore.handler.balance(scoreById, getProxyPlayer(sender.name)!!.uniqueId))
                    }else {
                        sender.sendLang("ScoreNull")
                    }
                }
                dynamic(comment = "player",permission = "ctscore.look.other") {
                    suggestion<ProxyCommandSender>{ _, _ ->
                        Bukkit.getOfflinePlayers().map { it.name!! }
                    }
                    execute<ProxyCommandSender>{ sender, context, argument ->
                        val scoreById = ScoreManager.getScoreById(context.argument(-1))
                        if (scoreById != null){
                            sender.sendLang("Look_2",argument,scoreById.name,CtScore.handler.balance(scoreById, getProxyPlayer(argument)!!.uniqueId))
                        }else {
                            sender.sendLang("ScoreNull")
                        }

                    }
                }
            }
    }
}
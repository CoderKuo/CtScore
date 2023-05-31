package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.api.CtScoreAPI
import com.dakuo.ctscore.data.ScoreManager
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

object CommandPay {
    val command = subCommand {
        dynamic {
            suggestion<ProxyPlayer>{ _, _ ->
                Bukkit.getOfflinePlayers().map { it.name!! }
            }
            dynamic {
                suggestion<ProxyCommandSender>{ _, _ ->
                    ScoreManager.cache.map{it.name}
                }
                dynamic {
                    restrict<ProxyPlayer>{ _, _, argument ->
                        Coerce.asDouble(argument).isPresent
                    }
                        execute<ProxyPlayer>{ sender, context, argument ->
                            val online = Bukkit.getOfflinePlayer(context.argument(-2)).isOnline
                            if (!online){
                                sender.sendMessage("玩家不在线")
                                return@execute
                            }
                            val scoreByName = ScoreManager.getScoreById(context.argument(-1))
                            scoreByName?.let {
                                if (!scoreByName.pay){
                                    sender.sendMessage("该积分不支持转账")
                                    return@execute
                                }
                                val balance =
                                    CtScore.handler.balance(it, sender.uniqueId)
                                if (balance.toDouble() < argument.toDouble()){
                                    sender.sendLang("Pay_False")
                                    return@execute
                                }else{
                                    CtScore.handler.withdraw(it, sender.uniqueId,argument.toDouble())
                                    CtScore.handler.deposit(it, Bukkit.getOfflinePlayer(context.argument(-2)).uniqueId,argument.toDouble())
                                    sender.sendLang("Pay_True",context.argument(-2),argument.toDouble(),it.name)
                                    Bukkit.getOfflinePlayer(context.argument(-2)).player?.sendLang("Pay_True_2",sender.name,argument.toDouble(),it.name)
                                }
                            }

                        }
                }
            }
        }
    }
}
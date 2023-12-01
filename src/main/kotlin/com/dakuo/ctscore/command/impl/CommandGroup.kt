package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.api.CtScoreGroupAPI
import com.dakuo.ctscore.data.group.GroupScoreConfig
import com.dakuo.ctscore.data.group.GroupScoreManager
import com.dakuo.ctscore.data.group.GroupValueManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.library.reflex.Reflex.Companion.invokeMethod

object CommandGroup {


    val group = subCommand {
        dynamic(comment = "组积分id") {
            suggest {
                GroupScoreManager.getAllGroupScoreId()
            }
            dynamic(comment = "存储标识") {

                dynamic(comment = "动作") {
                    suggestion<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender> ->
                        val id = context["组积分id"]
                        val groupClass = GroupScoreManager.getFromId(id) ?: run{
                            sender.sendMessage("没有找到id为 $id 的组积分")
                            error("没有找到id为 $id 的组积分")
                        }
                        GroupValueManager.getAllFunctions((groupClass as GroupScoreConfig).value)
                    }
                    dynamic(comment = "参数",optional = true) {
                        execute<ProxyCommandSender>{ sender, context, argument ->
                            val id = context["组积分id"]
                            val groupClass = GroupScoreManager.getFromId(id) ?: run{
                                sender.sendMessage("没有找到id为 $id 的组积分")
                                error("没有找到id为 $id 的组积分")
                            }
                            val valueObj = CtScoreGroupAPI.balance(context["组积分id"],context["存储标识"])
                            val result = GroupValueManager.invokeFunction(context["动作"],
                                (groupClass as GroupScoreConfig).value,
                                valueObj,argument)
                            sender.sendMessage(result.toString())
                        }
                    }
                    execute<ProxyCommandSender>{ sender, context, argument ->
                        val id = context["组积分id"]
                        val groupClass = GroupScoreManager.getFromId(id) ?: run{
                            sender.sendMessage("没有找到id为 $id 的组积分")
                            error("没有找到id为 $id 的组积分")
                        }
                        val valueObj = CtScoreGroupAPI.balance(context["组积分id"],context["存储标识"])
                        val result = GroupValueManager.invokeFunction(context["动作"],
                            (groupClass as GroupScoreConfig).value,
                            valueObj)
                        sender.sendMessage(result.toString())
                    }
                }
            }

            dynamic("动作") {
                suggest {
                    listOf("look")
                }
                execute<ProxyPlayer>{sender, context, argument ->
                    val id = context["组积分id"]
                    val groupClass = GroupScoreManager.getFromId(id) ?: run{
                        sender.sendMessage("没有找到id为 $id 的组积分")
                        error("没有找到id为 $id 的组积分")
                    }
                    val groupInstance = groupClass.group.newInstance()
                    val saveKey = groupInstance.invokeMethod<String>("getSaveKey",groupInstance.get(sender))
                    if (saveKey != null) {
                        val balance = CtScoreGroupAPI.balance(id, saveKey)
                        sender.sendMessage(balance.toString())
                        return@execute
                    }
                    sender.sendMessage("错误")
                }
            }
        }
    }


}
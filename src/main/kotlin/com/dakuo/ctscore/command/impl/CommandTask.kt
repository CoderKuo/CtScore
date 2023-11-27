package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.TimeUtils
import com.dakuo.ctscore.task.TaskManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.module.chat.Components
import taboolib.module.chat.colored

object CommandTask {

    val task = subCommand {
        execute<ProxyCommandSender>{ sender,_,_->
            val allTask = TaskManager.getAllTask()
            allTask.forEachIndexed { index,it->
                val status = TaskManager.getStatus(it)
                Components.text("§f[§a${index+1}§f] §a${status.description} §6状态: ${status.status.name}").append(
                    Components.text("§f[§a详细信息§f]").hoverText(buildList {
                        this.add("§7---------------------")
                        this.add("§a描述: ${status.description} §6状态: ${status.status.name}")
                        this.add("§b上次执行时间: ${if(status.previousFireTime == null) "未执行" else TimeUtils.formatDate(status.previousFireTime)}")
                        this.add("§e下次执行时间: ${TimeUtils.formatDate(status.nextFireTime)}")
                        this.add("§7---------------------")
                    })
                ).sendTo(sender)
            }
        }

        dynamic(comment = "操作") {
            suggest {
                listOf("pause","resume")
            }
            dynamic {
                suggest {
                    TaskManager.getAllTask().map { it.first.key.name }
                }
                execute<ProxyCommandSender>{ sender, context, argument ->
                    when(context["操作"]){
                        "pause"->{
                            val key = argument
                            TaskManager.pauseTask(key)
                            sender.sendMessage("§f[§6!§f] §b$key §c已暂停".colored())
                        }
                        "resume"->{
                            val key = argument
                            TaskManager.resumeTask(key)
                            sender.sendMessage("§f[§6!§f] §b$key §a已继续".colored())
                        }
                    }

                }
            }
        }
    }

}
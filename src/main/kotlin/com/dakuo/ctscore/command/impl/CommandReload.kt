package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.Score
import com.dakuo.ctscore.data.ScoreManager
import com.dakuo.ctscore.task.TaskManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submitAsync
import taboolib.module.lang.sendLang

object CommandReload {
    val command = subCommand {
        execute<ProxyCommandSender>{ sender, _, _ ->
            submitAsync {
                CtScore.config.reload()
                CtScore.init()
                CtScore.handler.save()
                TaskManager.stopAllTask()
                TaskManager.loadAllTask()
                sender.sendMessage("重载成功")
            }
        }

    }
}
package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.Score
import com.dakuo.ctscore.data.ScoreManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object CommandReload {
    val command = subCommand {
        execute<ProxyCommandSender>{ sender, _, _ ->
            CtScore.config.reload()
            CtScore.init()
            CtScore.handler.save()
            sender.sendMessage("重载成功")
        }

    }
}
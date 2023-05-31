package com.dakuo.ctscore.command.impl

import com.dakuo.ctscore.data.ShopManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers

object CommandShop {
    val command = subCommand {
        dynamic {
            suggestion<ProxyPlayer>{ _, _ ->
                ShopManager.list.map { it.name }
            }
            execute<ProxyPlayer>{ sender, context, argument ->
                ShopManager.buy(sender,argument)
            }
        }
    }
}
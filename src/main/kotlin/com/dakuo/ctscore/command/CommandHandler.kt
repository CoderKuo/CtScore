package com.dakuo.ctscore.command

import com.dakuo.ctscore.command.impl.*
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

@CommandHeader(name = "CtScore",aliases = ["cs","cts"],permission = "ctscore.access")
object CommandHandler {

    @CommandBody(permission = "ctscore.look")
    val look = CommandLook.command

    @CommandBody(permission = "ctscore.give")
    val give = CommandGive.command

    @CommandBody(permission = "ctscore.set")
    val set = CommandSet.command

    @CommandBody(permission = "ctscore.take")
    val take = CommandTake.command

    @CommandBody(permission = "ctscore.reset")
    val reset = CommandReset.command

    @CommandBody(permission = "ctscore.reload")
    val reload = CommandReload.command

    @CommandBody(permission = "ctscore.pay")
    val pay = CommandPay.command

    @CommandBody(permission = "ctscore.buy")
    val buy = CommandShop.command

    @CommandBody(permission = "ctscore.load")
    val load = CommandLoad.command

    @CommandBody(permission = "ctscore.access")
    val main = mainCommand {
        execute<CommandSender>{ sender, context, argument ->
            if (argument.isEmpty()){
                sendHelp(sender)
                return@execute
            }
        }
    }

    @CommandBody(permission = "ctscore.access")
    val help = subCommand{
        execute<CommandSender> { sender, _, _ ->
            sendHelp(sender)
        }
    }

    fun sendHelp(sender: CommandSender){
        sender.sendLang("help")
        sender.sendLang("help-1")
        sender.sendLang("help-2")
        sender.sendLang("help-3")
        sender.sendLang("help-4")
        sender.sendLang("help-5")
        sender.sendLang("help-6")
        sender.sendLang("help-7")
        sender.sendMessage("")
    }



}
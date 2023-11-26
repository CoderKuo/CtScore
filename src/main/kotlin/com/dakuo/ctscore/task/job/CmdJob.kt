package com.dakuo.ctscore.task.job

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder

class CmdJob:IJob() {
    override val name: String = "cmd"
    override fun run(player: Player,data:List<String>) {
        data.forEach {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),it.replace("%player%", player.name).replacePlaceholder(player))
        }
    }

}
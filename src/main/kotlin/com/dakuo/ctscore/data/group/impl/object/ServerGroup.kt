package com.dakuo.ctscore.data.group.impl.`object`

import com.dakuo.ctscore.data.group.AbstractGroup
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import taboolib.common.platform.ProxyCommandSender

object ServerGroup: AbstractGroup<Server>() {

    override fun get(sender: ProxyCommandSender): Server? {
        return Bukkit.getServer()
    }

    override fun getSaveKey(group: Server): String {
        return group.name
    }
}
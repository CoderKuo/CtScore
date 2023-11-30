package com.dakuo.ctscore.data.group.impl

import com.dakuo.ctscore.data.group.AbstractGroup
import org.bukkit.OfflinePlayer
import org.bukkit.Server

class ServerGroup(val server: Server): AbstractGroup<Server>() {


    override fun get(): Server {
        return server
    }

    override fun getSaveKey(group: Server): String {
        return group.name
    }

    override fun containsPlayer(offlinePlayer: OfflinePlayer): Boolean {
        return server.offlinePlayers.find {
            it.uniqueId == offlinePlayer.uniqueId
        } != null
    }
}
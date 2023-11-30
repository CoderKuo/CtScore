package com.dakuo.ctscore.data.group.impl

import com.dakuo.ctscore.data.group.AbstractGroup
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class PlayerGroup(val player: OfflinePlayer): AbstractGroup<OfflinePlayer>() {

    constructor(name:String):this(Bukkit.getOfflinePlayer(name))

    override fun get(): OfflinePlayer {
        return player
    }

    override fun getSaveKey(group: OfflinePlayer): String {
        return group.uniqueId.toString()
    }

    override fun containsPlayer(offlinePlayer: OfflinePlayer): Boolean {
        return player.uniqueId == offlinePlayer.uniqueId
    }
}
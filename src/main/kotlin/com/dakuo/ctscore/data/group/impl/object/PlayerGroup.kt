package com.dakuo.ctscore.data.group.impl.`object`

import com.dakuo.ctscore.data.group.AbstractGroup
import com.dakuo.ctscore.data.group.GroupName
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

@GroupName(["player","Player"])
class PlayerGroup(val player: OfflinePlayer): AbstractGroup<OfflinePlayer>() {

    override val name: List<String> = listOf("player","Player")

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
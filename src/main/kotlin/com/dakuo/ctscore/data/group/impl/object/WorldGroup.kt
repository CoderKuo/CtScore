package com.dakuo.ctscore.data.group.impl.`object`

import com.dakuo.ctscore.data.group.AbstractGroup
import org.bukkit.OfflinePlayer
import org.bukkit.World

class WorldGroup(val world:World):AbstractGroup<World>() {

    override val name: List<String> = listOf("world","World")

    override fun get(): World {
        return world
    }

    override fun containsPlayer(offlinePlayer: OfflinePlayer): Boolean {
        return offlinePlayer.player?.world == world
    }

    override fun getSaveKey(group: World): String {
        return group.name
    }
}
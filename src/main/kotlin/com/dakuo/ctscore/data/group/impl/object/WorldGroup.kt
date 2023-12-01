package com.dakuo.ctscore.data.group.impl.`object`

import com.dakuo.ctscore.GroupUtils
import com.dakuo.ctscore.data.group.AbstractGroup
import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender

object WorldGroup:AbstractGroup<World>() {

    override fun get(sender: ProxyCommandSender): World? {
        val offlinePlayer = GroupUtils.senderToPlayer(sender) ?: return null
        return if (offlinePlayer.isOnline) {
            (offlinePlayer as Player).world
        } else {
            null
        }
    }

    override fun getSaveKey(group: World): String {
        return group.name
    }
}
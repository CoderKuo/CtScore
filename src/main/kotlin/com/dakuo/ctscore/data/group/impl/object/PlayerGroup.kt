package com.dakuo.ctscore.data.group.impl.`object`

import com.dakuo.ctscore.data.group.AbstractGroup
import com.dakuo.ctscore.data.group.GroupName
import org.bukkit.OfflinePlayer
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.platform.type.BukkitPlayer

@GroupName(["player","Player"])
object PlayerGroup: AbstractGroup<OfflinePlayer>() {


    override fun getSaveKey(group: OfflinePlayer): String {
        return group.uniqueId.toString()
    }

    override fun get(sender: ProxyCommandSender): OfflinePlayer? {
        return if (sender is ProxyPlayer){
            (sender as BukkitPlayer).player
        }else{
            null
        }
    }
}
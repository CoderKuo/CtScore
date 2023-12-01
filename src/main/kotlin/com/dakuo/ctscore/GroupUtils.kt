package com.dakuo.ctscore

import org.bukkit.OfflinePlayer
import taboolib.common.platform.ProxyCommandSender
import taboolib.platform.type.BukkitPlayer

object GroupUtils {

    fun senderToPlayer(sender: ProxyCommandSender):OfflinePlayer?{
        return if (sender is BukkitPlayer){
            (sender as BukkitPlayer).player
        }else{
            null
        }
    }

}
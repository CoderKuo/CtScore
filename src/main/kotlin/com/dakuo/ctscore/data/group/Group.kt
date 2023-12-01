package com.dakuo.ctscore.data.group

import org.bukkit.OfflinePlayer
import taboolib.common.platform.ProxyCommandSender

interface Group<T> {

    fun getSaveKey(group: T):String

    fun get(sender: ProxyCommandSender):T?
}
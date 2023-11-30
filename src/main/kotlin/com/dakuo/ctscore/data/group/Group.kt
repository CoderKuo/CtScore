package com.dakuo.ctscore.data.group

import org.bukkit.OfflinePlayer

interface Group<T> {

    fun get():T

    fun getSaveKey(group:T):String

    fun containsPlayer(offlinePlayer: OfflinePlayer):Boolean


}
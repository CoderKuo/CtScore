package com.dakuo.ctscore.data

import com.dakuo.ctscore.Score
import org.bukkit.OfflinePlayer

class RankCache(val score:Score,val rankList:MutableList<ScoreCache>) {
    companion object {
        @JvmStatic
        val cache:MutableList<RankCache> = mutableListOf()
    }

}
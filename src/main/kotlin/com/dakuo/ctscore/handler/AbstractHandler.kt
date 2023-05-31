package com.dakuo.ctscore.handler

import com.dakuo.ctscore.Score
import com.dakuo.ctscore.data.ScoreCache
import org.bukkit.OfflinePlayer
import java.util.*

abstract class AbstractHandler {


    abstract fun balance(type:Score,uuid: UUID):Number
    abstract fun deposit(type:Score,uuid:UUID,number: Number):Boolean
    abstract fun withdraw(type:Score,uuid:UUID,number: Number):Boolean
    abstract fun set(type:Score,uuid:UUID,number: Number):Boolean

    abstract fun save()

    abstract fun rank(number: Int,score: Score):MutableList<ScoreCache>
}
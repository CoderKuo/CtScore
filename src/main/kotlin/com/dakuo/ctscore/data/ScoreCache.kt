package com.dakuo.ctscore.data

import com.dakuo.ctscore.Score
import java.util.*

class ScoreCache(val uuid: UUID,val score: Score,var number: Number) {

    companion object {
        @JvmStatic
        val cache:MutableList<ScoreCache> = mutableListOf()
    }


}
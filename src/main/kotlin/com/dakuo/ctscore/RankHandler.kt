package com.dakuo.ctscore

import com.dakuo.ctscore.data.RankCache
import com.dakuo.ctscore.data.ScoreCache
import com.dakuo.ctscore.data.ScoreManager

object RankHandler {
    fun init(){
        RankCache.cache.clear()
        for (score in ScoreManager.cache) {
            if (score.rank){
                RankCache.cache.add(RankCache(score,CtScore.handler.rank(CtScore.config.getInt("cache_count"),score)))
            }
        }
    }
}
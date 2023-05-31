package com.dakuo.ctscore.data

import com.dakuo.ctscore.Score
import taboolib.module.configuration.Configuration

object ScoreManager {
    val cache: MutableList<Score> = mutableListOf()

    fun getScoreById(id:String):Score?{
        for (score in cache) {
            if (score.id == id || score.name == id){
                return score
            }
        }
        return null
    }

}
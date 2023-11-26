package com.dakuo.ctscore.api

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.RankCache
import com.dakuo.ctscore.data.ScoreManager
import org.bukkit.Bukkit

object CtScoreAPI {
    fun getBalance(type:String,name:String):Number{
        return CtScore.handler.balance(ScoreManager.getScoreById(type)!!, Bukkit.getOfflinePlayer(name).uniqueId)
    }

    fun deposit(type:String,name:String,amount:Number){
        CtScore.handler.deposit(ScoreManager.getScoreById(type)!!, Bukkit.getOfflinePlayer(name).uniqueId,amount)
    }

    fun withdraw(type: String,name: String,amount: Number){
        CtScore.handler.withdraw(ScoreManager.getScoreById(type)!!, Bukkit.getOfflinePlayer(name).uniqueId,amount)
    }

    fun reset(type: String,name:String){
        set(type,name,ScoreManager.getScoreById(type)?.default ?: 0)
    }

    fun set(type: String,name: String,amount: Number){
        CtScore.handler.set(ScoreManager.getScoreById(type)!!, Bukkit.getOfflinePlayer(name).uniqueId,amount)
    }

    fun getRank(type:String,index:Int){

    }
}
package com.dakuo.ctscore.data

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.api.CtScoreAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.module.lang.sendLang
import taboolib.platform.compat.PlaceholderExpansion
import taboolib.platform.compat.replacePlaceholder
import java.lang.Exception

object ShopManager {
    val list:MutableList<Goods> = mutableListOf()

    fun initShop(){
        list.clear()
        val shop = CtScore.shop
        val keys = shop.getKeys(false)
        keys.forEach {
            val type = shop.getString("$it.type")
            val amount = shop.getDouble("$it.amount")
            val name = shop.getString("$it.name")
            val commands = shop.getStringList("$it.commands")
            list.add(Goods(it,type!!,amount,name!!, commands as MutableList<String>))
        }
    }

    fun buy(player: ProxyPlayer, param:String){
        for (goods in list) {
            if (goods.id == param || goods.name == param){
                val balance = CtScoreAPI.getBalance(goods.type, player.name)
                if (balance.toDouble() < goods.amount){
                    player.sendLang("Shop-1",goods.amount,ScoreManager.getScoreById(goods.type)!!.name)
                    return
                }else{
                    CtScoreAPI.withdraw(goods.type,player.name,goods.amount)
                    val commands = goods.commands
                    val isop = player.isOp
                    try{
                        player.isOp = true
                        for (command in commands) {
                            val replacePlaceholder = command.replace("%player%", player.name).replacePlaceholder(Bukkit.getPlayer(player.uniqueId)!!)
                            player.performCommand(replacePlaceholder)
                        }
                    }finally {
                        player.isOp = isop
                    }
                    player.sendLang("Shop-2",goods.name,ScoreManager.getScoreById(goods.type)!!.name,goods.amount)
                    return
                }
            }

        }
    }
}
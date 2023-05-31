package com.dakuo.ctscore.hook

import com.dakuo.ctscore.api.CtScoreAPI
import com.dakuo.ctscore.data.RankCache
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object PlaceHolderHook :PlaceholderExpansion{

    override val identifier: String
        get() = "ctscore"

    override fun onPlaceholderRequest(player: OfflinePlayer?, args: String): String{
        if (player == null){
            return ""
        }
        val split = args.split("_")
        when(split[0]){
            "number"->return CtScoreAPI.getBalance(split[1],player.name!!).toString()
            "rank"->{
                for (rankCache in RankCache.cache) {
                    if (split[1] == rankCache.score.id){
                        if (rankCache.rankList.size < split[2].toInt()){
                            return "暂无"
                        }
                        val get = rankCache.rankList.get(split[2].toInt() - 1)
                        val offlinePlayer = Bukkit.getOfflinePlayer(get.uuid)
                        offlinePlayer.name?.let {
                            return "$it - ${get.number}"
                        }
                    }
                }
            }
            "rankplayer"->{
                for (rankCache in RankCache.cache) {
                    if (split[1] == rankCache.score.id){
                        for ((i,scoreCache) in rankCache.rankList.withIndex()) {
                            if (split[2] == "me"){
                                if (scoreCache.uuid == player.uniqueId){
                                    return (i+1).toString()
                                }
                            }else{
                                val offlinePlayer = Bukkit.getOfflinePlayer(scoreCache.uuid)
                                offlinePlayer.name?.let {
                                    if (it == split[2]){
                                        return (i+1).toString()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ""
    }

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return onPlaceholderRequest(player as OfflinePlayer,args)
    }
}
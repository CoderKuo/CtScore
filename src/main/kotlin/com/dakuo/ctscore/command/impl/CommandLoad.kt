package com.dakuo.ctscore.command.impl

import com.alibaba.fastjson.JSON
import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.ScoreCache
import com.dakuo.ctscore.data.ScoreManager
import com.dakuo.ctscore.handler.*
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import java.util.*

object CommandLoad {

    val command = subCommand {
        dynamic {
            suggestion<ProxyCommandSender>{ _, _ ->
                arrayOf("yaml","mysql","sqlite").map { it }
            }
            execute<ProxyCommandSender>{ sender, context, argument ->
                when(argument){
                    "yaml"->{
                        val cache = ScoreManager.cache
                        cache.forEach {
                            val loadYaml = YamlFileHandler.loadYaml(it)
                            val keys = loadYaml.getKeys(false)
                            keys.forEach { key->
                                ScoreCache.cache.forEach { cache->
                                    if (cache.uuid.toString() == key && cache.score == it){
                                        cache.number = loadYaml.getDouble(key)
                                    }
                                }
                                val has = ScoreCache.cache.find { cache ->
                                    cache.uuid.toString() == key && cache.score == it
                                }
                                if (has == null) {
                                    ScoreCache.cache.add(
                                        ScoreCache(
                                            UUID.fromString(key),
                                            it,
                                            loadYaml.getDouble(key)
                                        )
                                    )
                                }
                            }
                            sender.sendMessage("§7[§b积分§7] §r${it.name} 的 ${keys.size} 条数据已经由yaml导出")
                        }

                    }
                    "mysql"->{
                        val select = MysqlHandler.playerTable.select(MysqlHandler.dataSource) {
                            rows("uuid", "data")
                        }
                        val list = mutableListOf<ScoreCache>()
                        select.forEach {
                            val uuid = getString("uuid")
                            val string = getString("data")
                            val parseArray = JSON.parseArray(string,MysqlPojo::class.java)
                            parseArray?.forEach {
                                if (ScoreManager.getScoreById(it.scoreId) == null){
                                    sender.sendMessage("§7[§b积分§7] §cid为${it.scoreId}的积分不存在或已被删除,将跳过导入")
                                }else {
                                    list.add(
                                        ScoreCache(
                                            UUID.fromString(uuid),
                                            ScoreManager.getScoreById(it.scoreId)!!,
                                            it.number
                                        )
                                    )
                                }
                            }
                        }
                        list.forEach {
                            ScoreCache.cache.add(it)
                        }
                        sender.sendMessage("§7[§b积分§7] §r${list.size} 条数据已经由mysql导出")
                    }
                    "sqlite"->{
                        val select = SQLiteHandler.playerTable.select(SQLiteHandler.dataSource) {
                            rows("uuid", "data")
                        }
                        val list = mutableListOf<ScoreCache>()
                        select.forEach {
                            val uuid = getString("uuid")
                            val string = getString("data")
                            val parseArray = JSON.parseArray(string,MysqlPojo::class.java)
                            parseArray?.forEach {
                                if (ScoreManager.getScoreById(it.scoreId) == null){
                                    sender.sendMessage("§7[§b积分§7] §cid为${it.scoreId}的积分不存在或已被删除,将跳过导入")
                                }else {
                                    list.add(
                                        ScoreCache(
                                            UUID.fromString(uuid),
                                            ScoreManager.getScoreById(it.scoreId)!!,
                                            it.number
                                        )
                                    )
                                }
                            }
                        }
                        list.forEach {
                            ScoreCache.cache.add(it)
                        }
                        sender.sendMessage("§7[§b积分§7] §r${list.size} 条数据已经由sqlite导出")
                    }
                }
                CtScore.handler.save()
            }
        }
    }
}
package com.dakuo.ctscore.handler

import com.alibaba.fastjson.JSON
import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.Score
import com.dakuo.ctscore.data.ScoreCache
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.math.BigDecimal
import java.util.*

object MysqlHandler: AbstractHandler() {
    val cache = ScoreCache.cache

    val host = CtScore.config.getHost("mysql")

    val playerTable = Table("ctscore_playerdata",host){
        add("uuid") {
            type(ColumnTypeSQL.VARCHAR, 255)
        }
        add("data") {
            type(ColumnTypeSQL.TEXT)
        }
    }

    val dataSource = host.createDataSource()

    init {
        playerTable.createTable(dataSource)
    }

    override fun balance(type: Score, uuid: UUID): Number {
        getPlayerByCache(uuid, type)?.let {
            return it.number
        }


        var result = 0.0
        if (playerTable.find(dataSource){
            where("uuid" eq uuid.toString())
            }){
            playerTable.select(dataSource){
                where("uuid" eq uuid.toString())
                rows("data")
            }.first {
                val str = getString("data")
                JSON.parseArray(str, MysqlPojo::class.java)?.forEach {
                    if (it.scoreId == type.id) {
                        result = it.number.toDouble()
                    }
                }
            }
        }else{
            playerTable.insert(dataSource,"uuid","data"){
                val array = mutableListOf<MysqlPojo>()
                array.add(MysqlPojo(type.id,type.default))
                value(uuid.toString(),JSON.toJSONString(array))
            }
        }

        cache.add(ScoreCache(uuid, type, result))
        return result
    }

    override fun deposit(type: Score, uuid: UUID, number: Number): Boolean {
        val playerByCache = getPlayerByCache(uuid, type)
        playerByCache?.let {
            val toDouble = playerByCache.number.toDouble()
            val plus = toDouble.plus(number.toDouble())
            playerByCache.number = plus
            return true
        }
        val result = balance(type,uuid)
        val bigDecimal = BigDecimal(result.toDouble()).add(BigDecimal(number.toDouble().toString())).toDouble()
        cache.add(ScoreCache(uuid,type,bigDecimal))
        return true
    }

    override fun withdraw(type: Score, uuid: UUID, number: Number): Boolean {
        return deposit(type, uuid, -number.toDouble())
    }

    override fun set(type: Score, uuid: UUID, number: Number): Boolean {
        val playerByCache = getPlayerByCache(uuid, type)
        playerByCache?.let {
            playerByCache.number = number
            return true
        }
        cache.add(ScoreCache(uuid, type, number.toDouble()))
        return true
    }

    override fun save() {
        if (cache.size <= 0){
            return
        }

        val iterator = cache.iterator()
        while (iterator.hasNext()){
            val next = iterator.next()
            var result:MutableList<MysqlPojo> = mutableListOf()

            if (playerTable.find(dataSource){
                    where("uuid" eq next.uuid.toString())
                }){
                playerTable.select(dataSource){
                    where("uuid" eq next.uuid.toString())
                    rows("data")
                }.first {
                    val str = getString("data")
                    val jsonArray = JSON.parseArray(str, MysqlPojo::class.java)
                    val has = jsonArray.find {
                        it.scoreId == next.score.id
                    }
                    if(has != null){
                        has.number = next.number
                    }else{
                        jsonArray.add(MysqlPojo(next.score.id,next.number))
                    }
                    result = jsonArray

                }

                playerTable.update(dataSource){
                    where("uuid" eq next.uuid.toString())
                    set("data",JSON.toJSONString(result))
                }
            }else{
                playerTable.insert(dataSource,"uuid","data"){
                    val array = mutableListOf<MysqlPojo>()
                    array.add(MysqlPojo(next.score.id,next.number))
                    value(next.uuid.toString(),JSON.toJSONString(array))
                }
            }

            iterator.remove()
        }
    }

    override fun rank(number: Int, score: Score): MutableList<ScoreCache> {
        val rankList = mutableListOf<ScoreCache>()
        playerTable.select(dataSource){
            rows("uuid","data")
        }.forEach {
            val data = this.getString("data")
            val uuid = this.getString("uuid")
            JSON.parseArray(data,MysqlPojo::class.java)?.forEach {
                if (it.scoreId == score.id) {
                    rankList.add(ScoreCache(UUID.fromString(uuid), score, it.number))
                }
            }
        }
        rankList.sortByDescending { it.number.toDouble() }
        return rankList
    }

    private fun getPlayerByCache(uuid: UUID, score: Score): ScoreCache? {
        for (scoreCache in cache) {
            return if (scoreCache.uuid == uuid && scoreCache.score == score) {
                scoreCache
            } else continue
        }
        return null
    }
}

data class MysqlPojo(val scoreId:String,var number:Number){
}
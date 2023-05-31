package com.dakuo.ctscore.handler

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.Score
import com.dakuo.ctscore.data.ScoreCache
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitScheduler
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.module.lang.sendLang
import java.io.File
import java.math.BigDecimal
import java.util.*

object YamlHandler : AbstractHandler() {
    val cache = ScoreCache.cache

    override fun balance(type: Score, uuid: UUID): Number {
        val playerByCache = getPlayerByCache(uuid, type)
        playerByCache?.let {
            return playerByCache.number
        }

        val loadYaml = YamlFileHandler.loadYaml(type)
        val string = loadYaml.getString(uuid.toString(),"0")!!
        cache.add(ScoreCache(uuid, type, string.toDouble()))
        return string.toDouble()
    }

    override fun deposit(type: Score, uuid: UUID, number: Number): Boolean {
        val playerByCache = getPlayerByCache(uuid, type)
        playerByCache?.let {
            val toDouble = playerByCache.number.toDouble()
            val plus = toDouble.plus(number.toDouble())
            playerByCache.number = plus
            return true
        }
        val loadYaml = YamlFileHandler.loadYaml(type)
        val string = loadYaml.getString(uuid.toString(),"0")!!
        val bigDecimal = BigDecimal(string).add(BigDecimal(number.toDouble().toString())).toDouble()

        cache.add(ScoreCache(uuid, type,bigDecimal));
        return true
    }

    override fun withdraw(type: Score, uuid: UUID, number: Number): Boolean {
        return deposit(type,uuid,-number.toDouble())
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
            YamlFileHandler.saveYaml(iterator.next())
            iterator.remove()
        }
    }

    fun getPlayerByCache(uuid: UUID, score: Score): ScoreCache? {
        for (scoreCache in cache) {
            return if (scoreCache.uuid == uuid && scoreCache.score == score) {
                scoreCache
            } else continue
        }
        return null
    }

    override fun rank(number: Int,score: Score): MutableList<ScoreCache> {
        val rankList = mutableListOf<ScoreCache>()
        val loadYaml = YamlFileHandler.loadYaml(score)
        val keys = loadYaml.getKeys(false)
        for (key in keys) {
            rankList.add(ScoreCache(UUID.fromString(key),score,loadYaml.getDouble(key)))
        }
        rankList.sortByDescending { it.number.toDouble() }
        return rankList
    }
}

object YamlFileHandler {

    fun saveYaml(scoreCache: ScoreCache){
        submitAsync {
            val loadYaml = loadYaml(scoreCache.score)
            loadYaml.set(scoreCache.uuid.toString(), scoreCache.number)
            val dataFolder = CtScore.instance.dataFolder
            loadYaml.save("${dataFolder.path}/${scoreCache.score.id}.yml")
        }
    }

    fun loadYaml(score: Score): YamlConfiguration {
        val dataFolder = CtScore.instance.dataFolder
        val file = File("${dataFolder.path}/${score.id}.yml")
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
            val createNewFile = file.createNewFile()
            if (createNewFile) {
                console().sendLang("Yaml-Created", score.name)
            }
        }

        return YamlConfiguration.loadConfiguration(file)
    }
}
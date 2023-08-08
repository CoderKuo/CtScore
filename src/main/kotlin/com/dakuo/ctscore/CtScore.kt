package com.dakuo.ctscore

import com.dakuo.ctscore.data.ScoreManager
import com.dakuo.ctscore.data.ShopManager
import com.dakuo.ctscore.handler.*
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.*
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

@RuntimeDependency(value = "com.alibaba:fastjson:1.2.78")
object CtScore : Plugin(){


    val instance by lazy { BukkitPlugin.getInstance() }

    val handler:AbstractHandler by lazy {
        loadHandler()
    }

    @Config("config.yml",migrate = true,autoReload = true)
    lateinit var config: Configuration

    @Config("shop.yml",migrate = true,autoReload = true)
    lateinit var shop:Configuration

    override fun onEnable() {
        Metrics(15440, pluginVersion, runningPlatform)
        init()
        console().sendLang("Plugin-Enabled", pluginVersion,KotlinVersion.CURRENT.toString())

    }

    override fun onDisable() {
        handler.save()
        console().sendLang("Plugin-Disabled")
    }

    fun loadHandler():AbstractHandler {
        val string = config.getString("database_type")
        console().sendLang("Data-Load",string!!)
        console().sendLang("Data-Load2")
        return when(string){
            "yaml"->YamlHandler
            "mysql"->MysqlHandler
            "sqlite"->SQLiteHandler
            else->YamlHandler
        }
    }

    fun loadScoreManager() {
        ScoreManager.cache.clear()
        val score = config.getConfigurationSection("Score")!!
        val keys = score.getKeys(false)
        console().sendLang("Score-Loaded")
        console().sendLang("Score-Loaded2")
        keys.forEach {
            val name = score.getString("$it.name")
            val pay = score.getBoolean("$it.pay", false)
            val rank = score.getBoolean("$it.rank", false)
            ScoreManager.cache.add(Score(it,name!!,pay,rank))
            console().sendLang("Score-Loaded3",it,name,if (pay) "§a可转账" else "§c不可转账",if (rank) "§a开启排名" else "§c未开启排名")
        }
        console().sendLang("Score-Loaded2")
    }

    fun init(){
        loadScoreManager()
        submit(period = config.getInt("auth_save").toLong(),async = true) {
            handler.save()
        }
        submitAsync {
            RankHandler.init()
        }
        submit(period = config.getInt("refresh_rank").toLong(),async = true) {
            RankHandler.init()
        }
        ShopManager.initShop()
    }


}
package com.dakuo.ctscore.handler.group

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.data.group.GroupScoreManager
import com.dakuo.ctscore.data.group.GroupValue
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost

object GroupMysqlHandler:AbstractGroupHandler() {

    val host = CtScore.config.getHost("mysql")

    val groupScoreTable = Table("ctscore_groupscore",host){
        add("id") {
            id()
            type(ColumnTypeSQL.INT)
            extra {
                this.options(ColumnOptionSQL.AUTO_INCREMENT)
                this.options(ColumnOptionSQL.PRIMARY_KEY)
            }
        }
        add("name") {
            type(ColumnTypeSQL.VARCHAR,255)
            extra {
                options(ColumnOptionSQL.KEY)
            }
        }
        add("saveKey") {
            type(ColumnTypeSQL.VARCHAR,255)
            extra {
                options(ColumnOptionSQL.KEY)
            }
        }
        add("value") {
            type(ColumnTypeSQL.TEXT)
        }
    }

    val dataSource = host.createDataSource()

    init {
        groupScoreTable.createTable(dataSource)
    }

    override fun get(name: String, saveKey: String): GroupValue<*>? {
        val find = groupScoreTable.find(dataSource){
            where("name" eq name)
            where("saveKey" eq saveKey)
        }

        if (find){
            val fromId = GroupScoreManager.getFromId(name) ?: return null

            return groupScoreTable.select(dataSource){
                where("name" eq name)
                where("saveKey" eq saveKey)
            }.first {
                return@first fromId.value.getConstructor(String::class.java).newInstance(this.getString("value"))
            }
        }
        return null
    }

    override fun insert(name: String, saveKey: String, value: GroupValue<*>) {
        groupScoreTable.insert(dataSource,"name","saveKey"){
            value(value.saveToString())
        }
    }


}
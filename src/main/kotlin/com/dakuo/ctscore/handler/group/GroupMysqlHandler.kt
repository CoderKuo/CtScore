package com.dakuo.ctscore.handler.group

import com.dakuo.ctscore.CtScore
import org.serverct.ersha.manager.database.database
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.util.*

class GroupMysqlHandler:AbstractGroupHandler() {

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
        }
        add("saveKey") {
            type(ColumnTypeSQL.VARCHAR,255)
        }
        add("value") {
            type(ColumnTypeSQL.TEXT)
        }
    }

    val dataSource = host.createDataSource()

    init {
        groupScoreTable.createTable(dataSource)
    }

    override fun get(name: String, saveKey: String): Any? {
        groupScoreTable.find(dataSource){

        }
        return null
    }
}
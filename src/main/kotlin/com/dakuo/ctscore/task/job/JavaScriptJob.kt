package com.dakuo.ctscore.task.job

import org.bukkit.entity.Player
import taboolib.common5.compileJS
import javax.script.SimpleBindings

class JavaScriptJob:IJob() {
    override val name: String = "js"

    override fun run(player: Player, data: List<String>) {
        data.forEach {
            it.compileJS()?.eval(SimpleBindings().apply {
                this["player"] = player
            })
        }
    }
}
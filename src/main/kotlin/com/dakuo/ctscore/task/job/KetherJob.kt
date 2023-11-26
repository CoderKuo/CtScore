package com.dakuo.ctscore.task.job

import org.bukkit.entity.Player
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

class KetherJob: IJob() {
    override val name: String = "kether"

    override fun run(player: Player, data: List<String>) {
        runKether {
            KetherShell.eval(data, ScriptOptions.new {
                    this.sender(player)
            })
        }
    }


}
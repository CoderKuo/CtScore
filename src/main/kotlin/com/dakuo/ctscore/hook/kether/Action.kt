package com.dakuo.ctscore.hook.kether

import com.dakuo.ctscore.api.CtScoreAPI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.Parser
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class Action {

    class ActionGet(val type: ParsedAction<String>, val player:ParsedAction<String>): ScriptAction<Number>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Number> {
            val type = frame.newFrame(type).run<String>().get()
            val player = frame.newFrame(player).run<String>().get()
            return CompletableFuture.completedFuture(CtScoreAPI.getBalance(type,player))
        }

    }

    class ActionSet(val type: ParsedAction<String>,val number: ParsedAction<String>, val player:ParsedAction<String>): ScriptAction<Unit>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Unit> {
            val type = frame.newFrame(type).run<String>().get()
            val number = frame.newFrame(number).run<String>().get().toDouble()
            val player = frame.newFrame(player).run<String>().get()
            return CompletableFuture.completedFuture(CtScoreAPI.set(type,player,number))
        }

    }

    class ActionAdd(val type: ParsedAction<String>,val number: ParsedAction<String>, val player:ParsedAction<String>): ScriptAction<Unit>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Unit> {
            val type = frame.newFrame(type).run<String>().get()
            val number = frame.newFrame(number).run<String>().get().toDouble()
            val player = frame.newFrame(player).run<String>().get()
            return CompletableFuture.completedFuture(CtScoreAPI.deposit(type,player,number))
        }

    }

    class ActionSub(val type: ParsedAction<String>,val number: ParsedAction<String>, val player:ParsedAction<String>): ScriptAction<Unit>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Unit> {
            val type = frame.newFrame(type).run<String>().get()
            val number = frame.newFrame(number).run<String>().get().toDouble()
            val player = frame.newFrame(player).run<String>().get()
            return CompletableFuture.completedFuture(CtScoreAPI.withdraw(type,player,number))
        }

    }


    companion object{

        @KetherParser(["ctscore"], shared = true)
        fun parser()= scriptParser {
            it.switch {
                case("get"){
                    ActionGet(it.nextAction<String>(),it.nextAction<String>())
                }
                case("add"){
                    ActionAdd(it.nextAction<String>(),it.nextAction<String>(),it.nextAction<String>())
                }
                case("set"){
                    ActionSet(it.nextAction<String>(),it.nextAction<String>(),it.nextAction<String>())
                }
                case("sub"){
                    ActionSub(it.nextAction<String>(),it.nextAction<String>(),it.nextAction<String>())
                }
            }
        }

    }
}
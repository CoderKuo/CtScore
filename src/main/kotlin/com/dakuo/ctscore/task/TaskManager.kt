package com.dakuo.ctscore.task

import com.dakuo.ctscore.task.job.IJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependency
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File

@RuntimeDependency("org.quartz-scheduler:quartz:2.5.0-rc1", test = "!org.quartz.impl.StdSchedulerFactory")
object TaskManager {

    val tasks = mutableListOf<Task>()

    val scheduler = StdSchedulerFactory.getDefaultScheduler();


    @Awake(LifeCycle.ENABLE)
    fun loadAllTask(){

        val jobs = runningClasses.filter {
            IJob::class.java.isAssignableFrom(it)
        }.associate{
            val instance = (it as Class<IJob>).newInstance()
            Pair(instance.name,instance)
        }

        tasks.clear()
        Configuration.loadFromFile(File(BukkitPlugin.getInstance().dataFolder,"task.yml").apply {
            if (!this.exists()){
                BukkitPlugin.getInstance().saveResource("task.yml",false)
            }
        }).apply {
            this.getKeys(false).forEach {key->
                val section = this.getConfigurationSection(key)!!
                val cron = section.getString("cron") ?: error("请填入cron表达式")
                val jobs = section.getMapList("jobs").map {map->
                    return@map map.keys.associateWith {
                        println(jobs)
                        if (!jobs.containsKey(it)){
                            error("没有找到名为 $it 的job")
                        }
                        if (map[it] is String) {
                            listOf(map[it].toString())
                        }else{
                            map[it] as List<String>
                        }
                    }.apply {
                        this.keys.forEach {
                            if (jobs.containsKey(it)){
                                error("没有找到名为 $it 的job")
                            }
                        }
                    }.mapKeys { jobs[it.key] ?: error("没有找到名为 ${it.key} 的job") }
                }
                info("Task $key 已加载")
                tasks.add(Task(key,cron,jobs))
            }
        }
        startAllTask()
    }

    fun startAllTask(){
        tasks.forEach { it ->
            val jobs = it.jobs.flatMap {
                it.map { map->
                    val jobData = JobDataMap().apply {
                        this["data"] = map.value
                    }
                    JobBuilder.newJob(map.key::class.java).usingJobData(jobData).build()
                }
            }

            val trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(it.cron))
                .build()
            jobs.forEach {
                scheduler.scheduleJob(it,trigger)
            }
        }
        scheduler.start()
    }



}

data class Task(val id:String,val cron:String,val jobs:List<Map<IJob,List<String>>>){

}
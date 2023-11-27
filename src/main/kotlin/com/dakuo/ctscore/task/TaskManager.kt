package com.dakuo.ctscore.task

import com.dakuo.ctscore.CtScore
import com.dakuo.ctscore.task.job.IJob
import org.quartz.*
import org.quartz.Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY
import org.quartz.Trigger.TriggerState
import org.quartz.impl.StdSchedulerFactory
import org.quartz.impl.matchers.GroupMatcher
import org.quartz.simpl.RAMJobStore
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependency
import taboolib.common.io.getClasses
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.util.*

@RuntimeDependency("org.quartz-scheduler:quartz:2.5.0-rc1", test = "!org.quartz.impl.StdSchedulerFactory")
object TaskManager {

    private val tasks = mutableListOf<Task>()

    private val schedulerFactory = StdSchedulerFactory().apply {
        this.initialize(File(BukkitPlugin.getInstance().dataFolder,"quartz.properties").apply {
            if (!this.exists()){
                BukkitPlugin.getInstance().saveResource("quartz.properties",false)
            }
        }.path)
    }

    private var scheduler = schedulerFactory.scheduler;

    @Awake(LifeCycle.ENABLE)
    fun loadAllTask(){
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
                        if (!IJob.jobs.containsKey(it)){
                            error("没有找到名为 $it 的job")
                        }
                        if (map[it] is String) {
                            listOf(map[it].toString())
                        }else{
                            map[it] as List<String>
                        }
                    }.apply {
                        this.keys.forEach {
                            if (!IJob.jobs.containsKey(it)){
                                error("没有找到名为 $it 的job")
                            }
                        }
                    }.mapKeys { IJob.jobs[it.key] ?: error("没有找到名为 ${it.key} 的job") }
                }
                info("Task $key 已加载")
                tasks.add(Task(key,section.getBoolean("async"),cron,jobs))
            }
        }
        startAllTask()
    }

    private fun startAllTask(){
        scheduler = schedulerFactory.scheduler
        tasks.forEach { it ->
            val async = it.async
            val description = it.id
            val cron = it.cron
            val jobs = it.jobs.flatMap {
                it.map { map->
                    val jobData = JobDataMap().apply {
                        this["data"] = map.value
                        this["async"] = async
                    }
                    JobBuilder.newJob(map.key).usingJobData(jobData).withDescription("task: $description job: ${map.key.newInstance().name}").build()
                }
            }

            jobs.forEach {
                scheduler.scheduleJob(it,TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build())
            }
        }
        scheduler.start()
    }

    fun stopAllTask(){
        scheduler.clear()
        scheduler.shutdown()
    }

    fun pauseTask(key:String){
        scheduler.getTriggersOfJob(JobKey(key)).forEach {
            scheduler.pauseTrigger(it.key)
        }
        scheduler.pauseJob(JobKey(key))
    }

    fun resumeTask(key: String){
        scheduler.getTriggersOfJob(JobKey(key)).forEach {
            scheduler.resumeTrigger(it.key)
        }
        scheduler.resumeJob(JobKey(key))
    }

    fun getAllTask(): List<Pair<JobDetail,Trigger>> {
        return scheduler.jobGroupNames
            .flatMap { group -> scheduler.getJobKeys(GroupMatcher.groupEquals(group)) }
            .flatMap { jobKey ->
                val jobDetail = scheduler.getJobDetail(jobKey)
                scheduler.getTriggersOfJob(jobKey).map { trigger -> Pair(jobDetail, trigger) }
            }
    }

    fun getStatus(jobAndTrigger: Pair<JobDetail,Trigger>): JobStatus {
        jobAndTrigger.apply {
            val jobDetail = this.first
            val trigger = this.second
            val key = jobDetail.key
            val description = jobDetail.description
            val status = scheduler.getTriggerState(trigger.key)
            val nextFireTime = trigger.nextFireTime
            val previousFireTime = trigger.previousFireTime
            return JobStatus(key, description, status, nextFireTime, previousFireTime)
        }
    }

}

data class JobStatus(val key:JobKey, val description:String, val status:TriggerState, val nextFireTime: Date,val previousFireTime:Date?)

data class Task(val id:String,val async:Boolean,val cron:String,val jobs:List<Map<Class<IJob>,List<String>>>){

}
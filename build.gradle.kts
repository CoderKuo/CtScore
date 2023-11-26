plugins {
    java
    `java-library`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
}

taboolib {
    description {
        contributors {
            name("大阔").description("CtScore")
        }
        dependencies{
            name("PlaceholderAPI").optional(true)
        }
    }
    install("common")
    install("common-5")
    install("platform-bukkit")
    install("module-configuration")
    install("module-metrics")
    install("module-lang")
    install("module-kether")
    install("module-nms")
    install("module-nms-util")
    install("module-chat")
    install("module-database")
    install("expansion-javascript")
    version = "6.0.12-35"


}

repositories {
    mavenCentral()
    maven("https://maven.aliyun.com/repository/central")
    maven { url = uri("https://repo.tabooproject.org/repository/releases") }
}

dependencies {
    compileOnly("ink.ptms.core:v12002:12002:mapped")
    compileOnly("ink.ptms.core:v12002:12002:universal")
    compileOnly("com.alibaba:fastjson:1.2.83")
    compileOnly(fileTree("libs"))
    compileOnly(kotlin("stdlib"))
    compileOnly("org.quartz-scheduler:quartz:2.5.0-rc1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
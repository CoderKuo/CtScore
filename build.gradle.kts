plugins {
    java
    id("io.izzel.taboolib") version "1.33"
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.0"
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
    install("module-nms")
    install("module-nms-util")
    install("module-chat")
    install("module-database")
    version = "6.0.9-116"


}

repositories {
    mavenCentral()
    maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
    maven { url = uri("https://repo.tabooproject.org/repository/maven-releases") }
}

dependencies {
    compileOnly("ink.ptms.core:v11902:11902:universal")
    compileOnly("ink.ptms.core:v11902:11902:mapped")
    compileOnly("com.alibaba:fastjson:1.2.78")
    compileOnly(fileTree("libs"))
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
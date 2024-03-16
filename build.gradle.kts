import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.2"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(
            UNIVERSAL,
            METRICS,
            BUKKIT_ALL,
            KETHER,
            NMS,
            NMS_UTIL
        )
    }
    description {
        contributors {
            name("InkerXoe")
        }
        dependencies {
            name("Residence").with("bukkit").optional(true)
            name("GriefDefender").with("bukkit").optional(true)
            name("WorldGuard").with("bukkit").optional(true)
        }
        desc("Provide a flexible and powerful death punishment system for the Minecraft Bukkit Server")
    }
    version { taboolib = "6.1.0" }
}

repositories {
    mavenCentral()
    maven("https://repo.glaremasters.me/repository/bloodshot")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:v12004:universal")
    compileOnly("com.griefdefender:api:2.1.0-SNAPSHOT")
    compileOnly(dependencyNotation = "com.sk89q.worldguard:worldguard-bukkit:7.0.0")
    taboo(fileTree("libs/relocated-nashorn-15.4.jar"))
    compileOnly(fileTree("libs"))
    compileOnly(kotlin("stdlib"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

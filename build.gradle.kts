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
            KETHER
        )
    }
    description {
        contributors {
            name("InkerXoe")
        }
        desc("Provide a flexible and powerful death punishment system for the Minecraft Bukkit server")
    }
    version { taboolib = "6.1.0" }
}

repositories {
    mavenCentral()
}

configurations{
    maybeCreate("packShadow")
    get("compileOnly").extendsFrom(get("packShadow"))
    get("packShadow").extendsFrom(get("taboo"))
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:v12004:universal")
    "packShadow"(fileTree("libs"))
    "packShadow"(kotlin("stdlib"))
    "packShadow"("org.openjdk.nashorn:nashorn-core:15.4")
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
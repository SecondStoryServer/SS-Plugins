import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.bungee.BungeePluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0" apply false
    id("net.minecrell.plugin-yml.bungee") version "0.4.0" apply false
    id("org.jmailen.kotlinter") version "3.4.4"
    id("com.palantir.git-version") version "0.12.3"
}

val gitVersion: Closure<String> by extra

allprojects {
    apply(plugin = "org.jmailen.kotlinter")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")

    val shadowImplementation by configurations.creating
    val shadowApi by configurations.creating
    configurations["implementation"].extendsFrom(shadowImplementation)
    configurations["api"].extendsFrom(shadowApi)

    if (project.name != "common") {
        val project = Project.get(project.name) ?: error("Not Found Project ${project.name}")

        repositories {
            if (project.isProxyPlugin) {
                maven("https://papermc.io/repo/repository/maven-public/")
            } else {
                maven("https://repo.codemc.io/repository/maven-public/")
            }
        }

        dependencies {
            when (project) {
                Project.Core -> {
                    shadowImplementation(kotlin("stdlib-jdk8"))
                    shadowApi(project(":common"))
                    shadowApi("com.github.sya-ri:EasySpigotAPI:2.3.1") {
                        exclude(group = "org.spigotmc", module = "spigot-api")
                        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
                    }
                    api("org.yatopiamc:yatopia-api:1.16.5-R0.1-SNAPSHOT") {
                        exclude("org.codehaus.plexus", "plexus-compiler-eclipse")
                    }
                }
                Project.WCore -> {
                    shadowImplementation(kotlin("stdlib-jdk8"))
                    shadowApi(project(":common"))
                    api("io.github.waterfallmc:waterfall-api:1.16-R0.4-SNAPSHOT")
                }
                Project.WDiscord -> {
                    implementation("com.google.code.gson:gson:2.8.6")
                    testImplementation("org.slf4j:slf4j-simple:1.7.30")
                }
                Project.WVotifier -> {
                    implementation("io.netty:netty-handler:4.1.53.Final")
                    implementation("io.netty", "netty-transport-native-epoll", "4.1.53.Final", classifier = "linux-x86_64")
                }
                else -> {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
            project.implementationProjects.forEach { implementation(project(":$it")) }
            project.dependJarFile.forEach { api(files("../dependJars/$it.jar")) }
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
            }
        }

        if (project.isProxyPlugin) {
            apply(plugin = "net.minecrell.plugin-yml.bungee")

            configure<BungeePluginDescription> {
                name = project.name
                version = gitVersion()
                main = project.main
                author = project.author
                depends = project.allDependPlugin.toSet()
            }
        } else {
            apply(plugin = "net.minecrell.plugin-yml.bukkit")

            configure<BukkitPluginDescription> {
                name = project.name
                version = gitVersion()
                main = project.main
                author = project.author
                apiVersion = "1.16"
                depend = project.allDependPlugin
            }
        }

        tasks.withType<ShadowJar> {
            configurations = listOf(shadowImplementation, shadowApi)
            archiveClassifier.set("")
            destinationDirectory.set(file("../jars"))
        }
    } else {
        dependencies {
            implementation("com.google.guava:guava:21.0")
        }
    }
}

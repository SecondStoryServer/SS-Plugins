import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("com.palantir.git-version") version "0.12.3"
}

val gitVersion: Closure<String> by extra

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    configure<KtlintExtension> {
        version.set("0.40.0")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")

    val shadowImplementation by configurations.creating
    val shadowApi by configurations.creating
    configurations["implementation"].extendsFrom(shadowImplementation)
    configurations["api"].extendsFrom(shadowApi)

    val project = Project.get(project.name) ?: error("Not Found Project ${project.name}")

    repositories {
        maven("https://repo.codemc.io/repository/maven-public/")
    }

    dependencies {
        when (project) {
            Project.Core -> {
                shadowImplementation(kotlin("stdlib-jdk8"))
                shadowApi("com.github.sya-ri:EasySpigotAPI:2.3.1") {
                    exclude(group = "org.spigotmc", module = "spigot-api")
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
                }
            }
            else -> {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        implementation("org.yatopiamc:yatopia-api:1.16.5-R0.1-SNAPSHOT") {
            exclude("org.codehaus.plexus", "plexus-compiler-eclipse")
        }
        project.implementationProjects.forEach { implementation(project(":$it")) }
        project.dependJarFile.forEach { api(files("../dependJars/$it.jar")) }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            useIR = true
        }
    }

    configure<BukkitPluginDescription> {
        name = project.name
        version = gitVersion()
        main = project.main
        author = project.author
        apiVersion = project.apiVersion
        depend = project.allDependPlugin
        softDepend = project.allSoftDependPlugin
    }

    tasks.withType<ShadowJar> {
        configurations = listOf(shadowImplementation, shadowApi)
        archiveClassifier.set("")
        destinationDirectory.set(file("../jars"))
    }
}

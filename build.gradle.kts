import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")

    repositories {
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<ShadowJar> {
        configurations = listOf(project.configurations.compileOnly.get())
        classifier = null
        destinationDirectory.set(file("../jars"))
    }
}

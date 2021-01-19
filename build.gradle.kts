import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
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
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val shadowImplementation by configurations.creating
    configurations["implementation"].extendsFrom(shadowImplementation)

    repositories {
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    tasks.withType<ShadowJar> {
        configurations = listOf(shadowImplementation)
        archiveClassifier.set("")
        destinationDirectory.set(file("../jars"))
    }

    configure<KtlintExtension> {
        version.set("0.40.0")
    }
}

task("updateVersions") {
    doLast {
        val outputBlockComment = "<!-- Generate Versions -->"
        val escapedOutputBlockComment = Regex.escape(outputBlockComment)
        val mdFile = file("README.md")
        val fileContent = mdFile.bufferedReader().use { it.readText() }
        mdFile.writeText(
            fileContent.replace(
                "$escapedOutputBlockComment[\\s\\S]*$escapedOutputBlockComment".toRegex(),
                buildString {
                    appendln(outputBlockComment)
                    appendln(
                        """
                    | Name | Version |
                    |:-----|--------:|
                        """.trimIndent()
                    )
                    Project.list.sortedBy { it.name }.forEach {
                        appendln("| ${it.name} | ${it.version} |")
                    }
                    append(outputBlockComment)
                }
            )
        )
    }
}

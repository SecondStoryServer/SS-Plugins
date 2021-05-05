import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

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
        version = project.version
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
                    | Name | Version | Dependency |
                    |:-----|--------:|-----------:|
                        """.trimIndent()
                    )
                    Project.list.sortedBy { it.name }.forEach {
                        val (buildVersion, dependencyVersion) = if (it.version.contains('(')) {
                            val separateIndex = it.version.indexOf('(')
                            val buildVersion = it.version.substring(0, separateIndex)
                            val dependencyVersion = it.version.substring(separateIndex + 1, it.version.length - 1)
                            buildVersion to dependencyVersion
                        } else {
                            it.version to ""
                        }
                        appendln("| ${it.name} | $buildVersion | $dependencyVersion |")
                    }
                    append(outputBlockComment)
                }
            )
        )
    }
}

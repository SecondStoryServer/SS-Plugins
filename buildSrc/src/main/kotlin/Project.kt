@file:Suppress("unused", "MemberVisibilityCanBePrivate")

open class Project(val version: String, groupName: String = "") {
    companion object {
        val list = mutableListOf<Project>()
    }

    constructor(buildVersion: Int, groupName: String = "") : this(buildVersion.toString(), groupName)
    constructor(buildVersion: Int, dependencyVersion: String, groupName: String = "") : this("$buildVersion($dependencyVersion)", groupName)

    private val simpleName = javaClass.simpleName
    val name by lazy { "SS-${if (groupName.isEmpty()) "" else "$groupName-"}$simpleName" }
    val group by lazy { "com.github.syari.ss.plugins.${if (groupName.isEmpty()) "" else "${groupName.toLowerCase()}."}${simpleName.toLowerCase()}" }
    val main = "$group.Main"
    val author = "sya_ri"
    val apiVersion = "1.16"
    open val dependProject = listOf<Project>()
    open val dependPlugin = listOf<String>()
    val dependProjectName by lazy { dependProject.map { it.name } }
    val allDependPlugin by lazy { dependProjectName + dependPlugin }
    open val softDependProject = listOf<Project>()
    open val softDependPlugin = listOf<String>()
    val softDependProjectName by lazy { softDependProject.map { it.name } }
    val allSoftDependPlugin by lazy { softDependProjectName + softDependPlugin }
    val implementationProjects by lazy { dependProjectName + softDependProjectName }

    init {
        list.add(this)
    }

    object Assist : Project(3) {
        override val dependProject = listOf(Core)
    }

    object Backup : Project(14) {
        override val dependProject = listOf(Core)
    }

    object CommandBlocker : Project(3) {
        override val dependProject = listOf(Core)
    }

    object Core : Project(32) {
        override val dependProject = listOf(Kotlin)
    }

    open class Dependency(buildVersion: Int, version: String) : Project(buildVersion, version, "Dependency") {
        object CrackShot : Dependency(1, "0.98.11") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShot")
        }

        object CrackShotPlus : Dependency(1, "1.97") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShotPlus")
        }

        object MythicMobs : Dependency(1, "4.11.0-beta-1") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("MythicMobs")
        }
    }

    object DevelopAssist : Project(3) {
        override val dependProject = listOf(Core)
    }

    object GlobalPlayers : Project(5) {
        override val dependProject = listOf(Core)
    }

    object Discord : Project(2) {
        override val dependProject = listOf(Core)
    }

    object ItemFrameCommand : Project(3) {
        override val dependProject = listOf(Core)
    }

    object Kotlin : Project(1, dependencyVersion = "1.4.30")

    object Lobby : Project(2) {
        override val dependProject = listOf(Core)
    }

    object MobArena : Project(19) {
        override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs, PlayerDataStore)
    }

    object PlayerDataStore : Project(11) {
        override val dependProject = listOf(Core)
    }

    object PluginManager : Project(9) {
        override val dependProject = listOf(Core)
    }
}

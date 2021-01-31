@file:Suppress("unused", "MemberVisibilityCanBePrivate")

open class Project(val version: String, groupName: String = "") {
    companion object {
        val list = mutableListOf<Project>()
    }

    constructor(buildVersion: Int) : this(buildVersion.toString())

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

    object Assist : Project(1) {
        override val dependProject = listOf(Core)
    }

    object Backup : Project(13) {
        override val dependProject = listOf(Core)
    }

    object CommandBlocker : Project(2) {
        override val dependProject = listOf(Core)
    }

    object Core : Project(31) {
        override val dependProject = listOf(Kotlin)
    }

    open class Dependency(version: String) : Project(version, "Dependency") {
        object CrackShot : Dependency("0.98.11") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShot")
        }

        object CrackShotPlus : Dependency("1.97") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShotPlus")
        }

        object MythicMobs : Dependency("4.11.0-beta-1") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("MythicMobs")
        }
    }

    object DevelopAssist : Project(2) {
        override val dependProject = listOf(Core)
    }

    object GlobalPlayers : Project(4) {
        override val dependProject = listOf(Core)
    }

    object Discord : Project(1) {
        override val dependProject = listOf(Core)
    }

    object ItemFrameCommand : Project(2) {
        override val dependProject = listOf(Core)
    }

    object Kotlin : Project("1.4.21")

    object Lobby : Project(1) {
        override val dependProject = listOf(Core)
    }

    object MobArena : Project(17) {
        override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs, PlayerDataStore)
    }

    object PlayerDataStore : Project(9) {
        override val dependProject = listOf(Core, Assist)
    }

    object PluginManager : Project(8) {
        override val dependProject = listOf(Core)
    }
}

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

open class Project(val version: String, groupName: String = "") {
    companion object {
        val list = listOf(
            Assist,
            Backup,
            CommandBlocker,
            Core,
            DemonKill,
            Dependency.CrackShot,
            Dependency.CrackShotPlus,
            Dependency.MythicMobs,
            DevelopAssist,
            GlobalPlayers,
            ItemFrameCommand,
            Lobby,
            MobArena,
            PlayerDataStore
        )

        fun get(name: String) = list.firstOrNull { it.name == name }
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
    open val dependJarFile = listOf<String>()
    val softDependProjectName by lazy { softDependProject.map { it.name } }
    val allSoftDependPlugin by lazy { softDependProjectName + softDependPlugin }
    val implementationProjects by lazy { dependProjectName + softDependProjectName }

    object Assist : Project(6) {
        override val dependProject = listOf(Core)
    }

    object Backup : Project(16) {
        override val dependProject = listOf(Core)
    }

    object CommandBlocker : Project(6) {
        override val dependProject = listOf(Core)
    }

    object Core : Project(36) {
        override val dependJarFile = listOf("patched_1.16.5")
    }

    object DemonKill : Project(1) {
        override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs)
    }

    open class Dependency(buildVersion: Int, version: String) : Project(buildVersion, version, "Dependency") {
        object CrackShot : Dependency(1, "0.98.11") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShot")
            override val dependJarFile = listOf("CrackShot")
        }

        object CrackShotPlus : Dependency(1, "1.97") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShotPlus")
            override val dependJarFile = listOf("CrackShotPlus")
        }

        object MythicMobs : Dependency(1, "4.11.0-beta-1") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("MythicMobs")
            override val dependJarFile = listOf("MythicMobs-4.11.0-BETA")
        }
    }

    object DevelopAssist : Project(4) {
        override val dependProject = listOf(Core)
    }

    object GlobalPlayers : Project(7) {
        override val dependProject = listOf(Core)
    }

    object ItemFrameCommand : Project(4) {
        override val dependProject = listOf(Core)
    }

    object Lobby : Project(6) {
        override val dependProject = listOf(Core)
    }

    object MobArena : Project(24) {
        override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs, PlayerDataStore)
    }

    object PlayerDataStore : Project(13) {
        override val dependProject = listOf(Core)
    }
}

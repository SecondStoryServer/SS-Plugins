@file:Suppress("unused", "MemberVisibilityCanBePrivate")

open class Project(groupName: String = "") {
    companion object {
        val list = listOf(
            Assist,
            Backup,
            CommandBlocker,
            Core,
            DemonKill,
            Dependency.CrackShotPlus,
            Dependency.MythicMobs,
            DevelopAssist,
            Event.AcrobatSniper,
            GlobalPlayers,
            Lobby,
            MobArena,
            RPG.Core
        )

        fun get(name: String) = list.firstOrNull { it.name == name }
    }

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

    object Assist : Project() {
        override val dependProject = listOf(Core)
    }

    object Backup : Project() {
        override val dependProject = listOf(Core)
    }

    object CommandBlocker : Project() {
        override val dependProject = listOf(Core)
    }

    object Core : Project() {
        override val dependJarFile = listOf("1.16.5-patched")
    }

    object DemonKill : Project() {
        override val dependProject = listOf(Core, Dependency.CrackShotPlus, Dependency.MythicMobs)
    }

    open class Dependency : Project("Dependency") {
        object CrackShotPlus : Dependency() {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShotPlus")
            override val dependJarFile = listOf("CrackShot", "CrackShotPlus")
        }

        object MythicMobs : Dependency() {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("MythicMobs")
            override val dependJarFile = listOf("MythicMobs-4.11.0-build-3560")
        }
    }

    object DevelopAssist : Project() {
        override val dependProject = listOf(Core)
    }

    open class Event : Project("Event") {
        object AcrobatSniper : Event() {
            override val dependProject = listOf(Core)
        }
    }

    object GlobalPlayers : Project() {
        override val dependProject = listOf(Core)
    }

    object Lobby : Project() {
        override val dependProject = listOf(Core)
    }

    object MobArena : Project() {
        override val dependProject = listOf(Core, Dependency.CrackShotPlus, Dependency.MythicMobs)
    }

    open class RPG : Project("RPG") {
        object Core : RPG() {
            override val dependProject = listOf(Project.Core)
        }
    }
}

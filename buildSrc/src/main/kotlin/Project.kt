@file:Suppress("unused", "MemberVisibilityCanBePrivate")

open class Project(groupName: String = "", val isProxyPlugin: Boolean = false) {
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
            RPG.Core,
            WCore
        )

        fun get(name: String) = list.firstOrNull { it.name == name }
    }

    private val simpleName = javaClass.simpleName
    private val projectName by lazy { if (isProxyPlugin) simpleName.substring(1) else simpleName }
    val name by lazy { "SS-${if (isProxyPlugin) "W-" else ""}${if (groupName.isEmpty()) "" else "$groupName-"}$projectName" }
    val group by lazy { "com.github.syari.ss.${if (isProxyPlugin) "w" else ""}plugins.${if (groupName.isEmpty()) "" else "${groupName.toLowerCase()}."}${projectName.toLowerCase()}" }
    val main = "$group.Main"
    val author = "sya_ri"
    open val dependProject = listOf<Project>()
    open val dependPlugin = listOf<String>()
    val dependProjectName by lazy { dependProject.map { it.name } }
    val allDependPlugin by lazy { dependProjectName + dependPlugin }
    open val dependJarFile = listOf<String>()
    val implementationProjects by lazy { dependProjectName }

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

    object WCore : Project(isProxyPlugin = true)
}

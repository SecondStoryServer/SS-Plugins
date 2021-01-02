interface Project {
    companion object {
        private const val group = "com.github.syari.ss.plugins"

        fun subgroup(path: String) = "$group.$path"

        fun build(number: Int) = number.toString()
    }

    val name: String
    val version: String
    val group: String
    val main: String
        get() = "$group.Main"
    val author: String
        get() = "sya_ri"
    val apiVersion: String
        get() = "1.16"
    val dependProject: List<Project>
    val dependProjectName: List<String>
        get() = dependProject.map { it.name }
    val dependPlugin: List<String>
        get() = dependProjectName + extraDependPlugin
    val extraDependPlugin: List<String>
        get() = listOf()

    object Backup: Project {
        override val name = "SS-Backup"
        override val version = build(6)
        override val group = subgroup("backup")
        override val dependProject = listOf(Core)
    }

    object CommandBlocker: Project {
        override val name = "SS-CommandBlocker"
        override val version = build(1)
        override val group = subgroup("commandblocker")
        override val dependProject = listOf(Core)
    }

    object Core: Project {
        override val name = "SS-Core"
        override val version = build(10)
        override val group = subgroup("core")
        override val dependProject = listOf(Kotlin)
    }

    interface Dependency: Project {
        object AdvancedMobArena: Dependency {
            override val name = "SS-Dependency-AdvancedMobArena"
            override val version = "7.22"
            override val group = subgroup("dependency.advancedmobarena")
            override val dependProject = listOf(Core)
            override val extraDependPlugin = listOf("AdvancedMobArena")
        }

        object CrackShot: Dependency {
            override val name = "SS-Dependency-CrackShot"
            override val version = "0.98.9"
            override val group = subgroup("dependency.crackshot")
            override val dependProject = listOf(Core)
            override val extraDependPlugin = listOf("CrackShot")
        }

        object CrackShotPlus: Dependency {
            override val name = "SS-Dependency-CrackShotPlus"
            override val version = "1.97"
            override val group = subgroup("dependency.crackshotplus")
            override val dependProject = listOf(Core)
            override val extraDependPlugin = listOf("CrackShotPlus")
        }

        object MythicMobs: Dependency {
            override val name = "SS-Dependency-MythicMobs"
            override val version = "4.11.0-build-3527"
            override val group = subgroup("dependency.mythicmobs")
            override val dependProject = listOf(Core)
            override val extraDependPlugin = listOf("MythicMobs")
        }
    }

    object GlobalPlayers: Project {
        override val name = "SS-GlobalPlayers"
        override val version = build(2)
        override val group = subgroup("globalplayers")
        override val dependProject = listOf(Core)
    }

    object Discord: Project {
        override val name = "SS-Discord"
        override val version = build(1)
        override val group = subgroup("discord")
        override val dependProject = listOf(Core)
    }

    object ItemCreator: Project {
        override val name = "SS-ItemCreator"
        override val version = build(5)
        override val group = subgroup("itemcreator")
        override val dependProject = listOf(Core)
    }

    object Kotlin: Project {
        override val name = "SS-Kotlin"
        override val version = "1.4.21"
        override val group = subgroup("kotlin")
        override val dependProject = listOf<Project>()
    }

    interface MA: Project {
        object Item: MA {
            override val name = "SS-MA-Item"
            override val version = build(4)
            override val group = subgroup("ma.item")
            override val dependProject = listOf(Core, Dependency.AdvancedMobArena)
        }

        object Kit: MA {
            override val name = "SS-MA-Kit"
            override val version = build(3)
            override val group = subgroup("ma.kit")
            override val dependProject = listOf(Core, Dependency.AdvancedMobArena)
        }

        object Mob: MA {
            override val name = "SS-MA-Mob"
            override val version = build(2)
            override val group = subgroup("ma.mob")
            override val dependProject = listOf(Core, Dependency.AdvancedMobArena, Dependency.MythicMobs)
        }

        object Shop: MA {
            override val name = "SS-MA-Shop"
            override val version = build(7)
            override val group = subgroup("ma.shop")
            override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs)
        }
    }

    object MaterialChecker: Project {
        override val name = "SS-MaterialChecker"
        override val version = build(4)
        override val group = subgroup("materialchecker")
        override val dependProject = listOf(Core)
    }

    object PluginManager: Project {
        override val name = "SS-PluginManager"
        override val version = build(6)
        override val group = subgroup("pluginmanager")
        override val dependProject = listOf(Core)
    }

    object SoundChecker: Project {
        override val name = "SS-SoundChecker"
        override val version = build(3)
        override val group = subgroup("soundchecker")
        override val dependProject = listOf(Core)
    }
}
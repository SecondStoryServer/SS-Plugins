open class Project(val version: String, groupName: String = "") {
    constructor(buildVersion: Int): this(buildVersion.toString())

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

    object AutoCommand: Project(2) {
        override val dependProject = listOf(Core)
    }

    object Backup: Project(9) {
        override val dependProject = listOf(Core)
    }

    object CommandBlocker: Project(1) {
        override val dependProject = listOf(Core)
    }

    object Core: Project(17) {
        override val dependProject = listOf(Kotlin)
    }

    open class Dependency(version: String): Project(version, "Dependency") {
        object CrackShot: Dependency("0.98.9") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShot")
        }

        object CrackShotPlus: Dependency("1.97") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("CrackShotPlus")
        }

        object MythicMobs: Dependency("4.11.0-build-3527") {
            override val dependProject = listOf(Core)
            override val dependPlugin = listOf("MythicMobs")
        }
    }

    object GlobalPlayers: Project(3) {
        override val dependProject = listOf(Core)
    }

    object Discord: Project(1) {
        override val dependProject = listOf(Core)
    }

    object ItemCreator: Project(5) {
        override val dependProject = listOf(Core)
    }

    object ItemFrameCommand: Project(1) {
        override val dependProject = listOf(Core)
    }

    object Kotlin: Project("1.4.21")

    object MaterialChecker: Project(4) {
        override val dependProject = listOf(Core)
    }

    object MobArena: Project(2) {
        override val dependProject = listOf(Core, Dependency.CrackShot, Dependency.CrackShotPlus, Dependency.MythicMobs)
    }

    object PlayerDataStore: Project(1) {
        override val dependProject = listOf(Core)
    }

    object PluginManager: Project(6) {
        override val dependProject = listOf(Core)
    }

    object SoundChecker: Project(3) {
        override val dependProject = listOf(Core)
    }
}
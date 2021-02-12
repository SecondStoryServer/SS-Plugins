val project = Project.Core
group = project.group

dependencies {
    shadowImplementation(kotlin("stdlib-jdk8"))
    api(files("dependencyJar/patched_1.16.5.jar"))
    shadowApi("com.github.sya-ri:EasySpigotAPI:1.2.0") {
        exclude(group = "org.spigotmc", module = "spigot-api")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.allDependPlugin
    softDepend = project.allSoftDependPlugin
}

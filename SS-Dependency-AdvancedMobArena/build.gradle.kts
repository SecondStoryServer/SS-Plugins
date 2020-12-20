val project = Project.Dependency.AdvancedMobArena
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    api(files("dependencyJar/AdvancedMobArena.jar"))
    api(files("dependencyJar/NexEngine.jar"))
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

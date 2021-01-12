val project = Project.MobArena
group = project.group

dependencies {
    project.implementationProjects.forEach { implementation(project(":$it")) }
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

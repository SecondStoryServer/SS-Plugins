val project = Project.Core
group = project.group

dependencies {
    api(files("dependencyJar/patched_1.16.4.jar"))
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

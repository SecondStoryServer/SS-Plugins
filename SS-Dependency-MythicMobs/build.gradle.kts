val project = Project.Dependency.MythicMobs
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    api(files("dependencyJar/MythicMobs-4.11.0-SNAPSHOT.jar"))
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

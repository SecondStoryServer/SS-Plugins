val project = Project.Dependency.NexEngine
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    implementation(files("dependencyJar/NexEngine.jar"))
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

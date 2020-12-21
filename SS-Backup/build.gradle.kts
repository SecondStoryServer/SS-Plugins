val project = Project.Backup
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    compileOnly("io.ktor:ktor-client-okhttp:1.4.3")
    compileOnly("io.ktor:ktor-client-auth:1.4.3")
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

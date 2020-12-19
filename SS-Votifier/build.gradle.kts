val project = Project.Votifier
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    implementation("io.netty:netty-handler:4.1.49.Final")
    implementation("io.netty", "netty-transport-native-epoll", "4.1.49.Final", classifier = "linux-x86_64")
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

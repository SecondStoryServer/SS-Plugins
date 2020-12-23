val project = Project.Discord
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("io.ktor:ktor-client-okhttp:1.4.0")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

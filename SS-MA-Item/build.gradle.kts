plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

val project = Project.MA.Item
group = project.group
version = project.version

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependProjectName
}

val jar by tasks.getting(Jar::class) {
    from(configurations.compileOnly.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    destinationDir = file("../jars")
}

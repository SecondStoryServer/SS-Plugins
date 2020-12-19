plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

val project = Project.Dependency.CrackShotPlus
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
    implementation(files("dependencyJar/CrackShotPlus.jar"))
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

val jar by tasks.getting(Jar::class) {
    from(configurations.compileOnly.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    destinationDirectory.file("../jars")
}

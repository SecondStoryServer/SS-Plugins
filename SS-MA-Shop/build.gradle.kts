plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

val project = Project.MA.Shop
group = project.group

dependencies {
    project.dependProjectName.forEach { implementation(project(":$it")) }
}

bukkit {
    name = project.name
    version = project.version
    main = project.main
    author = project.author
    apiVersion = project.apiVersion
    depend = project.dependPlugin
}

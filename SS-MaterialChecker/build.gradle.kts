plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = Project.subgroup("checker.material")
version = "1.0.0"

dependencies {
    implementation(project(":SS-Core"))
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "$group.Main"
    author = "sya_ri"
    apiVersion = "1.16"
    depend = listOf("SS-Core")
}

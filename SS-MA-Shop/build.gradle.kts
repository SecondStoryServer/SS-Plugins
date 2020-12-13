plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = Project.subgroup("ma.shop")
version = "1.0.0"

dependencies {
    implementation(project(":SS-Core"))
    implementation(project(":SS-Dependency-CrackShot"))
    implementation(project(":SS-Dependency-CrackShotPlus"))
    implementation(project(":SS-Dependency-MythicMobs"))
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "$group.Main"
    author = "sya_ri"
    apiVersion = "1.16"
    depend = listOf("SS-Core")
}

val jar by tasks.getting(Jar::class) {
    from(configurations.compileOnly.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    destinationDir = file("../jars")
}

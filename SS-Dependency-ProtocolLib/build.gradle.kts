plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = Project.subgroup("dependency.protocollib")
version = "4.6.0-build-483"

dependencies {
    implementation(project(":SS-Core"))
    api(files("dependencyJar/ProtocolLib.jar"))
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

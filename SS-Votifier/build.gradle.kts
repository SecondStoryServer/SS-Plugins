plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = Project.subgroup("votifier")
version = "1.1.3"

dependencies {
    implementation(project(":SS-Core"))
    implementation("io.netty:netty-handler:4.1.49.Final")
    implementation("io.netty", "netty-transport-native-epoll", "4.1.49.Final", classifier = "linux-x86_64")
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "$group.Main"
    author = "sya_ri"
    depend = listOf("SS-Core")
    apiVersion = "1.16"
}

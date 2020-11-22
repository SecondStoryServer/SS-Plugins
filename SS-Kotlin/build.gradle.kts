plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = "com.github.syari.ss.plugins.kotlin"
version = "1.4.10"

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "$group.Main"
    author = "sya_ri"
    apiVersion = "1.16"
    commands {
        register("ss-kotlin") {
            description = "Display Kotlin Version & Package"
        }
    }
}

val jar by tasks.getting(Jar::class) {
    from(configurations.compileOnly.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
}

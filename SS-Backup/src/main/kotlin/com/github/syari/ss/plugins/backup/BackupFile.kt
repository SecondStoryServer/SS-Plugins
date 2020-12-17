package com.github.syari.ss.plugins.backup

import java.io.File

sealed class BackupFile(val file: File) {
    class World(world: org.bukkit.World): BackupFile(world.worldFolder)
    class Plugin(plugin: org.bukkit.plugin.Plugin): BackupFile(plugin.dataFolder)
    class Other(file: File): BackupFile(file)
}

package com.github.syari.ss.plugins.core.persistentData

import org.bukkit.plugin.java.JavaPlugin

interface CustomPersistentDataContainer {
    fun <E> editPersistentData(
        plugin: JavaPlugin, action: CustomPersistentData.() -> E
    ): E?

    fun getPersistentData(plugin: JavaPlugin): CustomPersistentData?
}

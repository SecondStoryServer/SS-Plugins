package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import com.github.syari.ss.plugins.pluginmanager.PluginManager.pluginName
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import java.io.File
import java.util.jar.JarFile

object PluginManager {
    private inline val pluginManager
        get() = Bukkit.getPluginManager()

    private inline val pluginsDirectory
        get() = plugin.dataFolder.parentFile

    private inline val dependPluginMap
        get() = pluginManager.plugins.associateWith { it.description.depend }

    fun getPlugin(name: String) = pluginManager.getPlugin(name)

    fun getPluginJar(name: String) = pluginsDirectory.listFiles()?.firstOrNull {
        it.isFile && it.pluginName.equals(name, true)
    }

    fun getPluginsDependOn(plugin: Plugin): Set<Plugin> {
        return dependPluginMap.filter { it.value.contains(plugin.name) }.keys
    }

    val enabledPlugins
        get() = pluginManager.plugins.filterNot(Plugin::isEnabled)

    val disabledPlugins
        get() = pluginManager.plugins.filter(Plugin::isEnabled)

    val unloadPluginNames
        get() = pluginsDirectory.listFiles()?.pluginNames.orEmpty()

    private inline val Array<File>.pluginNames
        get() = plugins.mapNotNull { it.pluginName }

    private inline val Array<File>.plugins
        get() = filter { it.extension == "jar" }

    val File.pluginName
        get() = JarFile(this).use { jar ->
            jar.getJarEntry("plugin.yml")?.let {
                PluginDescriptionFile(jar.getInputStream(it)).name
            }
        }

    fun enable(plugin: Plugin) {
        pluginManager.enablePlugin(plugin)
    }

    fun disable(plugin: Plugin) {
        pluginManager.disablePlugin(plugin)
    }

    fun isLoaded(name: String) = getPlugin(name) != null

    fun load(jar: File) {
        pluginManager.loadPlugin(jar)
    }
}

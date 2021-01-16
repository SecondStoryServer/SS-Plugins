package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.plugin.InvalidDescriptionException
import org.bukkit.plugin.InvalidPluginException
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

    enum class LoadResult {
        Success, InvalidDescription, InvalidPlugin
    }

    fun load(jar: File): LoadResult {
        pluginManager.loadPlugin(jar)
        val plugin = try {
            Bukkit.getPluginManager().loadPlugin(jar)!!
        } catch (e: InvalidDescriptionException) {
            e.printStackTrace()
            return LoadResult.InvalidDescription
        } catch (e: InvalidPluginException) {
            e.printStackTrace()
            return LoadResult.InvalidPlugin
        }
        plugin.onLoad()
        Bukkit.getPluginManager().enablePlugin(plugin)
        return LoadResult.Success
    }
}

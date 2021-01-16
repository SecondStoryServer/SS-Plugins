package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Event
import org.bukkit.plugin.InvalidDescriptionException
import org.bukkit.plugin.InvalidPluginException
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.RegisteredListener
import java.io.File
import java.net.URLClassLoader
import java.util.SortedSet
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

    val loadedPluginNames
        get() = pluginManager.plugins.map(Plugin::getName)

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

    @Suppress("UNCHECKED_CAST")
    fun unload(plugin: Plugin): Boolean {
        if (plugin.isEnabled) {
            disable(plugin)
        }
        try {
            val pluginsField = pluginManager.javaClass.getDeclaredField("plugins")
            pluginsField.isAccessible = true
            val plugins = pluginsField.get(pluginManager) as MutableList<Plugin>
            val lookupNamesField = pluginManager.javaClass.getDeclaredField("lookupNames")
            lookupNamesField.isAccessible = true
            val lookupNames = lookupNamesField.get(pluginManager) as MutableMap<String, Plugin>
            if (plugins.contains(plugin)) plugins.remove(plugin)
            if (lookupNames.containsKey(plugin.name)) lookupNames.remove(plugin.name)
            pluginsField.set(pluginManager, plugins)
            lookupNamesField.set(pluginManager, lookupNames)
            try {
                try {
                    val listenersField = pluginManager.javaClass.getDeclaredField("listeners")
                    listenersField.isAccessible = true
                    val listeners = listenersField.get(pluginManager) as MutableMap<Event, SortedSet<RegisteredListener>>
                    val toRemoveFromListeners = arrayListOf<Event>()
                    listeners.forEach { (event, listenerSet) ->
                        listenerSet.forEach {
                            if (it.plugin === plugin) {
                                toRemoveFromListeners.add(event)
                            }
                        }
                    }
                    toRemoveFromListeners.forEach(listeners::remove)
                    listenersField.set(pluginManager, listeners)
                } catch (e: Exception) {
                }

                val commandMapField = pluginManager.javaClass.getDeclaredField("commandMap")
                commandMapField.isAccessible = true
                val commandMap = commandMapField.get(pluginManager) as CommandMap
                val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
                knownCommandsField.isAccessible = true
                val knownCommands = knownCommandsField.get(commandMap) as MutableMap<String, Command>
                val toRemoveFromKnownCommands = arrayListOf<String>()
                knownCommands.forEach { (name, command) ->
                    if (command is PluginCommand && command.plugin === plugin) {
                        command.unregister(commandMap)
                        toRemoveFromKnownCommands.add(name)
                    }
                }
                toRemoveFromKnownCommands.forEach(knownCommands::remove)
                knownCommandsField.set(commandMap, knownCommands)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val classLoader = pluginManager.javaClass.classLoader
            if (classLoader is URLClassLoader) {
                val pluginField = classLoader.javaClass.getDeclaredField("plugin")
                pluginField.isAccessible = true
                pluginField.set(classLoader, null)
                val pluginInitField = classLoader.javaClass.getDeclaredField("pluginInit")
                pluginInitField.isAccessible = true
                pluginInitField.set(classLoader, null)
                classLoader.close()
            }
            System.gc()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun reload(plugin: Plugin): Boolean {
        unload(plugin)
        return getPluginJar(plugin.name)?.let(::load) != null
    }
}

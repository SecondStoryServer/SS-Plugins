package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Event
import org.bukkit.plugin.InvalidDescriptionException
import org.bukkit.plugin.InvalidPluginException
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.RegisteredListener
import java.io.File
import java.io.IOException
import java.net.URLClassLoader
import java.util.SortedSet
import java.util.jar.JarFile
import java.util.logging.Level
import java.util.logging.Logger

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
        val name = plugin.name
        val commandMap: SimpleCommandMap?
        val plugins: MutableList<Plugin?>?
        val names: MutableMap<String?, Plugin?>?
        val commands: MutableMap<String?, Command>?
        var listeners: Map<Event?, SortedSet<RegisteredListener>>? = null
        var reloadlisteners = true
        pluginManager.disablePlugin(plugin)
        try {
            val pluginsField = Bukkit.getPluginManager().javaClass.getDeclaredField("plugins")
            pluginsField.isAccessible = true
            plugins = pluginsField[pluginManager] as MutableList<Plugin?>
            val lookupNamesField = Bukkit.getPluginManager().javaClass.getDeclaredField("lookupNames")
            lookupNamesField.isAccessible = true
            names = lookupNamesField[pluginManager] as MutableMap<String?, Plugin?>
            try {
                val listenersField = Bukkit.getPluginManager().javaClass.getDeclaredField("listeners")
                listenersField.isAccessible = true
                listeners = listenersField[pluginManager] as Map<Event?, SortedSet<RegisteredListener>>
            } catch (e: Exception) {
                reloadlisteners = false
            }
            val commandMapField = Bukkit.getPluginManager().javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            commandMap = commandMapField[pluginManager] as SimpleCommandMap
            val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            knownCommandsField.isAccessible = true
            commands = knownCommandsField[commandMap] as MutableMap<String?, Command>
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            return false
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return false
        }
        pluginManager.disablePlugin(plugin)
        if (plugins.contains(plugin)) plugins.remove(plugin)
        if (names.containsKey(name)) names.remove(name)
        if (listeners != null && reloadlisteners) {
            for (set in listeners.values) {
                val it = set.iterator()
                while (it.hasNext()) {
                    val value = it.next()
                    if (value.plugin === plugin) {
                        it.remove()
                    }
                }
            }
        }
        val iterator = commands.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value is PluginCommand) {
                val c = entry.value as PluginCommand
                if (c.plugin === plugin) {
                    c.unregister(commandMap)
                    iterator.remove()
                }
            }
        }

        // Attempt to close the classloader to unlock any handles on the plugin's jar file.
        val cl = plugin.javaClass.classLoader
        if (cl is URLClassLoader) {
            try {
                val pluginField = cl.javaClass.getDeclaredField("plugin")
                pluginField.isAccessible = true
                pluginField[cl] = null
                val pluginInitField = cl.javaClass.getDeclaredField("pluginInit")
                pluginInitField.isAccessible = true
                pluginInitField[cl] = null
            } catch (ex: NoSuchFieldException) {
                Logger.getLogger(PluginManager::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: SecurityException) {
                Logger.getLogger(PluginManager::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: IllegalArgumentException) {
                Logger.getLogger(PluginManager::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: IllegalAccessException) {
                Logger.getLogger(PluginManager::class.java.name).log(Level.SEVERE, null, ex)
            }
            try {
                cl.close()
            } catch (ex: IOException) {
                Logger.getLogger(PluginManager::class.java.name).log(Level.SEVERE, null, ex)
            }
        }

        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
        // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
        System.gc()
        return true
    }

    fun reload(plugin: Plugin): Boolean {
        unload(plugin)
        return getPluginJar(plugin.name)?.let(::load) != null
    }
}

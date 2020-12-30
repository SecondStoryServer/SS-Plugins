package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import org.bukkit.plugin.PluginDescriptionFile
import java.io.File
import java.util.jar.JarFile

object CommandCreator: OnEnable {
    override fun onEnable() {
        val pluginsFolder = plugin.dataFolder.parentFile
        val pluginManager = plugin.server.pluginManager
        plugin.command("pluginmanager", "SS-PluginManager") {
            tab {
                arg { element("load", "enable", "disable") }
                arg("load") { element(pluginsFolder.listFiles()?.getPluginNames()?.filter { pluginManager.getPlugin(it) == null }) }
                arg("enable") { element(pluginManager.plugins.filterNot { it.isEnabled }.map { it.name }) }
                arg("disable") { element(pluginManager.plugins.filter { it.isEnabled }.map { it.name }) }
            }
            execute {
                val dependPluginMap = pluginManager.plugins.associateWith { it.description.depend }
                when (args.whenIndex(0)) {
                    "load" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        if (pluginManager.getPlugin(pluginName) != null) return@execute sendError("読み込まれているプラグインです")
                        val file = pluginsFolder.getPluginJar(pluginName) ?: return@execute sendError("プラグインが見つかりませんでした")
                        sendWithPrefix("&6${pluginName} &fを読み込みます")
                        pluginManager.loadPlugin(file)
                        sendWithPrefix("&6${pluginName} &fを読み込みました")
                    }
                    "enable" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        val plugin = pluginManager.getPlugin(pluginName) ?: return@execute sendError("存在しないプラグインです")
                        if (plugin.isEnabled) return@execute sendError("既に有効化されています")
                        sendWithPrefix("&6${plugin.name} &fを有効化します")
                        pluginManager.enablePlugin(plugin)
                        sendWithPrefix("&6${plugin.name} &fを有効化しました")
                    }
                    "disable" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        val plugin = pluginManager.getPlugin(pluginName) ?: return@execute sendError("存在しないプラグインです")
                        val dependOnPlugin = dependPluginMap.filter { it.value.contains(plugin.name) }.keys
                        if (dependOnPlugin.isEmpty()) {
                            sendWithPrefix("&6${plugin.name} &fを無効化します")
                            pluginManager.disablePlugin(plugin)
                            sendWithPrefix("&6${plugin.name} &fを無効化しました")
                        } else {
                            sendError("&c依存しているプラグインがあるので無効化できません ${dependOnPlugin.joinToString("\n&6", "\n") { it.name }}")
                        }
                    }
                    else -> sendHelp(
                        "pluginmanager load" to "読み込まれていないプラグインをロードします", "pluginmanager enable" to "プラグインを有効化します", "pluginmanager disable" to "プラグインを無効化します"
                    )
                }
            }
        }
    }

    private fun Array<File>.getPluginNames() = mapNotNull {
        if (it.extension == "jar") {
            it.getPluginName()
        } else {
            null
        }
    }

    private fun File.getPluginName() = JarFile(this).use { jar ->
        jar.getJarEntry("plugin.yml")?.let {
            PluginDescriptionFile(jar.getInputStream(it)).name
        }
    }

    private fun File.getPluginJar(name: String) = listFiles()?.firstOrNull {
        it.isFile && it.getPluginName().equals(name, true)
    }
}
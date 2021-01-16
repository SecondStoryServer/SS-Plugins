package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin
import org.bukkit.plugin.Plugin

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("pluginmanager", "SS-PluginManager") {
            tab {
                arg { element("load", "enable", "disable") }
                arg("load") { element(PluginManager.unloadPluginNames) }
                arg("enable") { element(PluginManager.enabledPlugins.map(Plugin::getName)) }
                arg("disable") { element(PluginManager.disabledPlugins.map(Plugin::getName)) }
            }
            execute {
                when (args.whenIndex(0)) {
                    "load" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        if (PluginManager.isLoaded(pluginName)) return@execute sendError("読み込まれているプラグインです")
                        val file = PluginManager.getPluginJar(pluginName) ?: return@execute sendError("プラグインが見つかりませんでした")
                        sendWithPrefix("&6$pluginName &fを読み込みます")
                        when (PluginManager.load(file)) {
                            PluginManager.LoadResult.Success -> sendWithPrefix("&6$pluginName &fを読み込みました")
                            PluginManager.LoadResult.InvalidDescription -> sendError("&6$pluginName &cには plugin.yml が存在しません")
                            PluginManager.LoadResult.InvalidPlugin -> sendError("&6$pluginName &cは不正なプラグインです")
                        }
                    }
                    "enable" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        val plugin = PluginManager.getPlugin(pluginName) ?: return@execute sendError("存在しないプラグインです")
                        if (plugin.isEnabled) return@execute sendError("既に有効化されています")
                        sendWithPrefix("&6${plugin.name} &fを有効化します")
                        PluginManager.enable(plugin)
                        sendWithPrefix("&6${plugin.name} &fを有効化しました")
                    }
                    "disable" -> {
                        val pluginName = args.getOrNull(1) ?: return@execute sendError("プラグイン名を入力してください")
                        val plugin = PluginManager.getPlugin(pluginName) ?: return@execute sendError("存在しないプラグインです")
                        val dependOnPlugin = PluginManager.getPluginsDependOn(plugin)
                        if (dependOnPlugin.isEmpty()) {
                            sendWithPrefix("&6${plugin.name} &fを無効化します")
                            PluginManager.disable(plugin)
                            sendWithPrefix("&6${plugin.name} &fを無効化しました")
                        } else {
                            sendError("&c依存しているプラグインがあるので無効化できません ${dependOnPlugin.joinToString("\n&6", "\n") { it.name }}")
                        }
                    }
                    else -> sendHelp(
                        "pluginmanager load" to "読み込まれていないプラグインをロードします",
                        "pluginmanager load" to "プラグインを読み込みを停止します",
                        "pluginmanager enable" to "プラグインを有効化します",
                        "pluginmanager disable" to "プラグインを無効化します"
                    )
                }
            }
        }
        plugin.command("versions", "PluginManager") {
            execute {
                val plugins = plugin.server.pluginManager.plugins
                sendList("&fプラグイン一覧", plugins.map { "${it.name} &7${it.description.version}" }.sorted())
            }
        }
    }
}

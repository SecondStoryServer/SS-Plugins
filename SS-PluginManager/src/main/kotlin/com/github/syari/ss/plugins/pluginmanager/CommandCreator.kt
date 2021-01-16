package com.github.syari.ss.plugins.pluginmanager

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.pluginmanager.Main.Companion.plugin

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("versions", "PluginManager") {
            execute {
                val plugins = plugin.server.pluginManager.plugins
                sendList("&fプラグイン一覧", plugins.map { "${it.name} &7${it.description.version}" }.sorted())
            }
        }
    }
}

package com.github.syari.ss.wplugins.pluginmanager

import com.github.syari.ss.wplugins.core.code.SSPlugin
import com.github.syari.ss.wplugins.core.command.CommandCreator.Companion.command

class Main : SSPlugin() {
    override fun onEnable() {
        command("gversions", "PluginManager") {
            execute {
                val plugins = proxy.pluginManager.plugins
                sendList("&fプラグイン一覧", plugins.map { "${it.description.name} &7${it.description.version}" }.sorted())
            }
        }
    }
}

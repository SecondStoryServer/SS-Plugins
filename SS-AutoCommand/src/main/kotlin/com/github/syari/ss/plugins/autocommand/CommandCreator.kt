package com.github.syari.ss.plugins.autocommand

import com.github.syari.ss.plugins.autocommand.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("auto-command", "AutoCommand") {
            tab {
                arg { element("reload") }
            }
            execute {
                when (args.whenIndex(0)) {
                    "reload" -> {
                        sendWithPrefix("コンフィグを再読み込みします")
                        ConfigLoader.load(sender)
                    }
                }
            }
        }
    }
}

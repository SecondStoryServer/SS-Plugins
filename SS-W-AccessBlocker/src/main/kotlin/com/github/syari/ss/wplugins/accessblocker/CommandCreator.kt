package com.github.syari.ss.wplugins.accessblocker

import com.github.syari.ss.wplugins.accessblocker.Main.Companion.plugin
import com.github.syari.ss.wplugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.command.CommandCreator.Companion.command
import com.github.syari.ss.wplugins.core.command.CommandTabElement.Companion.element

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("access-blocker", "AccessBlocker") {
            tab {
                arg { element("reload") }
            }
            execute {
                when (args.whenIndex(0)) {
                    "reload" -> {
                        sendWithPrefix("コンフィグを再読み込みます")
                        ConfigLoader.load(sender)
                    }
                    else -> sendHelp(
                        "access-blocker reload" to "コンフィグを再読み込みします"
                    )
                }
            }
        }
    }
}

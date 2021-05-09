package com.github.syari.ss.wplugins.votifier

import com.github.syari.ss.wplugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.command.CommandCreator.Companion.command
import com.github.syari.ss.wplugins.core.command.CommandTabElement.Companion.element
import com.github.syari.ss.wplugins.votifier.BootstrapBuilder.reload
import com.github.syari.ss.wplugins.votifier.Main.Companion.plugin

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("votifier", "SS-Votifier") {
            tab {
                arg { element("reload") }
            }
            execute {
                when (args.whenIndex(0)) {
                    "reload" -> {
                        sendWithPrefix("コンフィグをリロードします")
                        reload(sender)
                    }
                    else -> {
                        sendHelp(
                            "votifier reload" to "コンフィグをリロードします"
                        )
                    }
                }
            }
        }
    }
}

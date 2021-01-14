package com.github.syari.ss.plugins.kotlin

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    private val kotlinDescription: Array<String>
        get() = """
                §6--------[ §a§lSS-Kotlin §6]--------
                §6 * §dkotlin-jvm §7- §dversion ${KotlinVersion.CURRENT}
                ${KotlinPackage.list.joinToString(separator = "\n") { "§6 * §d$it" }}
                §6-----------------------------
        """.trimIndent().lines().toTypedArray()

    override fun onLoad() {
        KotlinPackage.add("stdlib-jdk8")
    }

    override fun onEnable() {
        server.consoleSender.sendMessage(kotlinDescription)
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return if (sender.isOp) {
            sender.sendMessage(kotlinDescription)
            true
        } else {
            false
        }
    }
}

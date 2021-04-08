@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.code

import org.bukkit.plugin.java.JavaPlugin

open class SSPlugin : JavaPlugin() {
    open val onEnables = listOf<OnEnable>()
    open val onDisables = listOf<OnDisable>()

    override fun onEnable() {
        onEnables.forEach(OnEnable::onEnable)
    }

    override fun onDisable() {
        onDisables.forEach(OnDisable::onDisable)
    }
}

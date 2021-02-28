@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.code

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.EventRegister.Companion.registerEvents
import org.bukkit.plugin.java.JavaPlugin

open class SSPlugin : JavaPlugin() {
    open val events = listOf<EventRegister>()
    open val onEnables = listOf<OnEnable>()
    open val onDisables = listOf<OnDisable>()

    override fun onEnable() {
        registerEvents(*events.toTypedArray())
        onEnables.forEach(OnEnable::onEnable)
    }

    override fun onDisable() {
        onDisables.forEach(OnDisable::onDisable)
    }
}

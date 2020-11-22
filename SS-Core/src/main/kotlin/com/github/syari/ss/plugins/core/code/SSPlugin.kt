package com.github.syari.ss.plugins.core.code

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

open class SSPlugin: JavaPlugin() {
    open val listeners = listOf<Listener>()
    open val onEnables = listOf<OnEnable>()
    open val onDisables = listOf<OnDisable>()

    /**
     * ```
     * override val listeners = listOf(...)
     *
     * override fun onEnable(){
     *      registerListeners()
     * }
     * ```
     *
     * @see org.bukkit.plugin.PluginManager.registerEvents
     */
    fun registerListeners() {
        listeners.forEach {
            server.pluginManager.registerEvents(it, this@SSPlugin)
        }
    }

    /**
     * ```
     * override val onEnables = listOf(...)
     *
     * override fun onEnable(){
     *      runOnEnable()
     * }
     * ```
     */
    fun runOnEnable() {
        onEnables.forEach(OnEnable::onEnable)
    }

    /**
     * ```
     * override val onDisables = listOf(...)
     *
     * override fun onDisable(){
     *      runOnDisable()
     * }
     * ```
     */
    fun runOnDisable() {
        onDisables.forEach(OnDisable::onDisable)
    }
}
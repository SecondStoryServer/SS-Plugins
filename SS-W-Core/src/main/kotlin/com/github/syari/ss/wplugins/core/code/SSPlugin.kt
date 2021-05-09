package com.github.syari.ss.wplugins.core.code

import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

open class SSPlugin : Plugin() {
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
     * @see net.md_5.bungee.api.plugin.PluginManager.registerListener
     */
    fun registerListeners() {
        listeners.forEach {
            proxy.pluginManager.registerListener(this, it)
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

package com.github.syari.ss.plugins.developassist

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.developassist.itemcreator.ItemCreator
import com.github.syari.ss.plugins.developassist.soundchecker.SoundChecker
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val onEnables = listOf(ItemCreator, SoundChecker)

    override fun onEnable() {
        plugin = this
        runOnEnable()
    }
}

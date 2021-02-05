package com.github.syari.ss.plugins.core.code

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

interface EventRegister {
    /**
     * [ListenerFunctions.event] を実行できます
     */
    fun ListenerFunctions.events()
}

class ListenerFunctions(val plugin: JavaPlugin) : Listener {
    inline fun <reified T : Event> event(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        crossinline action: (T) -> Unit
    ) {
        plugin.server.pluginManager.registerEvent(
            T::class.java,
            this,
            priority,
            { _, event ->
                (event as? T)?.let(action)
            },
            plugin,
            ignoreCancelled
        )
    }

    inline fun <reified T> cancelEvent(
        priority: EventPriority = EventPriority.NORMAL,
        crossinline action: (T) -> Boolean
    ) where T : Event, T : Cancellable {
        event<T>(priority, true) {
            it.isCancelled = action(it)
        }
    }
}

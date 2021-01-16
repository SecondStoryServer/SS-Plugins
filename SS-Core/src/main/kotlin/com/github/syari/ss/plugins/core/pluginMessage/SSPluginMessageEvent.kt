@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.pluginMessage

import com.github.syari.ss.plugins.core.event.CustomEvent
import com.github.syari.ss.template.message.PluginMessageTemplate

class SSPluginMessageEvent(val template: PluginMessageTemplate) : CustomEvent()

package com.github.syari.ss.plugins.core.pluginMessage

import com.github.syari.ss.plugins.core.event.CustomEvent
import com.github.syari.ss.wplugins.core.pluginMessage.template.PluginMessageTemplate

class SSPluginMessageEvent(val template: PluginMessageTemplate): CustomEvent()
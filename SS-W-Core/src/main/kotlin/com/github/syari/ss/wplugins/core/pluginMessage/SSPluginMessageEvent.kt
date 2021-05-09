package com.github.syari.ss.wplugins.core.pluginMessage

import com.github.syari.ss.template.message.PluginMessageTemplate
import com.github.syari.ss.wplugins.core.event.CustomEvent

class SSPluginMessageEvent(val template: PluginMessageTemplate) : CustomEvent()

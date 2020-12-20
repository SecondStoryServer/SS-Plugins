package com.github.syari.ss.plugins.dependency.nexengine

import su.nexmedia.engine.NexPlugin

object NexEngineAPI {
    val engine
        get() = NexPlugin.getEngine()
}
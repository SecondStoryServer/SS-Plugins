package com.github.syari.ss.plugins.votifier.net

import io.netty.util.AttributeKey
import com.github.syari.ss.plugins.votifier.util.TokenUtil.newToken

class VotifierSession {
    var version = ProtocolVersion.UNKNOWN
        set(version) {
            check(this.version == ProtocolVersion.UNKNOWN) { "Protocol version already switched" }
            field = version
        }

    val challenge = newToken()

    var hasCompletedVote = false
        private set

    fun completeVote() {
        check(!hasCompletedVote) { "Protocol completed vote twice!" }
        hasCompletedVote = true
    }

    enum class ProtocolVersion(val humanReadable: String) {
        UNKNOWN("unknown"),
        ONE("v1"),
        TWO("v2");
    }

    companion object {
        @JvmField
        val KEY: AttributeKey<VotifierSession> = AttributeKey.valueOf("votifier_session")
    }

}
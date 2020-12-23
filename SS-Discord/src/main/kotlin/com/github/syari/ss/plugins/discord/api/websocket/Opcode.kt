package com.github.syari.ss.plugins.discord.api.websocket

internal enum class Opcode(val code: Int) {
    /**
     * dispatches an event (Receive)
     */
    DISPATCH(0),

    /**
     * used for ping checking (Send/Receive)
     */
    HEARTBEAT(1),

    /**
     * used for client handshake (Send)
     */
    IDENTIFY(2),

    /**
     * used to resume a closed connection (Send)
     */
    RESUME(6),

    /**
     * used to tell clients to reconnect to the gateway (Receive)
     */
    RECONNECT(7),

    /**
     * used to notify client they have an invalid session id (Receive)
     */
    INVALID_SESSION(9),

    /**
     * sent immediately after connecting, contains heartbeat and server debug information (Receive)
     */
    HELLO(10),

    /**
     * sent immediately following a client heartbeat that was received (Receive)
     */
    HEARTBEAT_ACK(11);

    companion object {
        fun getByCode(code: Int): Opcode? {
            return values().find { it.code == code }
        }
    }
}
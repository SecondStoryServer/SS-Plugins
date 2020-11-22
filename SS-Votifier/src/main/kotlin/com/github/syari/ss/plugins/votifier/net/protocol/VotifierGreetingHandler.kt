package com.github.syari.ss.plugins.votifier.net.protocol

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import com.github.syari.ss.plugins.votifier.net.VotifierSession
import java.nio.charset.StandardCharsets

@Sharable
class VotifierGreetingHandler: ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        val session = ctx.channel().attr(VotifierSession.KEY).get()
        val version = """
            VOTIFIER 2 ${session.challenge}
            
            """.trimIndent()
        val versionBuf = Unpooled.copiedBuffer(version, StandardCharsets.UTF_8)
        ctx.writeAndFlush(versionBuf)
    }
}
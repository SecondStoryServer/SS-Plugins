package com.github.syari.ss.plugins.votifier.net.protocol

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import com.github.syari.ss.plugins.votifier.net.VotifierSession
import com.github.syari.ss.plugins.votifier.net.protocol.v1.VotifierProtocolV1Decoder
import com.github.syari.ss.plugins.votifier.net.protocol.v2.VotifierProtocolV2Decoder
import java.nio.charset.StandardCharsets

class VotifierProtocolDifferentiator: ByteToMessageDecoder() {
    companion object {
        private const val PROTOCOL_2_MAGIC: Short = 0x733A
    }

    override fun decode(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        list: List<Any>
    ) {
        val readable = buf.readableBytes()
        if (readable < 2) return
        val readMagic = buf.getShort(0)
        val session = ctx.channel().attr(VotifierSession.KEY).get()
        if (readMagic == PROTOCOL_2_MAGIC) {
            session.version = VotifierSession.ProtocolVersion.TWO
            ctx.pipeline().addAfter(
                "protocolDifferentiator", "protocol2LengthDecoder", LengthFieldBasedFrameDecoder(1024, 2, 2, 0, 4)
            )
            ctx.pipeline().addAfter(
                "protocol2LengthDecoder", "protocol2StringDecoder", StringDecoder(StandardCharsets.UTF_8)
            )
            ctx.pipeline().addAfter(
                "protocol2StringDecoder", "protocol2VoteDecoder", VotifierProtocolV2Decoder()
            )
            ctx.pipeline().addAfter(
                "protocol2VoteDecoder", "protocol2StringEncoder", StringEncoder(StandardCharsets.UTF_8)
            )
        } else {
            session.version = VotifierSession.ProtocolVersion.ONE
            ctx.pipeline().addAfter(
                "protocolDifferentiator", "protocol1Handler", VotifierProtocolV1Decoder()
            )
        }
        ctx.pipeline().remove(this)
    }
}
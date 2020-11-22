package com.github.syari.ss.plugins.votifier.net

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.FastThreadLocalThread
import com.github.syari.ss.plugins.votifier.Main.Companion.plugin
import com.github.syari.ss.plugins.votifier.net.protocol.VoteInboundHandler
import com.github.syari.ss.plugins.votifier.net.protocol.VotifierGreetingHandler
import com.github.syari.ss.plugins.votifier.net.protocol.VotifierProtocolDifferentiator
import org.jetbrains.annotations.Contract
import java.net.InetSocketAddress
import java.util.concurrent.ThreadFactory
import java.util.logging.Level

class VotifierServerBootstrap(
    private val host: String,
    private val port: Int
) {
    private val bossLoopGroup: EventLoopGroup
    private val eventLoopGroup: EventLoopGroup
    private var serverChannel: Channel? = null

    init {
        if (USE_EPOLL) {
            bossLoopGroup = EpollEventLoopGroup(1, createThreadFactory("Votifier epoll boss"))
            eventLoopGroup = EpollEventLoopGroup(3, createThreadFactory("Votifier epoll worker"))
            plugin.logger.info("Using epoll transport to accept votes.")
        } else {
            bossLoopGroup = NioEventLoopGroup(1, createThreadFactory("Votifier NIO boss"))
            eventLoopGroup = NioEventLoopGroup(3, createThreadFactory("Votifier NIO worker"))
            plugin.logger.info("Using NIO transport to accept votes.")
        }
    }

    fun start() {
        val channelClass: Class<out ServerChannel> = if (USE_EPOLL) {
            EpollServerSocketChannel::class.java
        } else {
            NioServerSocketChannel::class.java
        }
        ServerBootstrap().channel(channelClass).group(
            bossLoopGroup, eventLoopGroup
        ).childHandler(object: ChannelInitializer<SocketChannel>() {
            override fun initChannel(channel: SocketChannel) {
                channel.attr(VotifierSession.KEY).set(VotifierSession())
                channel.pipeline().addLast("greetingHandler", VotifierGreetingHandler())
                channel.pipeline().addLast("protocolDifferentiator", VotifierProtocolDifferentiator())
                channel.pipeline().addLast("voteHandler", VoteInboundHandler())
            }
        }).bind(host, port).addListener(ChannelFutureListener { future: ChannelFuture ->
            if (future.isSuccess) {
                serverChannel = future.channel()
                plugin.logger.info("Votifier enabled on socket " + serverChannel?.localAddress() + ".")
            } else {
                val socketAddress = future.channel().localAddress() ?: InetSocketAddress(host, port)
                plugin.logger.log(
                    Level.SEVERE, "Votifier was not able to bind to $socketAddress", future.cause()
                )
            }
        })
    }

    fun shutdown() {
        serverChannel?.let {
            try {
                it.close().syncUninterruptibly()
            } catch (e: Exception) {
                plugin.logger.log(Level.SEVERE, "Unable to shutdown server channel", e)
            }
        }
        eventLoopGroup.shutdownGracefully()
        bossLoopGroup.shutdownGracefully()
    }

    companion object {
        private val USE_EPOLL = Epoll.isAvailable()

        @Contract(pure = true)
        private fun createThreadFactory(name: String): ThreadFactory {
            return ThreadFactory { runnable: Runnable? ->
                val thread = FastThreadLocalThread(runnable, name)
                thread.isDaemon = true
                thread
            }
        }
    }
}
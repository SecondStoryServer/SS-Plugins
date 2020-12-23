package com.github.syari.ss.plugins.discord.api.rest

import com.github.syari.ss.plugins.discord.api.DiscordAPI.LOGGER
import java.util.concurrent.ConcurrentHashMap

object RateLimiter {
    @Volatile
    private var globalRateLimitEnds: Long = 0L

    private val rateLimits = ConcurrentHashMap<Int, Int>()
    private val rateLimitEnds = ConcurrentHashMap<Int, Long>()
    private val rateLimitRemaining = ConcurrentHashMap<Int, Int>()

    fun setGlobalRateLimitEnds(delay: Long) {
        globalRateLimitEnds = System.currentTimeMillis() + delay
        LOGGER.info("Hit global rate limit ($delay ms)")
    }

    fun setRateLimitEnds(endPoint: EndPoint, time: Long) {
        rateLimitEnds[endPoint.hashCode()] = (rateLimitEnds[endPoint.hashCode()] ?: 0).coerceAtLeast(time)
    }

    fun setRateLimit(endPoint: EndPoint, value: Int) {
        if (rateLimits[endPoint.hashCode()] == null) {
            rateLimitRemaining[endPoint.hashCode()] = value - 1
        }

        rateLimits[endPoint.hashCode()] = value
    }

    fun incrementRateLimitRemaining(endPoint: EndPoint) {
        rateLimitRemaining[endPoint.hashCode()] = (rateLimitRemaining[endPoint.hashCode()] ?: return) + 1
    }

    fun setRateLimitRemaining(endPoint: EndPoint, value: Int) {
        rateLimitRemaining[endPoint.hashCode()] = value
    }

    fun wait(endPoint: EndPoint) { // Wait for global rate limit
        if (globalRateLimitEnds > System.currentTimeMillis()) {
            Thread.sleep(globalRateLimitEnds - System.currentTimeMillis())
        }

        // Wait for per route rate limit
        rateLimitRemaining[endPoint.hashCode()]?.let { // If the remaining count is 0
            if (it <= 0) { // Wait until it's reset
                while (globalRateLimitEnds.coerceAtLeast(rateLimitEnds[endPoint.hashCode()]!!) > System.currentTimeMillis()) {
                    val delay = globalRateLimitEnds.coerceAtLeast(rateLimitEnds[endPoint.hashCode()]!!) - System.currentTimeMillis()
                    LOGGER.debug("Hit a rate limit internally. Wait ${delay}ms")
                    Thread.sleep(delay)
                }

                // Reset
                rateLimitRemaining[endPoint.hashCode()] = rateLimits[endPoint.hashCode()]!!
            }
        }
    }
}
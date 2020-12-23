package com.github.syari.ss.plugins.discord.api.rest

import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

internal object RateLimiter {
    @Volatile
    private var globalRateLimitEnds: Long = 0L

    private val mutex = Mutex()
    private val mutexes = ConcurrentHashMap<Int, Mutex>()
    private val rateLimits = ConcurrentHashMap<Int, Int>()
    private val rateLimitEnds = ConcurrentHashMap<Int, Long>()
    private val rateLimitRemaining = ConcurrentHashMap<Int, Int>()

    internal suspend fun getMutex(endPoint: EndPoint) = mutex.withLock { mutexes.getOrPut(endPoint.hashCode()) { Mutex() } }

    suspend fun setGlobalRateLimitEnds(delay: Long) {
        mutex.withLock {
            globalRateLimitEnds = System.currentTimeMillis() + delay
            LOGGER.info("Hit global rate limit ($delay ms)")
        }
    }

    suspend fun setRateLimitEnds(endPoint: EndPoint, time: Long) {
        mutex.withLock {
            rateLimitEnds[endPoint.hashCode()] = (rateLimitEnds[endPoint.hashCode()] ?: 0).coerceAtLeast(time)
        }
    }

    suspend fun setRateLimit(endPoint: EndPoint, value: Int) {
        mutex.withLock { // First time
            if (rateLimits[endPoint.hashCode()] == null) {
                rateLimitRemaining[endPoint.hashCode()] = value - 1
            }

            rateLimits[endPoint.hashCode()] = value
        }
    }

    suspend fun incrementRateLimitRemaining(endPoint: EndPoint) {
        mutex.withLock {
            rateLimitRemaining[endPoint.hashCode()] = (rateLimitRemaining[endPoint.hashCode()] ?: return) + 1
        }
    }

    suspend fun setRateLimitRemaining(endPoint: EndPoint, value: Int) {
        mutex.withLock {
            rateLimitRemaining[endPoint.hashCode()] = value
        }
    }

    suspend fun wait(endPoint: EndPoint) { // Wait for global rate limit
        mutex.withLock {
            if (globalRateLimitEnds > System.currentTimeMillis()) {
                delay(globalRateLimitEnds - System.currentTimeMillis())
            }
        }

        // Wait for per route rate limit
        rateLimitRemaining[endPoint.hashCode()]?.let { // If the remaining count is 0
            if (it <= 0) { // Wait until it's reset
                while (globalRateLimitEnds.coerceAtLeast(rateLimitEnds[endPoint.hashCode()]!!) > System.currentTimeMillis()) {
                    val delay = globalRateLimitEnds.coerceAtLeast(rateLimitEnds[endPoint.hashCode()]!!) - System.currentTimeMillis()
                    LOGGER.debug("Hit a rate limit internally. Wait ${delay}ms")
                    delay(delay)
                }

                // Reset
                rateLimitRemaining[endPoint.hashCode()] = rateLimits[endPoint.hashCode()]!!
            }
        }
    }
}
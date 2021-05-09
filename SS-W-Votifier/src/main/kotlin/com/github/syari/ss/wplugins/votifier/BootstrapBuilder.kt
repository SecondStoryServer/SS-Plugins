package com.github.syari.ss.wplugins.votifier

import com.github.syari.ss.wplugins.core.Main.Companion.console
import com.github.syari.ss.wplugins.core.code.OnDisable
import com.github.syari.ss.wplugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.config.CreateConfig.config
import com.github.syari.ss.wplugins.core.config.CustomFileConfig
import com.github.syari.ss.wplugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.wplugins.votifier.Main.Companion.plugin
import com.github.syari.ss.wplugins.votifier.net.VotifierServerBootstrap
import com.github.syari.ss.wplugins.votifier.net.protocol.v1.RSA.generate
import com.github.syari.ss.wplugins.votifier.net.protocol.v1.RSAIO.load
import com.github.syari.ss.wplugins.votifier.net.protocol.v1.RSAIO.save
import com.github.syari.ss.wplugins.votifier.util.KeyCreator
import com.github.syari.ss.wplugins.votifier.util.TokenUtil.newToken
import net.md_5.bungee.api.CommandSender
import java.io.File
import java.security.Key
import java.security.KeyPair
import java.util.logging.Level

/**
 * 投票受け入れの設定を行う
 */
object BootstrapBuilder : OnEnable, OnDisable {
    override fun onEnable() {
        load(console)
    }

    override fun onDisable() {
        stop()
    }

    lateinit var keyPair: KeyPair
        private set

    lateinit var key: Key
        private set

    private lateinit var bootstrap: VotifierServerBootstrap

    private fun load(output: CommandSender) {
        plugin.config(output, "config.yml") {
            val (host, isEditHost) = getHost()
            val (port, isEditPort) = getPort()
            val (token, isEditToken) = getToken()
            if (isEditHost || isEditPort || isEditToken) {
                save()
            }
            bootstrap = VotifierServerBootstrap(host, port)
            bootstrap.start()

            key = KeyCreator.createKeyFrom(token)

            val rsaDirectory = File(plugin.dataFolder, "rsa")

            try {
                if (!rsaDirectory.exists()) {
                    rsaDirectory.mkdir()
                    keyPair = generate(2048)
                    save(rsaDirectory, keyPair)
                } else {
                    keyPair = load(rsaDirectory)
                }
            } catch (ex: Exception) {
                plugin.logger.log(
                    Level.SEVERE, "Error reading configuration file or RSA tokens", ex
                )
            }
        }
    }

    private const val HOST_PATH = "host"
    private const val DEFAULT_HOST = "0.0.0.0"

    private fun CustomFileConfig.getHost(): Pair<String, Boolean> {
        val host = get(HOST_PATH, ConfigDataType.STRING)
        return if (host == null) {
            val ip = DEFAULT_HOST
            set(HOST_PATH, ip)
            ip to true
        } else {
            host to false
        }
    }

    private const val PORT_PATH = "port"
    private const val DEFAULT_PORT = 8192

    private fun CustomFileConfig.getPort(): Pair<Int, Boolean> {
        val port = get(PORT_PATH, ConfigDataType.INT)
        return if (port == null || port < 0) {
            set(PORT_PATH, DEFAULT_PORT)
            DEFAULT_PORT to true
        } else {
            port to false
        }
    }

    private const val TOKEN_PATH = "token"

    private fun CustomFileConfig.getToken(): Pair<String, Boolean> {
        val token = get(TOKEN_PATH, ConfigDataType.STRING)
        return if (token == null) {
            val randomToken = newToken()
            set(TOKEN_PATH, randomToken)
            randomToken to true
        } else {
            token to false
        }
    }

    /**
     * コンフィグをリロードします
     * @param output 結果の出力先
     */
    fun reload(output: CommandSender) {
        stop()
        load(output)
    }

    private fun stop() {
        bootstrap.shutdown()
    }
}

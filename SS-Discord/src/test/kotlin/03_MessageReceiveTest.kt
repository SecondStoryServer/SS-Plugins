import com.github.syari.ss.plugins.discord.api.DiscordAPI
import com.github.syari.ss.plugins.discord.api.DiscordAPI.LOGGER

fun main() {
    DiscordAPI.login(BOT_TOKEN) { message ->
        LOGGER.debug(message.toString())
    }
    LOGGER.debug("return")
    while (true) {
    }
}
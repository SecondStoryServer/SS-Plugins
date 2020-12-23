import com.github.syari.ss.plugins.discord.api.KtDiscord
import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER

suspend fun main() {
    KtDiscord.login(BOT_TOKEN) { message ->
        LOGGER.debug(message.toString())
    }
    LOGGER.debug("return")
}
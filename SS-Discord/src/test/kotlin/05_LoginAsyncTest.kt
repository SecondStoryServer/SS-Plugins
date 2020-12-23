import com.github.syari.ss.plugins.discord.api.KtDiscord

fun main() {
    KtDiscord.loginAsync(BOT_TOKEN) { message ->
        KtDiscord.LOGGER.debug(message.toString())
    }
    while (true) {
    }
}
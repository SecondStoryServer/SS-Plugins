import com.github.syari.ss.plugins.discord.api.KtDiscord

suspend fun main() {
    KtDiscord.login("wrong") {}
    KtDiscord.LOGGER.debug("return")
}
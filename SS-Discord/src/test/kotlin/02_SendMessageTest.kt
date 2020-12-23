import com.github.syari.ss.plugins.discord.api.KtDiscord
import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER
import com.github.syari.ss.plugins.discord.api.entity.api.TextChannel

suspend fun main() {
    KtDiscord.login(BOT_TOKEN) {}
    TextChannel.get(TEST_TEXT_CHANNEL)?.send("Hello!!")
    LOGGER.debug("return")
}
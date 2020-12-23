import com.github.syari.ss.plugins.discord.api.KtDiscord
import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER
import com.github.syari.ss.plugins.discord.api.entity.TextChannel

fun main() {
    KtDiscord.login(BOT_TOKEN) {}
    Thread.sleep(1000)
    TextChannel.get(TEST_TEXT_CHANNEL)?.send("Hello!!")
    LOGGER.debug("return")
    while (true) {
    }
}
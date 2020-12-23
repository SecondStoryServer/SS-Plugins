import com.github.syari.ss.plugins.discord.api.KtDiscord
import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER
import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient

fun main() {
    KtDiscord.login(BOT_TOKEN) {}
    RestClient.request(EndPoint.GetGatewayBot)
    LOGGER.debug("return")
    while (true) {
    }
}
import com.github.syari.ss.plugins.discord.api.DiscordAPI
import com.github.syari.ss.plugins.discord.api.DiscordAPI.LOGGER
import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient

fun main() {
    DiscordAPI.login(BOT_TOKEN) {}
    RestClient.request(EndPoint.GetGatewayBot)
    LOGGER.debug("return")
    while (true) {
    }
}
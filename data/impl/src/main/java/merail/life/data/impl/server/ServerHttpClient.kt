package merail.life.data.impl.server

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import merail.life.data.impl.BuildConfig
import merail.life.domain.ServerConstants
import javax.inject.Inject

internal class ServerHttpClient @Inject constructor() {

    val client = HttpClient(OkHttp) {

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }

        defaultRequest {
            header(ServerConstants.ACCESS_TOKEN, BuildConfig.ACCESS_TOKEN)

            url(BuildConfig.DOMAIN_URL)
        }
    }
}
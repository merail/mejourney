package merail.life.data.impl.server

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import merail.life.data.impl.server.dto.ContentDto
import merail.life.data.impl.server.dto.CoverDto
import javax.inject.Inject

internal class ServerApi @Inject constructor(
    private val serverHttpClient: ServerHttpClient,
) {

    suspend fun getCovers(): List<CoverDto> = serverHttpClient.client.get("/covers").body()

    suspend fun getContent(
        coverId: String,
    ): ContentDto = serverHttpClient.client.get("/content") {
        parameter("coverId", coverId)
    }.body()
}
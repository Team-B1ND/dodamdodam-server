package com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube

import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongSearchResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongSearchFailedException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongUrlMalformedException
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.dto.InnertubeSearchResponse
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.dto.OEmbedResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class YoutubeClient {

    companion object {
        private const val INNERTUBE_URL = "https://youtubei.googleapis.com/youtubei/v1/search"
        private const val INNERTUBE_API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"
        private const val CLIENT_VERSION = "2.20250626.01.00"
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        private const val OEMBED_URL = "https://www.youtube.com/oembed"
    }

    private val restClient = RestClient.builder()
        .defaultHeader("User-Agent", USER_AGENT)
        .build()

    fun search(query: String, limit: Int = 5): List<WakeupSongSearchResponse> {
        val requestBody = mapOf(
            "context" to mapOf(
                "client" to mapOf(
                    "clientName" to "WEB",
                    "clientVersion" to CLIENT_VERSION,
                    "gl" to "KR",
                    "hl" to "ko"
                )
            ),
            "query" to query,
            "params" to "EgIQAQ=="
        )

        val response = try {
            restClient.post()
                .uri("$INNERTUBE_URL?key=$INNERTUBE_API_KEY&alt=json")
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Format-Version", "1")
                .header("X-YouTube-Client-Name", "1")
                .header("X-YouTube-Client-Version", CLIENT_VERSION)
                .header("Referer", "https://www.youtube.com/")
                .header("Origin", "https://www.youtube.com")
                .body(requestBody)
                .retrieve()
                .body<InnertubeSearchResponse>()
        } catch (e: Exception) {
            throw WakeupSongSearchFailedException()
        } ?: throw WakeupSongSearchFailedException()

        return parseSearchResponse(response, limit)
    }

    fun getVideoInfoByUrl(videoUrl: String): Triple<String, String, String> {
        val videoId = extractVideoId(videoUrl)
        val oEmbed = try {
            restClient.get()
                .uri("$OEMBED_URL?url=https://www.youtube.com/watch?v=$videoId&format=json")
                .retrieve()
                .body<OEmbedResponse>()
        } catch (e: Exception) {
            throw WakeupSongUrlMalformedException()
        } ?: throw WakeupSongUrlMalformedException()

        return Triple(
            oEmbed.title ?: "",
            oEmbed.authorName ?: "",
            oEmbed.thumbnailUrl ?: "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"
        )
    }

    fun extractVideoId(videoUrl: String): String {
        return try {
            when {
                videoUrl.startsWith("https://youtu.be/") ->
                    videoUrl.removePrefix("https://youtu.be/").split("?").first()
                videoUrl.contains("?v=") ->
                    videoUrl.split("?v=")[1].split("&").first()
                videoUrl.contains("&v=") ->
                    videoUrl.split("&v=")[1].split("&").first()
                else -> throw WakeupSongUrlMalformedException()
            }
        } catch (e: Exception) {
            throw WakeupSongUrlMalformedException()
        }
    }

    private fun parseSearchResponse(
        response: InnertubeSearchResponse,
        limit: Int
    ): List<WakeupSongSearchResponse> {
        val items = response.contents
            ?.twoColumnSearchResultsRenderer
            ?.primaryContents
            ?.sectionListRenderer
            ?.contents
            ?.firstOrNull()
            ?.itemSectionRenderer
            ?.contents
            ?: return emptyList()

        return items
            .mapNotNull { it.videoRenderer }
            .filter { it.videoId != null }
            .take(limit)
            .map { renderer ->
                val videoId = renderer.videoId!!
                val thumbnail = renderer.thumbnail?.thumbnails
                    ?.maxByOrNull { (it.width ?: 0) * (it.height ?: 0) }
                    ?.url
                    ?: "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"

                WakeupSongSearchResponse(
                    videoId = videoId,
                    videoTitle = renderer.title?.runs?.firstOrNull()?.text ?: "",
                    channelTitle = renderer.ownerText?.runs?.firstOrNull()?.text ?: "",
                    videoUrl = "https://www.youtube.com/watch?v=$videoId",
                    thumbnail = thumbnail
                )
            }
    }
}

package com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube

import com.b1nd.dodamdodam.wakeupsong.application.data.response.YouTubeSearchResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.YouTubeVideoNotFoundException
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.data.InnerTubeClient
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.data.InnerTubeContext
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.data.InnerTubeSearchRequest
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class YouTubeClient {

    private val restClient = RestClient.builder()
        .baseUrl("https://www.youtube.com/youtubei/v1")
        .build()

    fun search(keyword: String): List<YouTubeSearchResponse> {
        val request = InnerTubeSearchRequest(
            context = InnerTubeContext(client = InnerTubeClient()),
            query = keyword
        )

        val response = restClient.post()
            .uri("/search")
            .header("Content-Type", "application/json")
            .body(request)
            .retrieve()
            .body(JsonNode::class.java)
            ?: throw YouTubeVideoNotFoundException()

        return parseSearchResults(response)
    }

    fun getVideoInfo(videoId: String): YouTubeSearchResponse {
        val results = search(videoId)
        return results.firstOrNull() ?: throw YouTubeVideoNotFoundException()
    }

    private fun parseSearchResults(response: JsonNode): List<YouTubeSearchResponse> {
        val results = mutableListOf<YouTubeSearchResponse>()

        val contents = response
            .path("contents")
            .path("twoColumnSearchResultsRenderer")
            .path("primaryContents")
            .path("sectionListRenderer")
            .path("contents")

        for (section in contents) {
            val itemSection = section.path("itemSectionRenderer").path("contents")
            for (item in itemSection) {
                val videoRenderer = item.path("videoRenderer")
                if (videoRenderer.isMissingNode) continue

                val id = videoRenderer.path("videoId").asText(null) ?: continue
                val title = videoRenderer.path("title").path("runs")
                    .firstOrNull()?.path("text")?.asText("") ?: ""
                val channel = videoRenderer.path("ownerText").path("runs")
                    .firstOrNull()?.path("text")?.asText("") ?: ""
                val thumb = videoRenderer.path("thumbnail").path("thumbnails")
                    .lastOrNull()?.path("url")?.asText(null)

                results.add(
                    YouTubeSearchResponse(
                        videoId = id,
                        videoTitle = title,
                        channelTitle = channel,
                        thumbnail = thumb,
                        videoUrl = "https://www.youtube.com/watch?v=$id"
                    )
                )
            }
        }

        return results
    }

    companion object {
        private val VIDEO_ID_REGEX = Regex(
            """(?:youtube\.com/watch\?v=|youtu\.be/|youtube\.com/embed/|youtube\.com/shorts/)([a-zA-Z0-9_-]{11})"""
        )

        fun extractVideoId(url: String): String? =
            VIDEO_ID_REGEX.find(url)?.groupValues?.get(1)
    }
}

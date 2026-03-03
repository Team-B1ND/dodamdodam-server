package com.b1nd.dodamdodam.wakeupsong.infrastructure.melon

import com.b1nd.dodamdodam.wakeupsong.application.data.response.MelonChartResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.MelonChartFetchFailedException
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Component
class MelonChartClient {

    fun getChart(): List<MelonChartResponse> {
        try {
            val document = Jsoup.connect(MELON_CHART_URL)
                .userAgent(USER_AGENT)
                .timeout(5000)
                .get()

            val songs = mutableListOf<MelonChartResponse>()
            val rows = document.select("tr.lst50, tr.lst100")

            for ((index, row) in rows.withIndex()) {
                val title = row.select("div.ellipsis.rank01 span a").text()
                val artist = row.select("div.ellipsis.rank02 a").first()?.text() ?: ""
                val album = row.select("div.ellipsis.rank03 a").text()
                val thumbnail = row.select("img").attr("src").ifEmpty { null }

                songs.add(
                    MelonChartResponse(
                        rank = index + 1,
                        title = title,
                        artist = artist,
                        album = album,
                        thumbnail = thumbnail
                    )
                )
            }

            return songs
        } catch (e: Exception) {
            throw MelonChartFetchFailedException()
        }
    }

    companion object {
        private const val MELON_CHART_URL = "https://www.melon.com/chart/index.htm"
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }
}

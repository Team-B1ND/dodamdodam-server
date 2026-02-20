package com.b1nd.dodamdodam.wakeupsong.infrastructure.melon

import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.MelonChartResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

@Component
class MelonChartService {

    companion object {
        private const val MELON_CHART_URL = "https://www.melon.com/chart/index.htm"
        private const val TIMEOUT_MS = 10_000
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/120.0.0.0 Safari/537.36"
    }

    fun fetchChart(): List<MelonChartResponse> {
        val document = Jsoup.connect(MELON_CHART_URL)
            .userAgent(USER_AGENT)
            .referrer("https://www.melon.com")
            .header("Accept-Language", "ko-KR,ko;q=0.9")
            .timeout(TIMEOUT_MS)
            .get()

        return document.select("tr.lst50, tr.lst100").mapNotNull { row -> parseRow(row) }
    }

    private fun parseRow(row: Element): MelonChartResponse? {
        val rank = row.selectFirst("span.rank")
            ?.text()
            ?.trim()
            ?.toIntOrNull()
            ?: return null

        val thumbnail = row.selectFirst("td > div.wrap > a.image_typeAll > img")
            ?.attr("src")
            .orEmpty()

        val name = row.selectFirst("div.ellipsis.rank01 a")
            ?.text()
            ?.trim()
            .orEmpty()

        val artist = row.selectFirst("div.ellipsis.rank02 a")
            ?.text()
            ?.trim()
            .orEmpty()

        val album = row.selectFirst("div.ellipsis.rank03 a")
            ?.text()
            ?.trim()
            .orEmpty()

        return MelonChartResponse(
            rank = rank,
            name = name,
            artist = artist,
            album = album,
            thumbnail = thumbnail
        )
    }
}

package com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InnertubeSearchResponse(
    val contents: Contents? = null
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Contents(val twoColumnSearchResultsRenderer: TwoColumn? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TwoColumn(val primaryContents: PrimaryContents? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PrimaryContents(val sectionListRenderer: SectionList? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SectionList(val contents: List<SectionContent>? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SectionContent(val itemSectionRenderer: ItemSection? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ItemSection(val contents: List<SearchItem>? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SearchItem(val videoRenderer: VideoRenderer? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class VideoRenderer(
        val videoId: String? = null,
        val title: Title? = null,
        val ownerText: OwnerText? = null,
        val thumbnail: ThumbnailWrapper? = null
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Title(val runs: List<Run>? = null)

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class OwnerText(val runs: List<Run>? = null)

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Run(val text: String? = null)

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class ThumbnailWrapper(val thumbnails: List<Thumbnail>? = null)

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Thumbnail(val url: String? = null, val width: Int? = null, val height: Int? = null)
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class OEmbedResponse(
    val title: String? = null,
    val authorName: String? = null,
    val thumbnailUrl: String? = null
)

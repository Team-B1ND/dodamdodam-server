package com.b1nd.dodamdodam.core.github.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.netty.http.client.HttpClient

class GitHubClient(
    private val webClient: WebClient
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val objectMapper = ObjectMapper()

    fun downloadReleaseAsset(owner: String, repo: String, tag: String): ByteArray {
        val json = webClient.get()
            .uri("/repos/{owner}/{repo}/releases/tags/{tag}", owner, repo, tag)
            .retrieve()
            .bodyToMono<String>()
            .block() ?: throw IllegalStateException("Release not found: $owner/$repo@$tag")

        val release = objectMapper.readTree(json)
        val assets = release["assets"]

        if (assets == null || !assets.isArray || assets.isEmpty) {
            throw IllegalStateException("No assets found in release: $owner/$repo@$tag")
        }

        log.info("Release {}/{}@{} has {} assets: {}", owner, repo, tag,
            assets.size(),
            assets.map { it["name"]?.asText() })

        val zipAsset = assets.firstOrNull {
            it["name"]?.asText()?.endsWith(".zip") == true
        } ?: throw IllegalStateException(
            "No .zip asset found in release: $owner/$repo@$tag. Available: ${assets.map { it["name"]?.asText() }}"
        )

        val downloadUrl = zipAsset["browser_download_url"]?.asText()
            ?: throw IllegalStateException("No download URL for asset: ${zipAsset["name"]?.asText()}")

        log.info("Downloading release asset from: {}", downloadUrl)

        val httpClient = HttpClient.create().followRedirect(true)
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .codecs { it.defaultCodecs().maxInMemorySize(50 * 1024 * 1024) }
            .build()
            .get()
            .uri(downloadUrl)
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .retrieve()
            .bodyToMono<ByteArray>()
            .block() ?: throw IllegalStateException("Failed to download asset from: $downloadUrl")
    }

    fun getReleaseNote(owner: String, repo: String, tag: String): String? {
        return runCatching {
            val json = webClient.get()
                .uri("/repos/{owner}/{repo}/releases/tags/{tag}", owner, repo, tag)
                .retrieve()
                .bodyToMono<String>()
                .block() ?: return null
            val release = objectMapper.readTree(json)
            release["body"]?.asText()
        }.getOrNull()
    }

    companion object {
        private val GITHUB_RELEASE_URL_PATTERN =
            Regex("https?://github\\.com/([^/]+)/([^/]+)/releases/tag/([^/]+)")

        fun parseGitHubReleaseUrl(url: String): GitHubReleaseInfo {
            val match = GITHUB_RELEASE_URL_PATTERN.matchEntire(url)
                ?: throw IllegalArgumentException("Invalid GitHub release URL: $url")
            return GitHubReleaseInfo(
                owner = match.groupValues[1],
                repo = match.groupValues[2],
                tag = match.groupValues[3],
            )
        }
    }
}

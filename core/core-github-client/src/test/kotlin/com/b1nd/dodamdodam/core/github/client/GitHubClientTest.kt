package com.b1nd.dodamdodam.core.github.client

import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

/**
 * GitHub API 연동 수동 테스트.
 * 실행: ./gradlew :core:core-github-client:test --tests "*.GitHubClientTest"
 */
class GitHubClientTest {

    private val client = GitHubClient(
        WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            // 토큰이 필요하면 아래 주석 해제
            // .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ghp_xxxxx")
            .codecs { it.defaultCodecs().maxInMemorySize(50 * 1024 * 1024) }
            .build()
    )

    @Test
    fun `parse GitHub release URL`() {
        val info = GitHubClient.parseGitHubReleaseUrl(
            "https://github.com/daedyu/test-inapp/releases/tag/v1.0.0"
        )
        println("owner=${info.owner}, repo=${info.repo}, tag=${info.tag}")
        assert(info.owner == "daedyu")
        assert(info.repo == "test-inapp")
        assert(info.tag == "v1.0.0")
    }

    @Test
    fun `download release asset from real GitHub`() {
        val bytes = client.downloadReleaseAsset("daedyu", "test-inapp", "v1.0.0")
        println("Downloaded ${bytes.size} bytes")
        assert(bytes.isNotEmpty())
    }

    @Test
    fun `get release note from real GitHub`() {
        val note = client.getReleaseNote("daedyu", "test-inapp", "v1.0.0")
        println("Release note: $note")
    }
}

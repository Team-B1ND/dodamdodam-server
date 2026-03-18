package com.b1nd.dodamdodam.core.github.config

import com.b1nd.dodamdodam.core.github.client.GitHubClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(GitHubProperties::class)
class GitHubConfig(
    private val properties: GitHubProperties
) {
    @Bean
    fun githubWebClient(): WebClient {
        val builder = WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .codecs { it.defaultCodecs().maxInMemorySize(50 * 1024 * 1024) }
        if (properties.token.isNotBlank()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${properties.token}")
        }
        return builder.build()
    }

    @Bean
    fun gitHubClient(githubWebClient: WebClient): GitHubClient =
        GitHubClient(githubWebClient)
}

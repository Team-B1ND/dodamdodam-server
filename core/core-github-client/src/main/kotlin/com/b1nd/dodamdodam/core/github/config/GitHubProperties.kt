package com.b1nd.dodamdodam.core.github.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("github")
data class GitHubProperties(
    val token: String = "",
)

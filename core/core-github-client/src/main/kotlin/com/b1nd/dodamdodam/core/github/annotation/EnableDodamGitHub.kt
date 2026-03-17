package com.b1nd.dodamdodam.core.github.annotation

import com.b1nd.dodamdodam.core.github.config.GitHubConfig
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(GitHubConfig::class)
annotation class EnableDodamGitHub

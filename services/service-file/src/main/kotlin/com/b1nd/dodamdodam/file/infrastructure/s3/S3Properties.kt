package com.b1nd.dodamdodam.file.infrastructure.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("cloud.aws.s3")
data class S3Properties(
    val endpoint: String = "",
    val region: String = "ap-northeast-2",
    val bucket: String,
    val accessKey: String = "",
    val secretKey: String = "",
    val pathStyleAccess: Boolean = false,
    val keyPrefix: String = "",
)

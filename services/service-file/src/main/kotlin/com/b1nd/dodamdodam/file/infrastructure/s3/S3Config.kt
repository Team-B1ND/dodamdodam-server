package com.b1nd.dodamdodam.file.infrastructure.s3

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Configuration
class S3Config(
    private val properties: S3Properties,
) {

    @Bean
    fun s3Client(): S3Client = S3Client.builder()
        .apply {
            if (properties.endpoint.isNotBlank()) {
                endpointOverride(URI.create(properties.endpoint))
            }
        }
        .region(Region.of(properties.region))
        .credentialsProvider(resolveCredentials())
        .serviceConfiguration(
            S3Configuration.builder()
                .pathStyleAccessEnabled(properties.pathStyleAccess)
                .chunkedEncodingEnabled(!properties.pathStyleAccess)
                .build()
        )
        .build()

    private fun resolveCredentials(): AwsCredentialsProvider =
        if (properties.accessKey.isNotBlank() && properties.secretKey.isNotBlank()) {
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(properties.accessKey, properties.secretKey)
            )
        } else {
            DefaultCredentialsProvider.builder().build()
        }
}

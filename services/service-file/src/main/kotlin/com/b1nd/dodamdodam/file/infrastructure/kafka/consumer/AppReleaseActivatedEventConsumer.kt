package com.b1nd.dodamdodam.file.infrastructure.kafka.consumer

import com.b1nd.dodamdodam.core.github.client.GitHubClient
import com.b1nd.dodamdodam.core.kafka.constants.KafkaTopics
import com.b1nd.dodamdodam.core.kafka.event.app.AppReleaseActivatedEvent
import com.b1nd.dodamdodam.file.infrastructure.s3.S3ReleaseUploader
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AppReleaseActivatedEventConsumer(
    private val gitHubClient: GitHubClient,
    private val s3ReleaseUploader: S3ReleaseUploader,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [KafkaTopics.APP_RELEASE_ACTIVATED],
        groupId = "service-file-release",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeReleaseActivated(event: AppReleaseActivatedEvent) {
        log.info("Received release activated event: app={}, release={}", event.appPublicId, event.releasePublicId)
        runCatching {
            val releaseInfo = GitHubClient.parseGitHubReleaseUrl(event.githubReleaseUrl)
            val zipBytes = gitHubClient.downloadReleaseAsset(
                releaseInfo.owner,
                releaseInfo.repo,
                releaseInfo.tag,
            )
            s3ReleaseUploader.uploadReleaseArchive(zipBytes, event.appPublicId, event.releasePublicId)
        }.onFailure { e ->
            log.error("Failed to process release activated event for app={}, release={}", event.appPublicId, event.releasePublicId, e)
        }
    }
}

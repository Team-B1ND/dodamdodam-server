package com.b1nd.dodamdodam.file.infrastructure.s3

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import java.util.zip.ZipInputStream

@Component
class S3ReleaseUploader(
    private val s3Client: S3Client,
    private val properties: S3Properties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun uploadReleaseArchive(zipBytes: ByteArray, appPublicId: UUID, releasePublicId: UUID) {
        val basePath = buildBasePath(appPublicId, releasePublicId)
        var uploadedCount = 0

        ZipInputStream(ByteArrayInputStream(zipBytes)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory && !isIgnoredEntry(entry.name)) {
                    val relativePath = entry.name
                    if (relativePath.isNotBlank()) {
                        val key = "$basePath$relativePath"
                        val bytes = zis.readBytes()
                        val contentType = resolveContentType(relativePath)

                        val request = PutObjectRequest.builder()
                            .bucket(properties.bucket)
                            .key(key)
                            .contentType(contentType)
                            .contentLength(bytes.size.toLong())
                            .build()

                        s3Client.putObject(request, RequestBody.fromBytes(bytes))
                        uploadedCount++
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        log.info("Uploaded {} files to S3 for app={} release={}", uploadedCount, appPublicId, releasePublicId)
    }

    private fun buildBasePath(appPublicId: UUID, releasePublicId: UUID): String {
        val prefix = properties.keyPrefix.trim('/')
        val inappPath = "inapp/$appPublicId/releases/$releasePublicId/"
        return if (prefix.isNotBlank()) "$prefix/$inappPath" else inappPath
    }

    private fun isIgnoredEntry(name: String): Boolean {
        val lower = name.lowercase()
        return lower.startsWith("__macosx/") ||
            lower.endsWith("/.ds_store") ||
            lower.endsWith(".ds_store") ||
            lower.endsWith("/thumbs.db") ||
            lower.endsWith("thumbs.db")
    }

    private fun resolveContentType(fileName: String): String =
        runCatching { Files.probeContentType(Path.of(fileName)) }
            .getOrNull()
            ?.takeIf { it.isNotBlank() }
            ?: "application/octet-stream"
}

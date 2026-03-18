package com.b1nd.dodamdodam.file.infrastructure.s3

import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.ZipInputStream

@Component
class S3ReleaseUploader(
    private val s3Client: S3Client,
    private val properties: S3Properties,
) {
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

    private fun resolveContentType(fileName: String): String {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return CONTENT_TYPE_MAP[ext] ?: "application/octet-stream"
    }

    companion object {
        private val CONTENT_TYPE_MAP = mapOf(
            "html" to "text/html",
            "htm" to "text/html",
            "css" to "text/css",
            "js" to "application/javascript",
            "mjs" to "application/javascript",
            "json" to "application/json",
            "svg" to "image/svg+xml",
            "png" to "image/png",
            "jpg" to "image/jpeg",
            "jpeg" to "image/jpeg",
            "gif" to "image/gif",
            "webp" to "image/webp",
            "ico" to "image/x-icon",
            "woff" to "font/woff",
            "woff2" to "font/woff2",
            "ttf" to "font/ttf",
            "otf" to "font/otf",
            "txt" to "text/plain",
            "xml" to "application/xml",
            "wasm" to "application/wasm",
            "map" to "application/json",
        )
    }
}

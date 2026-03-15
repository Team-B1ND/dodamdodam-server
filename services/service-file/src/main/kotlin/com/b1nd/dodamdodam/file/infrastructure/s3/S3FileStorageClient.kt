package com.b1nd.dodamdodam.file.infrastructure.s3

import com.b1nd.dodamdodam.file.domain.exception.FileUploadFailedException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@Component
class S3FileStorageClient(
    private val s3Client: S3Client,
    private val properties: S3Properties,
) {
    fun upload(file: MultipartFile): String {
        val extension = file.extractExtension()
        val key = generateKey(extension)
        val contentType = file.resolveContentType()

        val request = PutObjectRequest.builder()
            .bucket(properties.bucket)
            .key(key)
            .contentType(contentType)
            .contentLength(file.size)
            .build()

        runCatching {
            file.inputStream.use { stream ->
                s3Client.putObject(request, RequestBody.fromInputStream(stream, file.size))
            }
        }.onFailure { throw FileUploadFailedException() }

        return buildFileUrl(key)
    }

    private fun MultipartFile.extractExtension(): String =
        originalFilename
            ?.substringAfterLast('.', "")
            ?.lowercase()
            ?: ""

    private fun generateKey(extension: String): String {
        val uuid = UUID.randomUUID().toString()
        val filename = if (extension.isNotBlank()) "$uuid.$extension" else uuid
        val prefix = properties.keyPrefix.trim('/')
        return if (prefix.isNotBlank()) "$prefix/$filename" else filename
    }

    private fun MultipartFile.resolveContentType(): String =
        contentType
            ?.takeIf { it.isNotBlank() && it != DEFAULT_CONTENT_TYPE }
            ?: originalFilename
                ?.let { Path.of(it).fileName }
                ?.let { Files.probeContentType(it) }
                ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_CONTENT_TYPE

    private fun buildFileUrl(key: String): String =
        if (properties.endpoint.isNotBlank()) {
            "${properties.endpoint.trimEnd('/')}/${properties.bucket}/$key"
        } else {
            "https://${properties.bucket}.s3.${properties.region}.amazonaws.com/$key"
        }

    companion object {
        private const val DEFAULT_CONTENT_TYPE = "application/octet-stream"
    }
}

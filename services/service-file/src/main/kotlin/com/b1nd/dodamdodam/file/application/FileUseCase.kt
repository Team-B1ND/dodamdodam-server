package com.b1nd.dodamdodam.file.application

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.file.application.data.response.FileUploadResponse
import com.b1nd.dodamdodam.file.domain.enumeration.FileType
import com.b1nd.dodamdodam.file.domain.service.FileValidationService
import com.b1nd.dodamdodam.file.infrastructure.s3.S3FileStorageClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class FileUseCase(
    private val fileValidationService: FileValidationService,
    private val s3FileStorageClient: S3FileStorageClient,
) {
    fun upload(file: MultipartFile, allowType: FileType?, width: Int?, height: Int?): Response<FileUploadResponse> {
        fileValidationService.validate(file, allowType, width, height)
        val url = s3FileStorageClient.upload(file)

        return Response.created(
            "파일이 업로드되었어요.",
            FileUploadResponse(
                url = url,
                originalFilename = file.originalFilename ?: "",
                contentType = file.contentType,
                size = file.size,
            )
        )
    }
}

package com.b1nd.dodamdodam.file.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.file.application.FileUseCase
import com.b1nd.dodamdodam.file.domain.enumeration.FileType
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileController(
    private val fileUseCase: FileUseCase,
) {
    @UserAccess
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("allowType", required = false) allowType: FileType?,
        @RequestParam("width", required = false) width: Int?,
        @RequestParam("height", required = false) height: Int?,
    ) = fileUseCase.upload(file, allowType, width, height)
}

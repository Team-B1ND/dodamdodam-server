package com.b1nd.dodamdodam.file.domain.service

import com.b1nd.dodamdodam.file.domain.enumeration.FileType
import com.b1nd.dodamdodam.file.domain.exception.FileDimensionNotAllowedException
import com.b1nd.dodamdodam.file.domain.exception.FileDimensionReadFailedException
import com.b1nd.dodamdodam.file.domain.exception.FileEmptyException
import com.b1nd.dodamdodam.file.domain.exception.FileTypeNotAllowedException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.imageio.ImageIO

@Service
class FileValidationService {

    fun validate(file: MultipartFile, allowType: FileType?, width: Int?, height: Int?) {
        if (file.isEmpty) throw FileEmptyException()

        val detectedType = file.detectType()

        if (allowType != null && detectedType != allowType) {
            throw FileTypeNotAllowedException()
        }

        if (width != null && height != null && detectedType?.supportsDimensionCheck == true) {
            validateDimension(file, width, height)
        }
    }

    private fun MultipartFile.detectType(): FileType? {
        val extension = originalFilename
            ?.substringAfterLast('.', "")
            ?.lowercase()
            ?.takeIf { it.isNotBlank() }
            ?: throw FileTypeNotAllowedException()
        return FileType.fromExtension(extension)
    }

    private fun validateDimension(file: MultipartFile, requiredWidth: Int, requiredHeight: Int) {
        val iis = ImageIO.createImageInputStream(file.inputStream)
            ?: throw FileDimensionReadFailedException()

        val reader = ImageIO.getImageReaders(iis)
            .takeIf { it.hasNext() }
            ?.next()
            ?: run {
                iis.close()
                throw FileDimensionReadFailedException()
            }

        try {
            reader.input = iis
            if (reader.getWidth(0) != requiredWidth || reader.getHeight(0) != requiredHeight) {
                throw FileDimensionNotAllowedException()
            }
        } finally {
            reader.dispose()
            iis.close()
        }
    }
}

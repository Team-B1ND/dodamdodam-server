package com.b1nd.dodamdodam.file.domain.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class FileEmptyException : BasicException(FileExceptionCode.FILE_EMPTY)

class FileTypeNotAllowedException : BasicException(FileExceptionCode.FILE_TYPE_NOT_ALLOWED)

class FileDimensionNotAllowedException : BasicException(FileExceptionCode.FILE_DIMENSION_NOT_ALLOWED)

class FileDimensionReadFailedException : BasicException(FileExceptionCode.FILE_DIMENSION_READ_FAILED)

class FileUploadFailedException : BasicException(FileExceptionCode.FILE_UPLOAD_FAILED)

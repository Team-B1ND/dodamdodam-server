package com.b1nd.dodamdodam.file.domain.enumeration

enum class FileType(
    val extensions: Set<String>,
    val mimePrefix: String,
    val supportsDimensionCheck: Boolean,
) {
    IMAGE(
        extensions = setOf("jpg", "jpeg", "png", "bmp", "webp", "svg", "tiff"),
        mimePrefix = "image/",
        supportsDimensionCheck = true,
    ),
    GIF(
        extensions = setOf("gif"),
        mimePrefix = "image/gif",
        supportsDimensionCheck = true,
    ),
    VIDEO(
        extensions = setOf("mp4", "avi", "mov", "mkv", "webm", "wmv", "flv"),
        mimePrefix = "video/",
        supportsDimensionCheck = false,
    ),
    AUDIO(
        extensions = setOf("mp3", "wav", "ogg", "flac", "aac", "wma"),
        mimePrefix = "audio/",
        supportsDimensionCheck = false,
    ),
    DOCUMENT(
        extensions = setOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv", "hwp"),
        mimePrefix = "",
        supportsDimensionCheck = false,
    );

    companion object {
        fun fromExtension(extension: String): FileType? {
            val ext = extension.lowercase()
            return entries.firstOrNull { ext in it.extensions }
        }
    }
}

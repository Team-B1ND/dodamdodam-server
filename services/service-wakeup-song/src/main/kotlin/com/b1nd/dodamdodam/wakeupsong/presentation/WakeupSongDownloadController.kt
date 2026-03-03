package com.b1nd.dodamdodam.wakeupsong.presentation

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.wakeupsong.application.WakeupSongUseCase
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.FilterInputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@RestController
class WakeupSongDownloadController(
    private val wakeupSongUseCase: WakeupSongUseCase
) {

    @UserAccess(hasAnyRoleOnly = true)
    @GetMapping("/download")
    fun downloadMp3(@RequestParam url: String): ResponseEntity<Resource> {
        val (file, title) = wakeupSongUseCase.downloadMp3(url)
        val fileSize = Files.size(file)

        val encodedFilename = URLEncoder.encode("$title.mp3", StandardCharsets.UTF_8)
            .replace("+", "%20")

        val cleanupStream = object : FilterInputStream(Files.newInputStream(file)) {
            override fun close() {
                super.close()
                Files.deleteIfExists(file)
            }
        }

        val resource = object : InputStreamResource(cleanupStream) {
            override fun contentLength(): Long = fileSize
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$encodedFilename")
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .contentLength(fileSize)
            .body(resource)
    }
}

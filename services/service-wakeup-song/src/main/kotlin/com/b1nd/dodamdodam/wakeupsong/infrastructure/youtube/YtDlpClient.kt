package com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.AudioDownloadFailedException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.InvalidVideoIdException
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

@Component
class YtDlpClient {

    fun downloadMp3(videoId: String): Path {
        if (!videoId.matches(Regex("^[a-zA-Z0-9_-]{11}$"))) {
            throw InvalidVideoIdException()
        }

        val safeUrl = "https://www.youtube.com/watch?v=$videoId"
        val tmpFile = Files.createTempFile("wakeup-song-", ".mp3")
        Files.delete(tmpFile)

        val outputTemplate = tmpFile.toString().removeSuffix(".mp3")

        val process = ProcessBuilder(
            "yt-dlp",
            "--extract-audio",
            "--audio-format", "mp3",
            "--audio-quality", "0",
            "-o", "$outputTemplate.%(ext)s",
            safeUrl
        )
            .redirectErrorStream(true)
            .start()

        val completed = process.waitFor(60, TimeUnit.SECONDS)
        if (!completed) {
            process.destroyForcibly()
            throw AudioDownloadFailedException()
        }

        if (process.exitValue() != 0) {
            throw AudioDownloadFailedException()
        }

        if (!Files.exists(tmpFile)) {
            throw AudioDownloadFailedException()
        }

        return tmpFile
    }
}

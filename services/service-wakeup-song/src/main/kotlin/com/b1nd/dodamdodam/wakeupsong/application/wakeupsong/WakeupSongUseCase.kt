package com.b1nd.dodamdodam.wakeupsong.application.wakeupsong

import com.b1nd.dodamdodam.core.security.passport.PassportUserDetails
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request.WakeupSongKeywordRequest
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.request.WakeupSongUrlRequest
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.MelonChartResponse
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongListResponse
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongResponse
import com.b1nd.dodamdodam.wakeupsong.application.wakeupsong.data.response.WakeupSongSearchResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatusType
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongSearchFailedException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongUnsupportedTypeException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.service.WakeupSongService
import com.b1nd.dodamdodam.wakeupsong.infrastructure.melon.MelonChartService
import com.b1nd.dodamdodam.wakeupsong.infrastructure.user.client.UserGrpcClient
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.YoutubeClient
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(rollbackFor = [Exception::class])
class WakeupSongUseCase(
    private val wakeupSongService: WakeupSongService,
    private val youtubeClient: YoutubeClient,
    private val melonChartService: MelonChartService,
    private val userGrpcClient: UserGrpcClient
) {

    private fun currentUserId(): UUID {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return (principal as PassportUserDetails).passport.userId!!
    }

    @Transactional(readOnly = true)
    fun getMy(): WakeupSongListResponse =
        WakeupSongListResponse(
            wakeupSongService.findAllByStudentId(currentUserId())
                .map { WakeupSongResponse.fromEntity(it) }
        )

    @Transactional(readOnly = true)
    fun getAllowed(): WakeupSongListResponse =
        WakeupSongListResponse(
            wakeupSongService.findAllByStatus(WakeupSongStatusType.ALLOWED)
                .map { WakeupSongResponse.fromEntity(it) }
        )

    @Transactional(readOnly = true)
    fun getPending(): WakeupSongListResponse =
        WakeupSongListResponse(
            wakeupSongService.findAllByStatus(WakeupSongStatusType.PENDING)
                .map { WakeupSongResponse.fromEntity(it) }
        )

    @Transactional(readOnly = true)
    fun getChart(): List<MelonChartResponse> =
        melonChartService.fetchChart()

    @Transactional(readOnly = true)
    fun search(keyword: String): List<WakeupSongSearchResponse> =
        youtubeClient.search(keyword, limit = 5)

    fun applyByUrl(request: WakeupSongUrlRequest) {
        val studentId = currentUserId()
        runBlocking { userGrpcClient.getStudentByUserId(studentId) }

        val (videoTitle, channelTitle, thumbnailUrl) = youtubeClient.getVideoInfoByUrl(request.videoUrl)
        val videoId = youtubeClient.extractVideoId(request.videoUrl)

        checkVideoType(videoTitle)

        val entity = WakeupSongEntity(
            studentId = studentId,
            videoId = videoId,
            videoTitle = videoTitle,
            videoUrl = request.videoUrl,
            channelTitle = channelTitle,
            thumbnailUrl = thumbnailUrl
        )
        wakeupSongService.save(entity)
    }

    fun applyByKeyword(request: WakeupSongKeywordRequest) {
        val studentId = currentUserId()
        runBlocking { userGrpcClient.getStudentByUserId(studentId) }

        val query = "${request.title} ${request.artist}"
        val results = youtubeClient.search(query, limit = 1)
        val video = results.firstOrNull() ?: throw WakeupSongSearchFailedException()

        checkVideoType(video.videoTitle)

        val entity = WakeupSongEntity(
            studentId = studentId,
            videoId = video.videoId,
            videoTitle = video.videoTitle,
            videoUrl = video.videoUrl,
            channelTitle = video.channelTitle,
            thumbnailUrl = video.thumbnail
        )
        wakeupSongService.save(entity)
    }

    fun allow(id: Long) = wakeupSongService.allow(id)

    fun deny(id: Long) = wakeupSongService.deny(id)

    fun deleteMy(id: Long) = wakeupSongService.deleteById(id, currentUserId())

    fun deleteById(id: Long) = wakeupSongService.deleteByIdAsAdmin(id)

    private fun checkVideoType(title: String) {
        if (title.contains("MV", ignoreCase = true)) {
            throw WakeupSongUnsupportedTypeException()
        }
    }
}

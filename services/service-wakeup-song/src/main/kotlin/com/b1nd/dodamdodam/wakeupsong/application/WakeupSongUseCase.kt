package com.b1nd.dodamdodam.wakeupsong.application

import com.b1nd.dodamdodam.core.common.data.FileDownloadResponse
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.security.passport.holder.PassportHolder
import com.b1nd.dodamdodam.core.security.passport.requireUserId
import com.b1nd.dodamdodam.wakeupsong.application.data.request.ApplyWakeupSongByKeywordRequest
import com.b1nd.dodamdodam.wakeupsong.application.data.request.ApplyWakeupSongRequest
import com.b1nd.dodamdodam.wakeupsong.application.data.response.MelonChartResponse
import com.b1nd.dodamdodam.wakeupsong.application.data.response.WakeupSongResponse
import com.b1nd.dodamdodam.wakeupsong.application.data.response.YouTubeSearchResponse
import com.b1nd.dodamdodam.wakeupsong.application.data.toEntity
import com.b1nd.dodamdodam.wakeupsong.application.data.toResponse
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongNotOwnerException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.YouTubeVideoNotFoundException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.service.WakeupSongService
import com.b1nd.dodamdodam.wakeupsong.infrastructure.melon.MelonChartClient
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.YtDlpClient
import com.b1nd.dodamdodam.wakeupsong.infrastructure.youtube.YouTubeClient
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files

@Component
@Transactional(rollbackFor = [Exception::class])
class WakeupSongUseCase(
    private val wakeupSongService: WakeupSongService,
    private val youTubeClient: YouTubeClient,
    private val melonChartClient: MelonChartClient,
    private val ytDlpClient: YtDlpClient
) {

    fun applyWakeupSong(request: ApplyWakeupSongRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val videoId = YouTubeClient.extractVideoId(request.videoUrl)
            ?: throw YouTubeVideoNotFoundException()
        val videoInfo = youTubeClient.getVideoInfo(videoId)
        val entity = videoInfo.toEntity(userId)
        wakeupSongService.save(entity)
        return Response.created("기상송이 신청되었어요.")
    }

    fun applyByKeyword(request: ApplyWakeupSongByKeywordRequest): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val keyword = "${request.artist} ${request.title}"
        val results = youTubeClient.search(keyword)
        val videoInfo = results.firstOrNull() ?: throw YouTubeVideoNotFoundException()
        val entity = videoInfo.toEntity(userId)
        wakeupSongService.save(entity)
        return Response.created("기상송이 신청되었어요.")
    }

    @Transactional(readOnly = true)
    fun getMyWakeupSongs(): Response<List<WakeupSongResponse>> {
        val userId = PassportHolder.current().requireUserId()
        val songs = wakeupSongService.getByUserId(userId).map { it.toResponse() }
        return Response.ok("내 기상송을 조회했어요.", songs)
    }

    @Transactional(readOnly = true)
    fun getAllowed(year: Int, month: Int, day: Int): Response<List<WakeupSongResponse>> {
        val songs = wakeupSongService.getAllowed(year, month, day).map { it.toResponse() }
        return Response.ok("승인된 기상송을 조회했어요.", songs)
    }

    @Transactional(readOnly = true)
    fun getPending(): Response<List<WakeupSongResponse>> {
        val songs = wakeupSongService.getPending().map { it.toResponse() }
        return Response.ok("대기 중인 기상송을 조회했어요.", songs)
    }

    fun allow(id: Long): Response<Any> {
        val wakeupSong = wakeupSongService.getById(id)
        wakeupSong.allow()
        wakeupSongService.save(wakeupSong)
        return Response.ok("기상송이 승인되었어요.")
    }

    fun deny(id: Long): Response<Any> {
        val wakeupSong = wakeupSongService.getById(id)
        wakeupSong.deny()
        wakeupSongService.save(wakeupSong)
        return Response.ok("기상송이 거절되었어요.")
    }

    fun deleteMyWakeupSong(id: Long): Response<Any> {
        val userId = PassportHolder.current().requireUserId()
        val wakeupSong = wakeupSongService.getById(id)
        if (wakeupSong.userId != userId) {
            throw WakeupSongNotOwnerException()
        }
        wakeupSongService.delete(wakeupSong)
        return Response.ok("기상송이 삭제되었어요.")
    }

    @Transactional(readOnly = true)
    fun searchYouTube(keyword: String): Response<List<YouTubeSearchResponse>> {
        val results = youTubeClient.search(keyword)
        return Response.ok("유튜브 검색 결과를 조회했어요.", results)
    }

    @Transactional(readOnly = true)
    fun getMelonChart(): Response<List<MelonChartResponse>> {
        val chart = melonChartClient.getChart()
        return Response.ok("멜론 차트를 조회했어요.", chart)
    }

    fun downloadMp3(url: String): ResponseEntity<Resource> {
        val videoId = YouTubeClient.extractVideoId(url)
            ?: throw YouTubeVideoNotFoundException()
        val videoInfo = youTubeClient.getVideoInfo(videoId)
        val file = ytDlpClient.downloadMp3(url)
        val bytes = Files.readAllBytes(file)
        Files.deleteIfExists(file)
        val response = FileDownloadResponse(bytes, "${videoInfo.videoTitle}.mp3", MediaType.parseMediaType("audio/mpeg"))
        return Response.toFileResponseEntity(response)
    }
}

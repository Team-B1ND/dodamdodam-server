package com.b1nd.dodamdodam.neis.infrastructure.comcigan

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

data class ParsedSchedule(
    val date: LocalDate,
    val grade: Int,
    val room: Int,
    val period: Int,
    val subject: String,
    val teacher: String,
)

@Component
class ComciganClient(
    private val comciganProperties: ComciganProperties,
    private val objectMapper: ObjectMapper,
) {
    private val eucKr = Charset.forName("EUC-KR")
    private val httpClient = HttpClient.newHttpClient()

    private lateinit var baseUrl: String
    private lateinit var searchUrl: String
    private lateinit var prefix: String
    private var dayNum: Int = 0
    private var thNum: Int = 0
    private var sbNum: Int = 0
    private var schoolCode: Int = 0

    @PostConstruct
    fun init() {
        bootstrap()
        resolveSchool()
    }

    private fun bootstrap() {
        val html = fetchAsEucKr("http://comci.net:4082/st")

        val route = Regex("\\./\\d+\\?\\d+l").find(html)?.value
            ?: throw IllegalStateException("컴시간 라우트 추출 실패")
        prefix = Regex("'(\\d+_)'").find(html)?.groupValues?.get(1)
            ?: throw IllegalStateException("컴시간 프리픽스 추출 실패")

        val orgDataMatch = Regex("원자료=Q자료\\(자료\\.자료(\\d+)").find(html)
        val dayDataMatch = Regex("일일자료=Q자료\\(자료\\.자료(\\d+)").find(html)

        dayNum = dayDataMatch?.groupValues?.get(1)?.toInt()
            ?: orgDataMatch?.groupValues?.get(1)?.toInt()
            ?: throw IllegalStateException("컴시간 dayNum 추출 실패")
        thNum = Regex("성명=자료\\.자료(\\d+)").find(html)?.groupValues?.get(1)?.toInt()
            ?: throw IllegalStateException("컴시간 thNum 추출 실패")
        sbNum = Regex("자료\\.자료(\\d+)\\[sb]").find(html)?.groupValues?.get(1)?.toInt()
            ?: throw IllegalStateException("컴시간 sbNum 추출 실패")

        baseUrl = "http://comci.net:4082${route.substring(1, 8)}"
        searchUrl = "$baseUrl${route.substring(8)}"
    }

    private fun resolveSchool() {
        val name = comciganProperties.schoolName
        val url = "$searchUrl${encodeEucKr(name)}"
        val body = fetchAsUtf8(url)
        val json = objectMapper.readTree(body.replace("\u0000", ""))

        val schools = json["학교검색"]
            ?: throw IllegalStateException("컴시간 학교 검색 실패")
        if (schools.size() == 0) {
            throw IllegalStateException("컴시간에서 '$name' 학교를 찾을 수 없어요.")
        }

        schoolCode = schools[0][3].asInt()
    }

    fun fetchWeeklySchedules(mondayDate: LocalDate): List<ParsedSchedule> {
        val json = fetchTimetable()
        val subjects = json["자료$sbNum"]
        val teachers = json["자료$thNum"]
        val timetableData = json["자료$dayNum"]

        val result = mutableListOf<ParsedSchedule>()

        for (gradeIdx in 1..minOf(comciganProperties.maxGrade, timetableData.size() - 1)) {
            val gradeData = timetableData[gradeIdx]
            for (roomIdx in 1..minOf(comciganProperties.maxRoom, gradeData.size() - 1)) {
                val roomData = gradeData[roomIdx]
                for (dayIdx in 1..minOf(5, roomData.size() - 1)) {
                    val dayData = roomData[dayIdx]
                    val date = mondayDate.plusDays((dayIdx - 1).toLong())
                    parsePeriods(dayData, subjects, teachers) { period, subject, teacher ->
                        result.add(ParsedSchedule(date, gradeIdx, roomIdx, period, subject, teacher))
                    }
                }
            }
        }

        return result
    }

    fun fetchDailySchedules(date: LocalDate): List<ParsedSchedule> {
        val dayOfWeek = date.dayOfWeek
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) return emptyList()

        val dayIdx = dayOfWeek.value
        val json = fetchTimetable()
        val subjects = json["자료$sbNum"]
        val teachers = json["자료$thNum"]
        val timetableData = json["자료$dayNum"]

        val result = mutableListOf<ParsedSchedule>()

        for (gradeIdx in 1..minOf(comciganProperties.maxGrade, timetableData.size() - 1)) {
            val gradeData = timetableData[gradeIdx]
            for (roomIdx in 1..minOf(comciganProperties.maxRoom, gradeData.size() - 1)) {
                val roomData = gradeData[roomIdx]
                if (dayIdx >= roomData.size()) continue
                val dayData = roomData[dayIdx]
                parsePeriods(dayData, subjects, teachers) { period, subject, teacher ->
                    result.add(ParsedSchedule(date, gradeIdx, roomIdx, period, subject, teacher))
                }
            }
        }

        return result
    }

    private inline fun parsePeriods(
        dayData: JsonNode,
        subjects: JsonNode,
        teachers: JsonNode,
        onPeriod: (period: Int, subject: String, teacher: String) -> Unit,
    ) {
        for (periodIdx in 1 until dayData.size()) {
            val value = dayData[periodIdx].asInt()
            val str = value.toString()
            if (str.length < 3) continue

            val subjectIdx = if (str.length >= 5) str.substring(0, 2).toInt() else str.substring(0, 1).toInt()
            val teacherIdx = str.substring(str.length - 2).toInt()

            val subject = getTextSafe(subjects, subjectIdx)
            val teacher = getTextSafe(teachers, teacherIdx)
            if (subject.isBlank()) continue

            onPeriod(periodIdx, subject, teacher)
        }
    }

    private fun fetchTimetable(): JsonNode {
        val param = "$prefix${schoolCode}_0_1"
        val encoded = Base64.getEncoder().encodeToString(param.toByteArray(Charsets.UTF_8))
        val body = fetchAsUtf8("$baseUrl?$encoded")
        return objectMapper.readTree(body.replace("\u0000", ""))
    }

    private fun fetchAsEucKr(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url))
            .header("User-Agent", "Mozilla/5.0").GET().build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())
        return String(response.body(), eucKr)
    }

    private fun fetchAsUtf8(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url))
            .header("User-Agent", "Mozilla/5.0").GET().build()
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    private fun encodeEucKr(text: String): String {
        return text.toByteArray(eucKr).joinToString("") { "%${String.format("%02X", it)}" }
    }

    private fun getTextSafe(node: JsonNode?, index: Int): String {
        if (node == null || index < 0 || index >= node.size()) return ""
        return node[index].asText("")
    }
}

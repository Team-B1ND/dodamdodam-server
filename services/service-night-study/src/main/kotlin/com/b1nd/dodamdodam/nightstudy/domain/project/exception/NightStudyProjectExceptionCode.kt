package com.b1nd.dodamdodam.nightstudy.domain.project.exception

import com.b1nd.dodamdodam.core.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

enum class NightStudyProjectExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ExceptionCode {
    NIGHT_STUDY_PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트 심야자습을 찾을 수 없어요."),
    NIGHT_STUDY_PROJECT_NOT_OWNER(HttpStatus.FORBIDDEN, "본인의 프로젝트 심야자습만 삭제할 수 있어요."),
    ;
}

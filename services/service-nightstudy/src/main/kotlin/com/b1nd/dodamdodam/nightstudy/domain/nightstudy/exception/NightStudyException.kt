package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class NightStudyNotFoundException: BasicException(NightStudyExceptionCode.NIGHT_STUDY_NOT_FOUND)

class NightStudyBannedException: BasicException(NightStudyExceptionCode.NIGHT_STUDY_BANNED)

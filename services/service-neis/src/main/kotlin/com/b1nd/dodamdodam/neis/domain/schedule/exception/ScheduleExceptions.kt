package com.b1nd.dodamdodam.neis.domain.schedule.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class ScheduleNotFoundException : BasicException(ScheduleExceptionCode.SCHEDULE_NOT_FOUND)
class ComciganApiFetchFailedException : BasicException(ScheduleExceptionCode.COMCIGAN_API_FAILED)
class ComciganSchoolNotFoundException : BasicException(ScheduleExceptionCode.COMCIGAN_SCHOOL_NOT_FOUND)

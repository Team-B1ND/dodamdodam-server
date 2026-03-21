package com.b1nd.dodamdodam.user.domain.teacher.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class TeacherNotFoundException(): BasicException(TeacherExceptionCode.TEACHER_NOT_FOUND)

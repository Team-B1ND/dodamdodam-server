package com.b1nd.dodamdodam.notice.domain.notice.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class NoticeNotFoundException : BasicException(NoticeExceptionCode.NOTICE_NOT_FOUND)
class NotNoticeAuthorException : BasicException(NoticeExceptionCode.NOT_NOTICE_AUTHOR)
class InvalidNoticeRequestException : BasicException(NoticeExceptionCode.INVALID_NOTICE_REQUEST)
class UserNotFoundException : BasicException(NoticeExceptionCode.USER_NOT_FOUND)
class UserServiceException : BasicException(NoticeExceptionCode.USER_SERVICE_ERROR)

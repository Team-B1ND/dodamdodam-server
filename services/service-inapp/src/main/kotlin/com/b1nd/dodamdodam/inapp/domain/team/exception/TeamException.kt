package com.b1nd.dodamdodam.inapp.domain.team.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class TeamNotFoundException: BasicException(TeamExceptionCode.TEAM_NOT_FOUND);

class TeamNameAlreadyExistException: BasicException(TeamExceptionCode.TEAM_NAME_ALREADY_EXIST);
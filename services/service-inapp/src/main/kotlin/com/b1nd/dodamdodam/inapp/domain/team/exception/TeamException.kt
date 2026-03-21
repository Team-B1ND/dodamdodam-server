package com.b1nd.dodamdodam.inapp.domain.team.exception

import com.b1nd.dodamdodam.core.common.exception.BasicException

class TeamNotFoundException: BasicException(TeamExceptionCode.TEAM_NOT_FOUND);

class TeamNameAlreadyExistException: BasicException(TeamExceptionCode.TEAM_NAME_ALREADY_EXIST);

class TeamOwnerPermissionRequiredException: BasicException(TeamExceptionCode.TEAM_OWNER_PERMISSION_REQUIRED);

class TeamCannotRemoveOwnerException: BasicException(TeamExceptionCode.TEAM_CANNOT_REMOVE_OWNER);

class TeamMemberNotFoundException: BasicException(TeamExceptionCode.TEAM_MEMBER_NOT_FOUND);
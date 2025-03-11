package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class AlreadyApplyCreativeClubException extends CustomException {
  public AlreadyApplyCreativeClubException() {
    super(ClubExceptionCode.ALREADY_USER_JOIN_CREATIVE_CLUB);
  }
}

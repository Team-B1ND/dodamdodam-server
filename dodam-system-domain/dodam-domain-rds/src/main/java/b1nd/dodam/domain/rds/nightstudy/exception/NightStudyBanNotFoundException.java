package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public class NightStudyBanNotFoundException extends CustomException {
  public NightStudyBanNotFoundException() {
    super(NightStudyExceptionCode.BAN_NOT_FOUND);
  }
}

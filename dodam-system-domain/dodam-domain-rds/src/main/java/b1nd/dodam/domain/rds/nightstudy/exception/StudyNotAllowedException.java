package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public class StudyNotAllowedException extends CustomException {

  public StudyNotAllowedException() {
    super(NightStudyExceptionCode.STUDY_NOT_ALLOWED);
  }

}
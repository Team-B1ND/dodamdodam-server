package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

public final class ApprovalAlarmUtil {
    public ApprovalAlarmUtil() {}

    public static ApprovalAlarmEvent createAlarmEvent(String pushToken, String target, String rejectReason, ApprovalStatus status) {
        return new ApprovalAlarmEvent(
                pushToken,
                target,
                createBody(target, rejectReason, status)
        );
    }

    private static String createBody(String target, String rejectReason, ApprovalStatus status){
        return switch (status) {

            case ALLOWED -> target+"이 승인됐어요!";
            case REJECTED -> target+"이 거절됐어요\n"+ (rejectReason != null ? "사유: " + rejectReason : "");
            case PENDING -> target+"이 승인 취소됐어요";
        };
    }
}

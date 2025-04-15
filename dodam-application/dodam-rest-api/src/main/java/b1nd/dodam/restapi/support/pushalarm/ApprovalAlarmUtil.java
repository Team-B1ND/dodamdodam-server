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

    private static String createBody(String target, String reason, ApprovalStatus status) {
        return switch (status) {
            case ALLOWED -> target + "이 승인되었어요!";
            case REJECTED -> target + "이 거절되었어요\n" + (reason != null ? "사유: " + reason : "");
            case PENDING -> target + "이 승인 취소되었어요";
            case BANNED -> target + "이 정지 되었어요😢\n" + (reason != null ? "사유: " + reason : "");
        };
    }
}

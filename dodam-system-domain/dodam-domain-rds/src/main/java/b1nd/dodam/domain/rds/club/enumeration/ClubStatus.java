package b1nd.dodam.domain.rds.club.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum ClubStatus {
    ALLOWED,
    PENDING,
    REJECTED,
    WAITING,
    DELETED;

    public static List<ClubStatus> getNotAllowedStatuses() {
        List<ClubStatus> statuses = new ArrayList<>();
        statuses.add(WAITING);
        statuses.add(REJECTED);
        statuses.add(DELETED);
        return statuses;
    }
}
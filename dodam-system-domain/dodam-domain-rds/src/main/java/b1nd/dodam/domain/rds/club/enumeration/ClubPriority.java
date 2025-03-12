package b1nd.dodam.domain.rds.club.enumeration;

import java.util.List;

public enum ClubPriority {
    CREATIVE_ACTIVITY_CLUB_1,
    CREATIVE_ACTIVITY_CLUB_2,
    CREATIVE_ACTIVITY_CLUB_3;

    public static List<ClubPriority> getClubPriorities(){
        return List.of(CREATIVE_ACTIVITY_CLUB_1,  CREATIVE_ACTIVITY_CLUB_2, CREATIVE_ACTIVITY_CLUB_3);
    }
}

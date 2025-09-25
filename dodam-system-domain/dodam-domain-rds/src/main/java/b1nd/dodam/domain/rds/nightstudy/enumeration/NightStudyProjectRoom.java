package b1nd.dodam.domain.rds.nightstudy.enumeration;

import java.util.Arrays;
import java.util.List;

public enum NightStudyProjectRoom {
    LAB_1, LAB_2, LAB_8, LAB_13;

    public static List<NightStudyProjectRoom> getAll() {
        return Arrays.stream(NightStudyProjectRoom.values()).toList();
    }
}

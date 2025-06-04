package b1nd.dodam.domain.rds.nightstudy.enumeration;

import java.util.Arrays;
import java.util.List;

public enum NightStudyProjectRoom {
    LAB_12, LAB_13, LAB_14, LAB_15, LAB_16;

    public static List<NightStudyProjectRoom> getAll() {
        return Arrays.stream(NightStudyProjectRoom.values()).toList();
    }
}

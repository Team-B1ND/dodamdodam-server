package b1nd.dodamcore.common.util;

import b1nd.dodamcore.meal.application.dto.Food;

import java.util.List;

public class StringConverter {

    public static String of(String originalString) {
        return originalString.replaceAll("[0-9].", "")
                .replaceAll("[()]", "")
                .replaceAll("\\.", "")
                .replaceAll("  ", "")
                .replaceAll("<br/>", ", ");
    }

    public static List<Food> toList(String originalString) {
        return originalString
                .replaceAll("\\*", "")
                .replaceAll("<br/>", ", ")
                .split(",\\s*");
    }
}

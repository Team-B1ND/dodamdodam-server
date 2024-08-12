package b1nd.dodam.neis.meal.client.support;

import b1nd.dodam.neis.meal.client.data.FoodDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class MealConverter {

    private MealConverter() {}

    public static String of(String originalString) {
        return originalString
                .replaceAll("[0-9].", "")
                .replaceAll("[()]", "")
                .replaceAll("\\.", "")
                .replaceAll("  ", "")
                .replaceAll("<br/>", ", ");
    }

    public static Float getCalorie(String originalString) {
        return Float.valueOf(originalString
                .replaceAll("[^\\d.]", ""));
    }

    public static List<FoodDetail> getFoodDetails(String originalString) {
        return parseFoods(originalString
                .replaceAll("\\*", "")
                .replaceAll("<br/>", ",")
                .replaceAll(" ", "")
                .split(",\\s*"));
    }

    private static List<FoodDetail> parseFoods(String[] items) {
        List<FoodDetail> details = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.+?)\\(([\\d.]+)\\)");

        for (String item : items) {
            Matcher matcher = pattern.matcher(item);
            if (matcher.matches()) {
                String name = matcher.group(1);
                List<Integer> allergies = Arrays.stream(matcher.group(2).split("\\."))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                details.add(new FoodDetail(name, allergies));
            } else {
                details.add(new FoodDetail(item, Collections.emptyList()));
            }
        }

        return details;
    }

}

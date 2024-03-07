package b1nd.dodamcore.meal.application;

import b1nd.dodamcore.meal.application.dto.Food;
import b1nd.dodamcore.meal.application.dto.FoodDetail;
import b1nd.dodamcore.meal.application.dto.Meal;
import b1nd.dodamcore.meal.application.dto.NewMeal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MealService {

    private final MealClient mealClient;

    public NewMeal getMealWithCalorie(int year, int month, int day) {
        String response = mealClient.getMeal(String.format("%04d%02d%02d", year, month, day));
        LocalDate localDate = LocalDate.of(year, month, day);

        if (response.contains("INFO-200")) {
            return new NewMeal(false, localDate, null, null, null);
        }

        JSONArray rawData = getRawData(response);
        Map<String, Food> mealMap = new HashMap<>();

        for (Object raw : rawData) {
            JSONObject data = (JSONObject) raw;
            String mealType = String.valueOf(data.get("MMEAL_SC_NM"));
            List<FoodDetail> details = MealConverter.getFoodDetails(String.valueOf(data.get("DDISH_NM")));
            Float calorie = MealConverter.getCalorie(String.valueOf(data.get("CAL_INFO")));
            mealMap.put(mealType, new Food(details, calorie));
        }

        return NewMeal.builder()
                .exists(true)
                .date(localDate)
                .breakfast(mealMap.getOrDefault("조식", null))
                .lunch(mealMap.getOrDefault("중식", null))
                .dinner(mealMap.getOrDefault("석식", null))
                .build();
    }

    @Cacheable(value = "meal-of-day", key = "#year.toString().concat(-#month).concat(-#day)")
    public Meal getMeal(int year, int month, int day) {
        return retrieveMealOrCalorie(year, month, day, "meal");
    }

    @Cacheable(value = "meal-of-month", key = "#year.toString().concat(-#month)")
    public List<Meal> getMealOfMonth(int year, int month) {
        return retrieveMealOrCalorieOfMonth(year, month, "meal");
    }

    @Cacheable(value = "calorie-of-day", key = "#year.toString().concat(-#month).concat(-#day)")
    public Meal getCalorie(int year, int month, int day) {
        return retrieveMealOrCalorie(year, month, day, "calorie");
    }

    @Cacheable(value = "calorie-of-month", key = "#year.toString().concat(-#month)")
    public List<Meal> getCalorieOfMonth(int year, int month) {
        return retrieveMealOrCalorieOfMonth(year, month, "calorie");
    }

    private Meal retrieveMealOrCalorie(int year, int month, int day, String type) {
        String response = mealClient.getMeal(String.format("%04d%02d%02d", year, month, day));
        LocalDate localDate = LocalDate.of(year, month, day);

        if (response.contains("INFO-200")) {
            return new Meal(false, localDate, null, null, null);
        }

        JSONArray rawData = getRawData(response);
        Map<String, String> mealMap = new HashMap<>();

        for (Object raw : rawData) {
            JSONObject data = (JSONObject) raw;
            String mealType = String.valueOf(data.get("MMEAL_SC_NM"));
            String value = type.equals("meal") ? MealConverter.of(String.valueOf(data.get("DDISH_NM"))) :
                    String.valueOf(data.get("CAL_INFO"));
            mealMap.put(mealType, value);
        }

        return Meal.builder()
                .exists(true)
                .date(localDate)
                .breakfast(mealMap.getOrDefault("조식", null))
                .lunch(mealMap.getOrDefault("중식", null))
                .dinner(mealMap.getOrDefault("석식", null))
                .build();
    }

    private List<Meal> retrieveMealOrCalorieOfMonth(int year, int month, String type) {
        String response = mealClient.getMeal(String.format("%04d%02d", year, month));

        if (response.contains("INFO-200")) {
            return Collections.emptyList();
        }

        JSONArray rawData = getRawData(response);
        Map<LocalDate, Meal> mealMap = new TreeMap<>();

        for (Object object : rawData) {
            JSONObject meal = (JSONObject) object;
            String ymd = String.valueOf(meal.get("MLSV_YMD"));
            String mealType = String.valueOf(meal.get("MMEAL_SC_NM"));
            LocalDate date = LocalDate.of(year, month, Integer.parseInt(ymd.substring(ymd.length() - 2)));
            String value = type.equals("meal") ? MealConverter.of(String.valueOf(meal.get("DDISH_NM"))) :
                    String.valueOf(meal.get("CAL_INFO"));

            Meal existingMeal = mealMap.get(date);
            if (existingMeal == null) {
                existingMeal = new Meal(true, date, null, null, null);
                mealMap.put(date, existingMeal);
            }

            switch (mealType) {
                case "조식" -> existingMeal.setBreakfast(value);
                case "중식" -> existingMeal.setLunch(value);
                case "석식" -> existingMeal.setDinner(value);
            }
        }

        return new ArrayList<>(mealMap.values());
    }

    @SneakyThrows
    private JSONArray getRawData(String response) {
        JSONParser jsonParser = new JSONParser();
        JSONObject parse = (JSONObject) jsonParser.parse(response);
        JSONArray serviceDietInfo = (JSONArray) parse.get("mealServiceDietInfo");
        JSONObject mealInfo = (JSONObject) serviceDietInfo.get(1);

        return (JSONArray) mealInfo.get("row");
    }
}

package b1nd.dodam.neis.meal.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.neis.meal.client.data.Food;
import b1nd.dodam.neis.meal.client.data.FoodDetail;
import b1nd.dodam.neis.meal.client.data.Meal;
import b1nd.dodam.neis.meal.client.properties.NeisProperties;
import b1nd.dodam.neis.meal.client.support.MealConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NeisMealClient {

    private final NeisProperties neisProperties;
    private final WebClientSupport webClient;

    @Cacheable(value = "meal-day", key = "#year.toString().concat(-#month).concat(-#day)")
    public Meal getMeal(int year, int month, int day) {
        String response = getByDate(String.format("%04d%02d%02d", year, month, day));
        LocalDate localDate = LocalDate.of(year, month, day);

        if (response.contains("INFO-200")) {
            return new Meal(false, localDate, null, null, null);
        }

        JSONArray rawData = getRawData(response);
        Map<String, Food> mealMap = new HashMap<>();

        for (Object raw : rawData) {
            JSONObject mealData = (JSONObject) raw;
            String mealType = String.valueOf(mealData.get("MMEAL_SC_NM"));
            List<FoodDetail> details = MealConverter.getFoodDetails(String.valueOf(mealData.get("DDISH_NM")));
            Float calorie = MealConverter.getCalorie(String.valueOf(mealData.get("CAL_INFO")));
            mealMap.put(mealType, new Food(details, calorie));
        }

        return Meal.builder()
                .exists(true)
                .date(localDate)
                .breakfast(mealMap.getOrDefault("조식", null))
                .lunch(mealMap.getOrDefault("중식", null))
                .dinner(mealMap.getOrDefault("석식", null))
                .build();
    }

    @Cacheable(value = "meal-month", key = "#year.toString().concat(-#month)")
    public List<Meal> getMealOfMonth(int year, int month) {
        String response = getByDate(String.format("%04d%02d", year, month));

        if (response.contains("INFO-200")) {
            return Collections.emptyList();
        }

        JSONArray rawData = getRawData(response);
        Map<LocalDate, Meal> mealMap = new TreeMap<>();

        for (Object object : rawData) {
            JSONObject mealData = (JSONObject) object;
            String ymd = String.valueOf(mealData.get("MLSV_YMD"));
            String mealType = String.valueOf(mealData.get("MMEAL_SC_NM"));
            LocalDate date = LocalDate.of(year, month, Integer.parseInt(ymd.substring(ymd.length() - 2)));
            List<FoodDetail> details = MealConverter.getFoodDetails(String.valueOf(mealData.get("DDISH_NM")));
            Float calorie = MealConverter.getCalorie(String.valueOf(mealData.get("CAL_INFO")));

            Food food = new Food(details, calorie);

            Meal existingMeal = mealMap.get(date);
            if (existingMeal == null) {
                existingMeal = new Meal(true, date, null, null, null);
                mealMap.put(date, existingMeal);
            }

            switch (mealType) {
                case "조식" -> existingMeal.setBreakfast(food);
                case "중식" -> existingMeal.setLunch(food);
                case "석식" -> existingMeal.setDinner(food);
            }
        }

        return new ArrayList<>(mealMap.values());
    }

    private String getByDate(String date) {
        return webClient.get(
                UriComponentsBuilder.fromUriString(neisProperties.getUrl())
                        .build(neisProperties.getApiKey(), date).toString(),
                String.class
        ).block();
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

package b1nd.dodamapi.meal;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.meal.application.MealService;
import b1nd.dodamcore.meal.application.dto.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    public ResponseData<Meal> getMeal(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        Meal meal = mealService.getMeal(year, month, day);
        return ResponseData.ok("급식 조회 성공", meal);
    }

    @GetMapping("/month")
    public ResponseData<List<Meal>> getMealOfMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        List<Meal> meals = mealService.getMealOfMonth(year, month);
        return ResponseData.ok("한달 급식 조회 성공", meals);
    }
}

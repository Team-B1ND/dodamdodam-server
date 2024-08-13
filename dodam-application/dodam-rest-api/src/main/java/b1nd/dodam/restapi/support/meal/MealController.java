package b1nd.dodam.restapi.support.meal;

import b1nd.dodam.neis.meal.client.NeisMealClient;
import b1nd.dodam.neis.meal.client.data.Meal;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/meal")
@RequiredArgsConstructor
public class MealController {

    private final NeisMealClient neisMealClient;

    @GetMapping
    public ResponseData<Meal> getMeal(@RequestParam int year,
                                      @RequestParam int month,
                                      @RequestParam int day) {
        return ResponseData.ok("급식 조회 성공", neisMealClient.getMeal(year, month, day));
    }

    @GetMapping("/month")
    public ResponseData<List<Meal>> getMealOfMonth(@RequestParam int year,
                                                   @RequestParam int month) {
        return ResponseData.ok("한달 급식 조회 성공", neisMealClient.getMealOfMonth(year, month));
    }

}

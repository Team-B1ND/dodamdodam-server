package b1nd.dodam.restapi.support.meal;

import b1nd.dodam.neis.meal.client.data.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class MealUseCase {

    @Cacheable(value = "meal-day", key = "#meal.date.format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-M-d'))")
    public Meal saveMeal(Meal meal) {
        return meal;
    }
}

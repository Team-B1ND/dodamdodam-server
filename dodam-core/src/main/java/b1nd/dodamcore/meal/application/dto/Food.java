package b1nd.dodamcore.meal.application.dto;

import java.util.List;

public record Food(List<FoodDetail> details, Float calorie) {
}

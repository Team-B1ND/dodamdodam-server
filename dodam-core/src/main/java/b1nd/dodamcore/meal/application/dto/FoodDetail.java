package b1nd.dodamcore.meal.application.dto;

import java.util.List;

public record FoodDetail(String name, List<Integer> allergies) {
}

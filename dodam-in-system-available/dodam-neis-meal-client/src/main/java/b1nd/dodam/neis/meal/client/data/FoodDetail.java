package b1nd.dodam.neis.meal.client.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class FoodDetail implements Serializable {

    private String name;
    private List<Integer> allergies;
}

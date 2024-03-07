package b1nd.dodamcore.meal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal implements Serializable {

    private boolean exists;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Food breakfast;

    private Food lunch;

    private Food dinner;

    public void setBreakfast(Food breakfast) {
        this.breakfast = breakfast;
    }
    public void setLunch(Food lunch) {
        this.lunch = lunch;
    }
    public void setDinner(Food dinner) {
        this.dinner = dinner;
    }
}

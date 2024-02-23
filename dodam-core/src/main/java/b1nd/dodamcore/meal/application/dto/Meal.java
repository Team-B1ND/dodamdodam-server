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

    private String breakfast;

    private String lunch;

    private String dinner;

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }
    public void setLunch(String lunch) {
        this.lunch = lunch;
    }
    public void setDinner(String dinner) {
        this.dinner = dinner;
    }
}

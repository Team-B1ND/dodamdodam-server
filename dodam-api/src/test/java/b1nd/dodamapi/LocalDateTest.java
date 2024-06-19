package b1nd.dodamapi;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class LocalDateTest {

    @Test
    void localDate(){
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate);
    }
}

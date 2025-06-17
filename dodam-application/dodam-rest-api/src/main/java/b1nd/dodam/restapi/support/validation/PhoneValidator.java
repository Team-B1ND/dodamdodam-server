package b1nd.dodam.restapi.support.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String PHONE_PATTERN =
            "^(01[0-9]-\\d{3,4}-\\d{4}|" +
            "0[2-9][0-9]{0,1}-\\d{3,4}-\\d{4})$";

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) return true;
        return pattern.matcher(phone).matches();
    }
}

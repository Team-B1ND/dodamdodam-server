package b1nd.dodam.restapi.support.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String PHONE_PATTERN =
            "^(01[016789]|02|0[3-6][0-9])\\d{7,8}$";

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) return true;
        return pattern.matcher(phone).matches();
    }
}

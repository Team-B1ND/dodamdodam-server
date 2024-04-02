package b1nd.dodamapi.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ListValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyList {

    String message() default "must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

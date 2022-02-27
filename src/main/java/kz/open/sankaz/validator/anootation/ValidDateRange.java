package kz.open.sankaz.validator.anootation;

import kz.open.sankaz.validator.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message () default "'Дата по' не может быть раньше 'Дата с'";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}

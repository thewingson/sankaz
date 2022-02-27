package kz.open.sankaz.validator;

import kz.open.sankaz.pojo.filter.BookingCreateFilter;
import kz.open.sankaz.validator.anootation.ValidDateRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingCreateFilter> {
    @Override
    public boolean isValid(BookingCreateFilter value, ConstraintValidatorContext context) {
        return value.getEndDate().isAfter(value.getStartDate());
    }
}
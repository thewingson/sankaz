package kz.open.sankaz.validator;

import kz.open.sankaz.pojo.filter.BookingCreateDateInfoFilter;
import kz.open.sankaz.validator.anootation.ValidDateRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingCreateDateInfoFilter> {
    @Override
    public boolean isValid(BookingCreateDateInfoFilter value, ConstraintValidatorContext context) {
        return value.getEndDate().isAfter(value.getStartDate());
    }
}
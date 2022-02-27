package kz.open.sankaz.validator;

import kz.open.sankaz.pojo.filter.BookingCreateFilter;
import kz.open.sankaz.validator.anootation.ValidBookingUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingUserValidator implements ConstraintValidator<ValidBookingUser, BookingCreateFilter> {
    @Override
    public boolean isValid(BookingCreateFilter value, ConstraintValidatorContext context) {
        if (value.getUserId() != null){
            return true;
        }
        if (value.getFirstName() != null && value.getLastName() != null && value.getTelNumber() != null){
            return true;
        }
        return false;
    }
}
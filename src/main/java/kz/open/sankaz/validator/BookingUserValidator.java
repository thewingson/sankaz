package kz.open.sankaz.validator;

import kz.open.sankaz.pojo.filter.BookingCreateUserInfoFilter;
import kz.open.sankaz.validator.anootation.ValidBookingUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingUserValidator implements ConstraintValidator<ValidBookingUser, BookingCreateUserInfoFilter> {
    @Override
    public boolean isValid(BookingCreateUserInfoFilter value, ConstraintValidatorContext context) {
        if (value.getUserId() != null){
            return true;
        }
        if (value.getFirstName() != null && value.getLastName() != null && value.getTelNumber() != null){
            return true;
        }
        return false;
    }
}
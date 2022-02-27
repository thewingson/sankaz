package kz.open.sankaz.validator;

import kz.open.sankaz.model.enums.BookingStatus;
import kz.open.sankaz.pojo.filter.BookingAdminCreateFilter;
import kz.open.sankaz.validator.anootation.ValidBookingStatusDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStatusDateValidator implements ConstraintValidator<ValidBookingStatusDate, BookingAdminCreateFilter> {
    @Override
    public boolean isValid(BookingAdminCreateFilter value, ConstraintValidatorContext context) {
        if (value.getStatus().equals(BookingStatus.APPROVED.name())) {
            if(value.getApprovedDate() == null) return false;
        }
        if (value.getStatus().equals(BookingStatus.CANCELLED.name())) {
            if(value.getCancelledDate() == null) return false;
        }
        if (value.getStatus().equals(BookingStatus.PAID.name())) {
            if(value.getApprovedDate() == null || value.getPaidDate() == null) return false;
        }
        return true;
    }
}
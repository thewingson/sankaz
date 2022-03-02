package kz.open.sankaz.pojo.filter;

import kz.open.sankaz.validator.anootation.ValidBookingUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ValidBookingUser
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BookingCreateUserInfoFilter extends BookingCreateDateInfoFilter {
    private Long userId;
    @NotEmpty
    @Size(min = 1)
    private String firstName;
    @NotEmpty
    @Size(min = 1)
    private String lastName;
    @NotEmpty
    @Size(min = 12, max = 12)
    private String telNumber;
}

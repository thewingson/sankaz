package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOrganizationDto {
    private int confirmationNumber;
    private String telNumber;
    private String password;
}

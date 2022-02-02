package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterOrgDto extends BaseDto {
    private String iin;
    private String orgName;
    private String fullName;
    private String iban;
    private String telNumber;
    private String email;
}

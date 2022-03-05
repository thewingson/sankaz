package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecUserDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private String telNumber;
    private String email;
    private String username;
    private String userType;
    private DictionaryLangDto city;
    private DictionaryLangDto gender;
    private String confirmationStatus;
}

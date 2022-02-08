package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FinishRegDto extends BaseDto {
    private String telNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String city;
    private String password;
}

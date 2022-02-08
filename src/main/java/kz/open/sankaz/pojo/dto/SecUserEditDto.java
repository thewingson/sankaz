package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecUserEditDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String city;
}

package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto extends AbstractDto {
    private String name;
    private String managerFullName;
    private String iin;
    private String iban;
    private String email;
    private String telNumber;
    private String confirmationStatus;
    private LocalDateTime confirmedTs;
    private String confirmedBy;
    private SecUserDto user;
}

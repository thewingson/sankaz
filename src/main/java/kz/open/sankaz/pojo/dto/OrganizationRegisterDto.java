package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRegisterDto extends AbstractDto {
    private String name;
    private String managerFullName;
    private String iin;
    private String iban;
    private String email;
    private String telNumber;
    private String confirmationStatus;
    private String confirmedBy;
    private LocalDateTime approvedDate;
    private Long orgId;
    private List<SanForMainDto> sans;
}

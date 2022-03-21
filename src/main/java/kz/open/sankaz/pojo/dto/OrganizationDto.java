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
public class OrganizationDto extends AbstractDto {
    private String name;
    private String managerFullName;
    private String iin;
    private String iban;
    private String email;
    private String telNumber;
    private String confirmationStatus;
    private LocalDateTime approvedDate;
    private String confirmedBy;
    private SecUserInOrgDto user;
    private List<FileDto> pics;
    private String companyName;
    private String description;
    private String address;
    private String instagramLink;
    private String siteLink;
    private DictionaryLangDto companyCategory;
    private String rejectMessage;
}

package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAddDataDto extends BaseDto {
    private String companyName;
    private String description;
    private String address;
    private String instagramLink;
    private String siteLink;
    private String categoryCode;
}

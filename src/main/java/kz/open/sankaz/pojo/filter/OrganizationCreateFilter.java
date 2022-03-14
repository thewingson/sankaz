package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationCreateFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 12, max = 12)
    private String iin;
    @NotEmpty
    @Size(min = 3)
    private String name;
    @NotEmpty
    @Size(min = 3)
    private String managerFullName;
    @NotEmpty
    @Size(min = 9)
    private String iban;
    @NotEmpty
    @Size(min = 12)
    private String telNumber;
    @NotEmpty
    @Size(min = 8)
    private String email;
    @NotNull
    private Long companyCategoryId;
    @NotNull
    private Long userId;
    private String instagramLink;
    private String siteLink;
    private String description;
    private String address;
    private String companyName;
    private String confirmationStatus = "NEW";
}

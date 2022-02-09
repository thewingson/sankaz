package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationRegisterFinishFilter extends BaseFilter {
    @NotEmpty(message = "{OrganizationRegisterFinishFilter.iin.NotEmpty}")
    @Size(min = 12, max = 12, message = "{OrganizationRegisterFinishFilter.iin.Size}")
    private String iin;
    @NotEmpty(message = "{OrganizationRegisterFinishFilter.orgName.NotEmpty}")
    @Size(min = 3, message = "{OrganizationRegisterFinishFilter.orgName.Size}")
    private String orgName;
    @NotEmpty(message = "{OrganizationRegisterFinishFilter.fullName.NotEmpty}")
    @Size(min = 3, message = "{OrganizationRegisterFinishFilter.fullName.Size}")
    private String fullName;
    @NotEmpty(message = "{OrganizationRegisterFinishFilter.iban.NotEmpty}")
    @Size(min = 9, max = 34, message = "{OrganizationRegisterFinishFilter.iban.Size}")
    private String iban;
    @NotEmpty(message = "{TelNumberFilter.telNumber.NotEmpty}")
    @Size(min = 12, max = 12, message = "{TelNumberFilter.telNumber.Size}")
    private String telNumber;
    @NotEmpty(message = "{FinishRegFilter.email.NotEmpty}")
    @Size(min = 8, message = "{FinishRegFilter.email.Size}")
    private String email;
}

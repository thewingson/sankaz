package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEditFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 10, max = 2000)
    private String description;
    @NotNull
    private String address;
    private String instagramLink;
    private String siteLink;
    @NotNull
    private Long categoryId;
}

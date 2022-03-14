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
public class SanCreateFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 3)
    private String name;
    @NotEmpty
    @Size(min = 10, max = 2000)
    private String description;
    private String instagramLink;
    private String siteLink;
    private String[] telNumbers;
    @NotNull
    private Long sanTypeId;
    @NotNull
    private Long cityId;
    @NotNull
    private Long orgId;
    private Double longitude;
    private Double latitude;
    @NotNull
    private String address;
}
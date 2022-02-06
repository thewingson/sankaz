package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanCreateDto {
    private String name;
    private String description;
    private String instagramLink;
    private String siteLink;
    private String[] telNumbers;
    private long[] sanTypes;
}
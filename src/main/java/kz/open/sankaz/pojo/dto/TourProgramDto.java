package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourProgramDto extends AbstractDto {
    private String shortDescription;
    private BigDecimal price;
    private TourDto tour;
    private List<ProgramDayDto> days;
    private List<ProgramServiceDto> services;
}

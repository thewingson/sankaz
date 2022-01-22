package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramServiceDto extends AbstractDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private TourProgramDto program;
}

package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourDto extends AbstractDto {
    private Long id;
    private String name;
    private String description;
    private List<TourProgramDto> programs;
}

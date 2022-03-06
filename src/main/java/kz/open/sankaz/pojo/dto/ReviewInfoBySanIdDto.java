package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInfoBySanIdDto extends BaseDto {
    private Double avgRating;
    private Integer reviewCount;
    private Map<Integer, Integer> ratings;
    private List<ReviewBySanIdDto> reviews;
}

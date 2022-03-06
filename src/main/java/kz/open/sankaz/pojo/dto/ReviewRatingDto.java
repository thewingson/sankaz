package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewRatingDto {
    private float rating = 0.0f;
    private int count = 0;
}

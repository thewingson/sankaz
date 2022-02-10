package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewBySanIdDto extends BaseDto {
    protected String username;
    private Float rating;
    protected String text;
    private Long parentReviewId;
}

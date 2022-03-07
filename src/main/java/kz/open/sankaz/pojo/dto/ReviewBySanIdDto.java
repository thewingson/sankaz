package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewBySanIdDto extends BaseDto {
    private Long userId;
    private String username;
    private Float rating;
    private LocalDateTime reviewDate;
    private String text;
    private Long parentReviewId;
    private List<ReviewBySanIdDto> childReviews;
}

package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewBySanIdDto extends BaseDto {
    protected Long userId;
    protected String username;
    private Float rating;
    private LocalDateTime reviewDate;
    protected String text;
    private Long parentReviewId;
}

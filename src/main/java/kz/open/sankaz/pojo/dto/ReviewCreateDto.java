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
public class ReviewCreateDto extends AbstractDto {
    private LocalDateTime reviewDate;
    private Float rating;
    protected String text;
    protected String username;
    private Long parentReviewId;
    protected String fullName;
    protected Long sanId;
}

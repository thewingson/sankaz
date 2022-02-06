package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto extends AbstractDto {
    private Float rating;
    protected String text;
    protected String username;
    private Long parentReviewId;

    protected String fullName;
    protected Long sanId;
}

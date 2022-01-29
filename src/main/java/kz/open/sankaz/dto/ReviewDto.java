package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto extends AbstractDto {
    private Float rating;
    private SanDto san;
    protected String text;
    protected String username;
    private Long parentReview;
}

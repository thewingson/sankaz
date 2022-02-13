package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateFilter extends BaseFilter {
    private Long id;
    private Float rating;
    @NotEmpty
    protected String text;
    @NotEmpty
    protected String username;
    private Long parentReviewId;
}

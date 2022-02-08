package kz.open.sankaz.pojo.filter;

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
public class SanForMainFilter extends BaseFilter {
    private Long cityId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<AgeFilter> tickets;
}
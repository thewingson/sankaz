package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingModerAllDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private Long classId;
    private Long roomId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}

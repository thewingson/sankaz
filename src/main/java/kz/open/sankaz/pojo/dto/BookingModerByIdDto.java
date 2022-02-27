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
public class BookingModerByIdDto extends AbstractDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer adults;
    private Integer children;
    private String className;
    private Long roomId;
    private String firstName;
    private String lastName;
    private String telNumber;
    private String status;
    private BigInteger price;
    private boolean bookedByUser;
}

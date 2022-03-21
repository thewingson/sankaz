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
public class ConfirmationStatusDto {
    private Long orgId;
    private String confirmationStatus;
    private String rejectMessage;
    private LocalDateTime requestDate;
    private LocalDateTime approvedDate;
    private LocalDateTime rejectedDate;
}

package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class RegisterFilter extends BaseFilter {
    @NotNull(message = "{RegisterFilter.confirmationNumber.NotEmpty}")
    private Integer confirmationNumber;
    @NotEmpty(message = "{TelNumberFilter.telNumber.NotEmpty}")
    @Size(min = 12, max = 12, message = "{TelNumberFilter.telNumber.Size}")
    private String telNumber;

    public int getResetNumber(){
        return getConfirmationNumber();
    }

    public void setResetNumber(int resetNumber){
        setConfirmationNumber(resetNumber);
    }
}

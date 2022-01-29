package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDto extends AbstractDto {
    private int confirmationNumber;
    private String telNumber;

    public int getResetNumber(){
        return getConfirmationNumber();
    }

    public void setResetNumber(int resetNumber){
        setConfirmationNumber(resetNumber);
    }
}

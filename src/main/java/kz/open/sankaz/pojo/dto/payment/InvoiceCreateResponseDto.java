package kz.open.sankaz.pojo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCreateResponseDto {
    private String url;
    private String id;

//    {
//        "url":"https://ecommerce.pult24.kz/payment/view?id=11111111111111",
//            "id":"11111111111111"
//    }
}

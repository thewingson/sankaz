package kz.open.sankaz.pojo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCreateDto {
    private String merchantId;
    private BigInteger amount;
    private String callbackUrl;
    private String description;
    private String returnUrl;
    private String orderId;
    private boolean demo;



//    {
//        "merchantId": "123123123123123",
//            "callbackUrl": "http://example.com/callback",
//            "description": "описание",
//            "returnUrl": "http://example.com",
//            "amount": 10000,(Транзакция на 100 тенге. Сумма транзакции указывается в тиин(1 тенге = 100 тиин))
//        "orderId": "123",
//            "demo": true
//    }




}

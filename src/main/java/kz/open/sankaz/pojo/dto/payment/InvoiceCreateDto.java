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
    private String reference_id;
    private BigDecimal amount;
    private String user_phone;
    private String email;
    private String merchant_name;
    private String back_url;
    private String description;
    private String death_date;
    private String option;
    private RequestUrl request_url;

//        "reference_id" : "st123123",
//        "amount" : "10",
//        "user_phone": "87087087878",
//        "email": "example@example.com",
//        "merchant_name" : "merchViktor",
//        "request_url": {
//              "url": "https://test.ol3g.ru/",
//              "type":"POST"
//              },
//        "back_url": "https://www.test.wooppay.com",
//        "description": "auto_tets",
//        "death_date": "2025-12-25 22:42:03",
//        "option": "4"

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class RequestUrl {
        private String url;
        private RequestType type;
    }

    enum RequestType {
        GET, POST, PUT, DELETE;
    }

    public void setRequestUrl(String url, String type){
        this.request_url = new RequestUrl(url, RequestType.valueOf(type));
    }
}

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
    private String operation_url;
    private Response response;

//    {
//        "operation_url": "https://www.test.wooppay.com/invoice/operation?id=381406&key=b5165d8167817f6c857fbf8462e83637",
//            "response": {
//                   "operation_id": "1000105475",
//                "replenishment_id": "",
//                "invoice_id": "381406",
//                "key": "b5165d8167817f6c857fbf8462e83637"
//    }
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class Response {
        private String operation_id;
        private String replenishment_id;
        private String invoice_id;
        private String key;
    }
}

package kz.open.sankaz.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> implements Serializable {

    @NonNull
    String type;

    String text;

    T data;

    public ResponseModel(@NonNull String type) {
        this.type = type;
    }

    public ResponseModel(@NonNull String type, String text) {
        this.type = type;
        this.text = text;
    }

    public ResponseModel(@NonNull String type, T data) {
        this.type = type;
        this.data = data;
    }

    public ResponseModel(@NonNull String type, String text, T data) {
        this.type = type;
        this.text = text;
        this.data = data;
    }

    public static ResponseEntity<?> error(HttpStatus errorCode, String errorText) {
        return ResponseEntity.status(errorCode).body(
                ResponseModel.builder()
                        .type("error")
                        .text(errorText)
                        .build()
        );
    }

    public static ResponseEntity<?> success(Object content) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .type("success")
                        .data(content)
                        .build()
        );
    }

    public static ResponseEntity<?> successPure() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .type("success").build()
        );
    }
}
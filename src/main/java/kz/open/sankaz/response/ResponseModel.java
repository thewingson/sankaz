package kz.open.sankaz.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> implements Serializable {

    @NonNull
    String type;

    String text;

    String errorType;

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

    public ResponseModel(@NonNull String type, String text, String errorType, T data) {
        this.type = type;
        this.text = text;
        this.errorType = errorType;
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

    public static ResponseEntity<?> error(HttpStatus errorCode, String errorType, String errorText) {
        return ResponseEntity.status(errorCode).contentType(MediaType.APPLICATION_JSON).body(
                ResponseModel.builder()
                        .type("error")
                        .errorType(errorType)
                        .text(errorText)
                        .build()
        );
    }

    public static ResponseEntity<?> error(HttpStatus errorCode, String errorType, Map<String, ?> content, String errorText) {
        return ResponseEntity.status(errorCode).contentType(MediaType.APPLICATION_JSON).body(
                ResponseModel.builder()
                        .type("error")
                        .errorType(errorType)
                        .data(content)
                        .text(errorText)
                        .build()
        );
    }

    public static ResponseEntity<?> success(Object content) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                ResponseModel.builder()
                        .type("success")
                        .data(content)
                        .build()
        );
    }

    public static ResponseEntity<?> success(Map<String, Object> content) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                ResponseModel.builder()
                        .type("success")
                        .data(content)
                        .build()
        );
    }

    public static ResponseEntity<?> successPure() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                ResponseModel.builder()
                        .type("success").build()
        );
    }
}
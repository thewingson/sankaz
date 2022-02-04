package kz.open.sankaz.interceptor;

import kz.open.sankaz.response.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException() {
        return ResponseModel.error(BAD_REQUEST, "Файл превышает лимит по размеру! Максимальный размер 2МБ");
    }
}

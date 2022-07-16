package kz.open.sankaz.exception;

import java.util.Map;

public class MessageCodeException extends RuntimeException {
    private static final String ERROR_MESSAGE = "MessageCodeException";

    private String code;
    private Map<String, ?> data;

    public MessageCodeException(String code) {
        super(ERROR_MESSAGE);
        this.code = code;
    }

    public MessageCodeException(String code, Map<String, ?> data) {
        super(ERROR_MESSAGE);
        this.code = code;
        this.data = data;
    }

    public MessageCodeException(String code, Map<String, ?> data, String errMsg) {
        super(errMsg);
        this.code = code;
        this.data = data;
    }


    public String getCode() {
        return code;
    }

    public Map<String, ?> getData() {
        return data;
    }
}

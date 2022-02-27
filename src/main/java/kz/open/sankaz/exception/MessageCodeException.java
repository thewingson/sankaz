package kz.open.sankaz.exception;

public class MessageCodeException extends RuntimeException {
    private static final String ERROR_MESSAGE = "BookingException";

    private String code;

    public MessageCodeException(String code) {
        super(ERROR_MESSAGE);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}

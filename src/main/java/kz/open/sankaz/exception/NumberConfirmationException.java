package kz.open.sankaz.exception;

public class NumberConfirmationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "NumberConfirmationException";

    private String code;
    private String reason;

    public NumberConfirmationException(String code) {
        super(ERROR_MESSAGE);
        this.code = code;
        this.reason = NumberConfirmationExceptionMessages.params.get(code);
    }


    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}

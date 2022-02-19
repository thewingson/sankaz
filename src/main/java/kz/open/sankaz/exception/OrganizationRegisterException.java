package kz.open.sankaz.exception;

public class OrganizationRegisterException extends RuntimeException {
    private static final String ERROR_MESSAGE = "OrganizationRegisterException";

    private String code;
    private String reason;

    public OrganizationRegisterException(String code) {
        super(ERROR_MESSAGE);
        this.code = code;
        this.reason = OrganizationRegisterExceptionMessages.params.get(code);
    }


    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}

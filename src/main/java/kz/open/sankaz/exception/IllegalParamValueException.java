package kz.open.sankaz.exception;

public class IllegalParamValueException extends RuntimeException {
    private static final String PARAM_VALUE_NOT_FOUND = "Param by value not found!";

    public IllegalParamValueException(String param, String value) {
        super(PARAM_VALUE_NOT_FOUND + " Param: " + param + ". Value: " + value);
    }

    public IllegalParamValueException(String param, String value, Throwable err) {
        super(PARAM_VALUE_NOT_FOUND + " Param: " + param + ". Value: " + value, err);
    }
}

package kz.open.sankaz.exception;

public class IllegalParamValueException extends RuntimeException {
    private static final String PARAM_VALUE_NOT_FOUND = "Не найден параметр по значению!";

    public IllegalParamValueException(String param, String value) {
        super(PARAM_VALUE_NOT_FOUND + " Параметр: " + param + ". Значение: " + value);
    }

    public IllegalParamValueException(String param, String value, Throwable err) {
        super(PARAM_VALUE_NOT_FOUND + " Параметр: " + param + ". Значение: " + value, err);
    }
}

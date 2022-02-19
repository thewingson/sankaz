package kz.open.sankaz.exception;

import java.util.HashMap;
import java.util.Map;

public class NumberConfirmationExceptionMessages {
    public static final String NUMBER_ALREADY_CONFIRMED_CODE = "NUMBER_ALREADY_CONFIRMED_CODE";
    public static final String NUMBER_ALREADY_CONFIRMED_MESSAGE = "Номер уже подтвержден! Пожалуйста, закончите регистрацию.";

    public static final String CONFIRMATION_TIME_IS_EXPIRED = "CONFIRMATION_TIME_IS_EXPIRED";
    public static final String CONFIRMATION_TIME_IS_EXPIRED_MESSAGE = "Время номера подтверждения истек! Пожалуйста, отправьте еще раз!";

    public static final String INVALID_CONFIRMATION_NUMBER = "INVALID_CONFIRMATION_NUMBER";
    public static final String INVALID_CONFIRMATION_NUMBER_MESSAGE = "Неправильный номер подтверждения!";

    public static final Map<String, String> params = new HashMap<String, String>() {{
        put(NUMBER_ALREADY_CONFIRMED_CODE, NUMBER_ALREADY_CONFIRMED_MESSAGE);
        put(CONFIRMATION_TIME_IS_EXPIRED, CONFIRMATION_TIME_IS_EXPIRED_MESSAGE);
        put(INVALID_CONFIRMATION_NUMBER, INVALID_CONFIRMATION_NUMBER_MESSAGE);
    }};

}

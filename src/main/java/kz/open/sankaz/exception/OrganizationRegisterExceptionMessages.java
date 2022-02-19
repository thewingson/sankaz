package kz.open.sankaz.exception;

import java.util.HashMap;
import java.util.Map;

public class OrganizationRegisterExceptionMessages {
    public static final String NUMBER_NOT_CONFIRMED_CODE = "NUMBER_NOT_CONFIRMED_CODE";
    public static final String NUMBER_NOT_CONFIRMED_MESSAGE = "Номер не подтвержден! Пожалуйста, подтвердите номер.";

    public static final String EMAIL_IS_BUSY_CODE = "EMAIL_IS_BUSY_CODE";
    public static final String EMAIL_IS_BUSY_MESSAGE = "Этот email занят другой организацией! Пожалуйста, введите другой email для огранизации.";

    public static final String IBAN_IS_BUSY_CODE = "IBAN_IS_BUSY_CODE";
    public static final String IBAN_IS_BUSY_MESSAGE = "Этот IBAN занят другой организацией! Пожалуйста, введите другой IBAN для огранизаци.!";

    public static final String IIN_IS_BUSY_CODE = "IIN_IS_BUSY_CODE";
    public static final String IIN_IS_BUSY_MESSAGE = "Этот ИИН занят другой организацией! Пожалуйста, введите другой ИИН для огранизации.";

    public static final String TEL_NUMBER_IS_BUSY_CODE = "TEL_NUMBER_IS_BUSY_CODE";
    public static final String TEL_NUMBER_IS_BUSY_MESSAGE = "Этот номер занят другой организацией! Пожалуйста, введите другой номер для огранизации.";

    public static final String ORG_ALREADY_REGISTERED_CODE = "ORG_ALREADY_REGISTERED_CODE";
    public static final String ORG_ALREADY_REGISTERED_MESSAGE = "Вы уже зарегистрировали организацию! Пользователь может создать только одну огранизацию.";


    public static final Map<String, String> params = new HashMap<String, String>() {{
        put(NUMBER_NOT_CONFIRMED_CODE, NUMBER_NOT_CONFIRMED_MESSAGE);
        put(EMAIL_IS_BUSY_CODE, EMAIL_IS_BUSY_MESSAGE);
        put(IBAN_IS_BUSY_CODE, IBAN_IS_BUSY_MESSAGE);
        put(IIN_IS_BUSY_CODE, IIN_IS_BUSY_MESSAGE);
        put(TEL_NUMBER_IS_BUSY_CODE, TEL_NUMBER_IS_BUSY_MESSAGE);
        put(ORG_ALREADY_REGISTERED_CODE, ORG_ALREADY_REGISTERED_MESSAGE);
    }};

}

package kz.open.sankaz.model.enums;

public enum ConfirmationStatus {
    NEW,
    ON_CONFIRMATION, //смс код жібергеннен кейінгі статус
    CONFIRMED, //смс кодты жазғаннан кейінгі статус
    FINISHED;// модератор аяқтағаннан кейін
}

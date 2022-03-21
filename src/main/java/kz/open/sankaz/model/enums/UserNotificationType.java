package kz.open.sankaz.model.enums;

public enum UserNotificationType {
    STOCK("STOCK"),
    BOOKING("BOOKING"),
    PAYMENT("PAYMENT");

    private String value;

    UserNotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.value;
    }
}

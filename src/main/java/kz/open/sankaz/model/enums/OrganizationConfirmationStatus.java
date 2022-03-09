package kz.open.sankaz.model.enums;

public enum OrganizationConfirmationStatus {
    NEW("NEW"),
    ON_CONFIRMATION("ON_CONFIRMATION"),
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED"),
    PROFILE_FINISHED("PROFILE_FINISHED"),
    SERVICE_CREATED("SERVICE_CREATED");

    private String value;

    OrganizationConfirmationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.value;
    }
}

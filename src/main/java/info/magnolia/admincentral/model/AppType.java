package info.magnolia.admincentral.model;

import java.util.Arrays;

public enum AppType {
    CONTENT_TYPE("content-type");

    private final String text;

    AppType(String text) {
        this.text = text;
    }

    public static AppType fromText(String text) {
        return Arrays.stream(AppType.values())
                .filter(type -> type.text.equals(text))
                .findFirst()
                .orElse(CONTENT_TYPE);
    }
}

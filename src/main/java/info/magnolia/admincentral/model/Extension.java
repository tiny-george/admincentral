package info.magnolia.admincentral.model;

import java.util.List;

public record Extension(String name, String description, boolean active, List<String> requiredConfig) {
}

package info.magnolia.admincentral.model;

import java.util.Map;

public record EnableExtension(String extensionId, Map<String, String> configValues) {
}

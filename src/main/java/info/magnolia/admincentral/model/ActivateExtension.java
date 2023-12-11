package info.magnolia.admincentral.model;

import java.util.Map;

public record ActivateExtension(String extensionId, Map<String, String> configValues) {
}

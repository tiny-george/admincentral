package info.magnolia.admincentral.model;

import java.util.Map;

public record ActivateExtension(String name, Map<String, String> configValues) {
}

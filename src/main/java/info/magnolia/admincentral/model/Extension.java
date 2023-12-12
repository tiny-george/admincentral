package info.magnolia.admincentral.model;

import info.magnolia.extensibility.api.ExtensionStatus;

import java.util.Set;

public record Extension(String id, String name, String description, String owner, ExtensionStatus status, boolean deployed, Set<String> multitenantConfigKeys, boolean available) {
}

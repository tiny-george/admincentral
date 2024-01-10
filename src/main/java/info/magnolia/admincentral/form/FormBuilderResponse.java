package info.magnolia.admincentral.form;

import java.util.List;

public record FormBuilderResponse(int count, List<Field> fields) {
}

package info.magnolia.admincentral.datasource;

import info.magnolia.response.Response;

import java.util.List;
import java.util.Map;

public class DatasourceResolver {

    public Response<List<Map<String, Object>>> values(String datasource) {
        return Response.ok(List.of(Map.of("id", "someId"), Map.of("id", "anotherId")));
    }
}

package info.magnolia.admincentral.datasource;

import info.magnolia.response.Response;

import java.util.List;

public class DatasourceResolver {

    public Response<List<KeyValue>> values(String datasource) {
        return Response.ok(List.of(new KeyValue("someId", "First Item"),
                new KeyValue("otherId", "Second Item")));
    }
}

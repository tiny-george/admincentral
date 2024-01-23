package info.magnolia.admincentral.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.magnolia.admincentral.EnvironmentContext;
import info.magnolia.datasource.api.*;
import info.magnolia.datasource.http.HttpDatasource;
import info.magnolia.response.Response;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@RequestScoped
public class DatasourceResolver {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    EnvironmentContext environmentContext;


    public Response<List<Map<String, Object>>> values(String datasource) {
        //TODO get type from definition
        var ds = extensionDatasource(datasource);
        if (ds.hasTrait(DatasourceTrait.LIST_ITEMS) && ds.hasTrait(DatasourceTrait.ITEM_RESOLVER)) {
            Response<FetchData<Map<String, Object>>> items = ((ListItems)ds).fetch(environmentContext.context());
            if (items.hasError()) {
                return Response.error(items.getError());
            }
            List<Map<String, Object>> all = items.get().items();
            return Response.ok(all);
        }
        return Response.error(new Exception("Can not get values from datasource " + datasource));
    }

    private Datasource<Map> extensionDatasource(String name) {
        //TODO traits from definition
        //TODO deal with datasource types?
        //TODO get id
        Function<Map, String> function = map -> (String) map.get("id");
        Datasource<Map> ds = new HttpDatasource<>(name,
                //"http://localhost:8090",
                "https://" + name + ".exp.magnolia-cloud.com",
                DatasourceType.EXTENSION,
                Set.of(DatasourceTrait.LIST_ITEMS, DatasourceTrait.ITEM_RESOLVER),
                Map.class, function, objectMapper);
        return ds;
    }
}

package info.magnolia.admincentral.conversor;

import info.magnolia.response.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlToObject {

    private final ObjectMapper objectMapper;

    public YamlToObject() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
    }

    public <T> Response<T> toObject(String s, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(s, clazz);
            return Response.ok(object);
        } catch (JsonProcessingException e) {
            return Response.error(e);
        }
    }
}

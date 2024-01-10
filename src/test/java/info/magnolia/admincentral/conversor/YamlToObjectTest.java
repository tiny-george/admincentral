package info.magnolia.admincentral.conversor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import info.magnolia.admincentral.model.Form;
import info.magnolia.admincentral.model.Model;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class YamlToObjectTest {

    @Test
    public void fooBarMap() {
        var converter = new YamlToObject();

        var response = converter.toObject("foo: bar", Map.class);

        assertEquals("bar", response.get().get("foo"));
    }

    @Test
    public void model() {
        var converter = new YamlToObject();

        var response = converter.toObject("""
name: watch
properties:
  - name: id
  - name: price
    type: twoDecimalsNumber
  - name: name
  - name: description
  - name: color
  - name: shopify-item
    type: datasource:shopify-multi
        """, Model.class);

        assertEquals("watch", response.get().name());
        assertEquals(6, response.get().properties().size());
    }

    @Test
    public void form() {
        var converter = new YamlToObject();

        var response = converter.toObject("""
properties:
  - name: description
    label: About
  - name: shopify-item
    label: Shopify
    component:
      type: select
      value: hello {{id}}
  - name: color
    label: Color
    component:
      type: extension
      value: warp-extensions-color-picker
        """, Form.class);

        assertEquals(3, response.get().properties().size());
    }
}

package info.magnolia.admincentral.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import info.magnolia.admincentral.model.Model;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class MustacheTest {

    @Test
    public void map() {
        var mustache = new Mustache();

        assertEquals("hello world", mustache.render("hello {{message}}", Map.of("message", "world")));
        assertEquals("yo yo yo", mustache.render("yo {{message}} yo", Map.of("message", "yo")));
    }

    @Test
    public void pojo() {
        var mustache = new Mustache();

        assertEquals("pojo cool model", mustache.render("pojo {{name}}", new Model("cool model", null)));
    }
}

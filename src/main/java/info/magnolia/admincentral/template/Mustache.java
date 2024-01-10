package info.magnolia.admincentral.template;

import java.io.StringReader;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;

public class Mustache {

    private final DefaultMustacheFactory mf;

    public Mustache() {
        this.mf = new DefaultMustacheFactory();
    }

    public String render(String text, Object object) {
        var m = mf.compile(new StringReader(text), text);
        return m.execute(new StringWriter(), object).toString();
    }
}

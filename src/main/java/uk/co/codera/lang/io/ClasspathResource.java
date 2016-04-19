package uk.co.codera.lang.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ClasspathResource {

    private final String path;

    public ClasspathResource(String path) {
        this.path = path;
    }

    public String getAsString() {
        try {
            return IOUtils.toString(getAsStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private InputStream getAsStream() {
        return ClasspathResource.class.getResourceAsStream(this.path);
    }
}
package org.piccolo.parsing.util;

import static java.lang.ClassLoader.getSystemResource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceHelpers {

    public static String resource(String resource) {
        try {
            URL systemResource = getSystemResource(resource);
            return Files.readString(Path.of(systemResource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new AssertionError("Resource not found: " + e.getMessage());
        }
    }
}
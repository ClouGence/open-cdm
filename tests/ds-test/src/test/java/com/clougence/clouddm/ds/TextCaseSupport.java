package com.clougence.clouddm.ds;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class TextCaseSupport {

    public static final String CASE_DELIMITER = "----------";

    private TextCaseSupport(){
    }

    public static List<String> resourceFiles(String resourceDir) {
        return resourceFiles(resourceDir, path -> true);
    }

    public static List<String> resourceFiles(String resourceDir, Predicate<String> filter) {
        return resourceFiles(resourceDir, ".txt", filter);
    }

    public static List<String> resourceFiles(String resourceDir, String suffix, Predicate<String> filter) {
        URL url = TextCaseSupport.class.getClassLoader().getResource(resourceDir);
        if (url == null) {
            throw new IllegalArgumentException("Resource directory not found: " + resourceDir);
        }
        try {
            Path dir = Paths.get(url.toURI());
            List<String> resources = new ArrayList<>();
            try (Stream<Path> stream = Files.walk(dir)) {
                stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(suffix))
                    .sorted(Comparator.comparing(Path::toString))
                    .map(path -> resourceDir + "/" + dir.relativize(path).toString().replace('\\', '/'))
                    .filter(filter)
                    .forEach(resources::add);
            }
            return resources;
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Failed to list resource directory: " + resourceDir, e);
        }
    }

    public static List<CaseBlock> loadBlocks(String resourcePath) {
        return parseBlocks(resourcePath, readResource(resourcePath));
    }

    public static List<CaseBlock> parseBlocks(String resourcePath, String content) {
        List<CaseBlock> cases = new ArrayList<>();
        int index = 0;
        for (String part : content.split("(?m)^" + CASE_DELIMITER + "\\s*$")) {
            String block = part.strip();
            if (!block.isEmpty()) {
                index++;
                cases.add(parseBlock(resourcePath, index, block));
            }
        }
        return cases;
    }

    public static String readResource(String resourcePath) {
        try (InputStream input = TextCaseSupport.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load resource: " + resourcePath, e);
        }
    }

    public static String readOptionalLine(String text, String prefix) {
        for (String line : text.split("\\R")) {
            String stripped = line.strip();
            if (stripped.startsWith(prefix)) {
                return stripped.substring(prefix.length()).strip();
            }
        }
        return null;
    }

    public static String readRequiredLine(String text, String prefix) {
        String value = readOptionalLine(text, prefix);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing line: " + prefix);
        }
        return value;
    }

    public static String section(String text, String start, String end) {
        int startIndex = text.indexOf(start);
        if (startIndex < 0) {
            throw new IllegalArgumentException("Missing section: " + start);
        }
        startIndex += start.length();
        int endIndex = end == null ? text.length() : text.indexOf(end, startIndex);
        if (end != null && endIndex < 0) {
            throw new IllegalArgumentException("Missing section: " + end);
        }
        return text.substring(startIndex, endIndex);
    }

    private static CaseBlock parseBlock(String resourcePath, int index, String block) {
        String name = null;
        String body = block;
        int lineEnd = block.indexOf('\n');
        String firstLine = lineEnd < 0 ? block : block.substring(0, lineEnd).strip();
        if (firstLine.startsWith("[") && firstLine.endsWith("]")) {
            name = firstLine.substring(1, firstLine.length() - 1).strip();
            body = lineEnd < 0 ? "" : block.substring(lineEnd + 1).strip();
        }
        if (name == null || name.isBlank()) {
            name = resourcePath + "#" + String.format("%03d", index);
        }
        return new CaseBlock(resourcePath, name, body, index);
    }

    public record CaseBlock(String resourcePath, String name, String body, int index) {
    }
}

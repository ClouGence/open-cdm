/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.registry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/** Strict loader for datasource-owned {@code *-database-resources.xml} files. */
public final class DatabaseResourceXmlLoader {

    private static final String SCHEMA_RESOURCE = "/META-INF/clougence/database-resources.xsd";
    private static final Map<String, Group> GROUPS = Map.of(//
            "storedProcedures", new Group("procedure", RegisteredResourceType.PROCEDURE),
            "storedFunctions", new Group("function", RegisteredResourceType.FUNCTION),
            "tablesOrViews", new Group("tableOrView", RegisteredResourceType.TABLE),
            "systemTypes", new Group("systemType", RegisteredResourceType.TYPE));

    private DatabaseResourceXmlLoader(){
    }

    public static List<DatabaseResource> load(Class<?> owner, String resource) {
        InputStream input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing database resources: " + resource);
        }
        try (input) {
            byte[] document = input.readAllBytes();
            validate(document, resource);
            XMLStreamReader reader = xmlInputFactory().createXMLStreamReader(new ByteArrayInputStream(document));
            try {
                return parse(reader, resource);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read database resources: " + resource, e);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Failed to load database resources: " + resource, e);
        } catch (Exception e) {
            if (e instanceof IllegalStateException stateException) {
                throw stateException;
            }
            throw new IllegalStateException("Failed to close database resources: " + resource, e);
        }
    }

    private static void validate(byte[] document, String resource) {
        Validator validator = SchemaHolder.SCHEMA.newValidator();
        try {
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new StreamSource(new ByteArrayInputStream(document)));
        } catch (SAXException e) {
            throw new IllegalStateException("Invalid database resource " + resource + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to validate database resources: " + resource, e);
        }
    }

    private static XMLInputFactory xmlInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        set(factory, XMLInputFactory.SUPPORT_DTD, false);
        set(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        set(factory, XMLConstants.ACCESS_EXTERNAL_DTD, "");
        set(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return factory;
    }

    private static Schema loadSchema() {
        InputStream input = DatabaseResourceXmlLoader.class.getResourceAsStream(SCHEMA_RESOURCE);
        if (input == null) {
            throw new IllegalStateException("Missing database resource schema: " + SCHEMA_RESOURCE);
        }
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try (input) {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newSchema(new StreamSource(input));
        } catch (SAXException | IOException e) {
            throw new IllegalStateException("Failed to load database resource schema: " + SCHEMA_RESOURCE, e);
        }
    }

    private static List<DatabaseResource> parse(XMLStreamReader reader, String resource) throws XMLStreamException {
        moveToStart(reader, resource);
        requireElement(reader, "databaseResources", resource);
        requireNoAttributes(reader, resource);
        List<DatabaseResource> result = new ArrayList<>();
        Set<String> seenGroups = new HashSet<>();
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.END_ELEMENT) {
                requireElement(reader, "databaseResources", resource);
                break;
            }
            String groupName = reader.getLocalName();
            Group group = GROUPS.get(groupName);
            if (group == null || !seenGroups.add(groupName)) {
                throw invalid(reader, resource, "unsupported or duplicate category: " + groupName);
            }
            requireNoAttributes(reader, resource);
            parseGroup(reader, resource, groupName, group, result);
        }
        if (!seenGroups.equals(GROUPS.keySet())) {
            throw invalid(reader, resource, "all four resource categories are required");
        }
        return List.copyOf(result);
    }

    private static void parseGroup(XMLStreamReader reader, String resource, String groupName, Group group,
            List<DatabaseResource> result) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.END_ELEMENT) {
                requireElement(reader, groupName, resource);
                return;
            }
            requireElement(reader, group.element(), resource);
            result.add(parseResource(reader, resource, group));
        }
        throw invalid(reader, resource, "category is not closed: " + groupName);
    }

    private static DatabaseResource parseResource(XMLStreamReader reader, String resource, Group group)
            throws XMLStreamException {
        Set<String> allowed = new HashSet<>(Set.of("name", "catalog", "schema", "skipPermission", "environment"));
        if (group.type() == RegisteredResourceType.FUNCTION) {
            allowed.add("aggregate");
        }
        Map<String, String> attributes = new HashMap<>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String name = reader.getAttributeLocalName(i);
            if (!allowed.contains(name) || attributes.put(name, reader.getAttributeValue(i)) != null) {
                throw invalid(reader, resource, "unsupported or duplicate attribute: " + name);
            }
        }
        String name = required(attributes, "name", reader, resource);
        String catalog = required(attributes, "catalog", reader, resource);
        String schema = required(attributes, "schema", reader, resource);
        boolean skipPermission = trueOnly(attributes, "skipPermission", reader, resource);
        boolean aggregate = trueOnly(attributes, "aggregate", reader, resource);
        String environment = attributes.get("environment");
        if (environment != null && environment.isBlank()) {
            throw invalid(reader, resource, "environment must not be blank");
        }

        int event = reader.nextTag();
        if (event != XMLStreamConstants.START_ELEMENT || !"versions".equals(reader.getLocalName())) {
            throw invalid(reader, resource, "resource must contain one versions element");
        }
        requireNoAttributes(reader, resource);
        Set<String> versions = parseVersions(reader.getElementText(), reader, resource);
        event = reader.nextTag();
        if (event != XMLStreamConstants.END_ELEMENT || !group.element().equals(reader.getLocalName())) {
            throw invalid(reader, resource, "resource must end after versions");
        }
        return new DatabaseResource(group.type(), name, catalog, schema, versions, skipPermission, aggregate,
                environment);
    }

    private static Set<String> parseVersions(String text, XMLStreamReader reader, String resource) {
        if (text == null || text.isBlank()) {
            throw invalid(reader, resource, "versions must not be empty");
        }
        Set<String> versions = new LinkedHashSet<>();
        for (String item : text.strip().split("/", -1)) {
            String version = item.strip();
            if (version.startsWith("v") || version.startsWith("V")) {
                version = version.substring(1);
            }
            if (version.isBlank() || !versions.add(version)) {
                throw invalid(reader, resource, "invalid or duplicate version: " + item);
            }
        }
        return Collections.unmodifiableSet(versions);
    }

    private static String required(Map<String, String> attributes, String name, XMLStreamReader reader,
            String resource) {
        String value = attributes.get(name);
        if (value == null || value.isBlank()) {
            throw invalid(reader, resource, name + " must not be blank");
        }
        return value;
    }

    private static boolean trueOnly(Map<String, String> attributes, String name, XMLStreamReader reader,
            String resource) {
        String value = attributes.get(name);
        if (value == null) {
            return false;
        }
        if (!"true".equals(value)) {
            throw invalid(reader, resource, name + " may only be specified as true");
        }
        return true;
    }

    private static void moveToStart(XMLStreamReader reader, String resource) throws XMLStreamException {
        while (reader.hasNext() && reader.next() != XMLStreamConstants.START_ELEMENT) {
            // Move past the declaration and ignorable whitespace.
        }
        if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
            throw invalid(reader, resource, "missing document element");
        }
    }

    private static void requireNoAttributes(XMLStreamReader reader, String resource) {
        if (reader.getAttributeCount() != 0) {
            throw invalid(reader, resource, reader.getLocalName() + " does not accept attributes");
        }
    }

    private static void requireElement(XMLStreamReader reader, String expected, String resource) {
        if (!expected.equals(reader.getLocalName())) {
            throw invalid(reader, resource, "expected " + expected + " but found " + reader.getLocalName());
        }
    }

    private static IllegalStateException invalid(XMLStreamReader reader, String resource, String message) {
        return new IllegalStateException("Invalid database resource " + resource + " at line "
                                         + reader.getLocation().getLineNumber() + ": " + message);
    }

    private static void set(XMLInputFactory factory, String name, Object value) {
        try {
            factory.setProperty(name, value);
        } catch (IllegalArgumentException ignored) {
            // The JDK provider already disables unsupported external access mechanisms.
        }
    }

    private record Group(String element, RegisteredResourceType type) {
    }

    private static final class SchemaHolder {
        private static final Schema SCHEMA = loadSchema();
    }
}

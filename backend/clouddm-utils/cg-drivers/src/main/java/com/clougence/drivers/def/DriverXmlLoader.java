/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.drivers.def;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriverXmlLoader {

    static final String DRIVER_DEFINITION_FILE = "META-INF/clougence/drivers.xml";

    public List<FamilyDef> load(InputStream xmlInputStream) {
        if (xmlInputStream == null) {
            return Collections.emptyList();
        }

        try {
            return loadFromStream(xmlInputStream);
        } catch (IOException e) {
            log.warn("load driver definitions failed, resource={} msg={}", DRIVER_DEFINITION_FILE, e.getMessage());
            return Collections.emptyList();
        }
    }

    public String definitionLocation() {
        return DRIVER_DEFINITION_FILE;
    }

    private List<FamilyDef> loadFromStream(InputStream xmlStream) throws IOException {
        if (xmlStream == null) {
            return Collections.emptyList();
        }

        Document document = parseDocument(xmlStream);
        Element root = document.getDocumentElement();
        if (root == null) {
            return Collections.emptyList();
        }

        Map<String, FamilyDef> descriptions = new LinkedHashMap<>();
        for (Element driverElement : findDriverElements(root)) {
            FamilyDef description = toDriverDescription(driverElement);
            if (description != null) {
                String mergeKey = buildMergeKey(description.familyName);
                FamilyDef exists = descriptions.get(mergeKey);
                if (exists == null) {
                    descriptions.put(mergeKey, description);
                } else {
                    mergeDriver(exists, description);
                }
            }
        }
        return new ArrayList<>(descriptions.values());
    }

    private Document parseDocument(InputStream xmlStream) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlStream);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("parse drivers.xml failed", e);
        }
    }

    private List<Element> findDriverElements(Element root) {
        if (root == null) {
            return Collections.emptyList();
        }

        List<Element> driverElements = new ArrayList<>();
        for (Element child : childElements(root, "driver")) {
            driverElements.add(child);
        }
        return driverElements;
    }

    private FamilyDef toDriverDescription(Element driverElement) {
        if (driverElement == null) {
            return null;
        }

        List<ResDef> resources = parseResources(driverElement);
        String driverName = firstNotBlank(attributeOrChildValue(driverElement, "driverName"));
        String withFamily = firstNotBlank(attributeOrChildValue(driverElement, "driverFamily"));
        String withVersion = firstNotBlank(attributeOrChildValue(driverElement, "version"));

        boolean valid = StringUtils.isNotBlank(withFamily);
        valid = valid && StringUtils.isNotBlank(withVersion);
        if (!valid) {
            return null;
        }

        FamilyDef driverDescription = new FamilyDef();
        driverDescription.familyName = withFamily;
        VerDef driverVersion = driverDescription.addVersion(withVersion);
        driverVersion.setDsFactory(driverName);
        for (ResDef resource : resources) {
            if (resource != null) {
                driverVersion.addResource(resource);
            }
        }

        return driverDescription;
    }

    private String buildMergeKey(String withFamily) {
        return StringUtils.lowerCase(StringUtils.defaultString(withFamily));
    }

    private List<ResDef> parseResources(Element driverElement) {
        List<ResDef> resources = new ArrayList<>();

        for (Element resourceElement : childElements(driverElement, "resource")) {
            String coordinate = firstNotBlank(textValue(resourceElement));
            if (StringUtils.isBlank(coordinate)) {
                continue;
            }

            String type = firstNotBlank(attributeOrChildValue(resourceElement, "type"), inferResourceType(coordinate));
            ResDef resource = new ResDef();
            resource.setResourceType(type);
            resource.setCoordinate(coordinate);
            resources.add(resource);
        }

        return resources;
    }

    private String inferResourceType(String coordinate) {
        if (StringUtils.isBlank(coordinate)) {
            return "maven";
        }
        String trimmed = coordinate.trim();
        if (trimmed.startsWith("files(")) {
            return "file";
        }
        if (trimmed.startsWith("file:")) {
            return "file";
        }
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return "url";
        }
        return "maven";
    }

    private String firstNotBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value.trim();
            }
        }
        return null;
    }

    private String attributeOrChildValue(Element element, String name) {
        if (element == null || StringUtils.isBlank(name)) {
            return null;
        }

        String attributeValue = element.getAttribute(name);
        if (StringUtils.isNotBlank(attributeValue)) {
            return attributeValue;
        }
        for (Element child : childElements(element, name)) {
            String childText = textValue(child);
            if (StringUtils.isNotBlank(childText)) {
                return childText;
            }
        }
        return null;
    }

    private List<Element> childElements(Element parent, String tagName) {
        if (parent == null) {
            return Collections.emptyList();
        }

        List<Element> elements = new ArrayList<>();
        NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element childElement = (Element) child;
            if (tagName == null || tagName.equals(childElement.getTagName())) {
                elements.add(childElement);
            }
        }
        return elements;
    }

    private String textValue(Element element) {
        return element == null ? null : StringUtils.trimToNull(element.getTextContent());
    }

    private void mergeDriver(FamilyDef target, FamilyDef source) {
        if (target == null || source == null) {
            return;
        }

        for (VerDef sourceVersion : source.versions) {
            VerDef targetVersion = target.addVersion(sourceVersion.getVersion());
            mergeVersion(targetVersion, sourceVersion);
        }
    }

    private void mergeVersion(VerDef target, VerDef source) {
        if (target == null || source == null) {
            return;
        }

        target.setFamilyName(StringUtils.defaultIfBlank(target.getFamilyName(), source.getFamilyName()));
        if (StringUtils.isNotBlank(source.getDsFactory())) {
            target.setDsFactory(source.getDsFactory());
        }
        if (StringUtils.isNotBlank(source.getComment())) {
            target.setComment(source.getComment());
        }
        if (source.getLoader() != null) {
            target.setLoader(source.getLoader());
        }
        if (source.getLoaderMap() != null) {
            target.setLoaderMap(source.getLoaderMap());
        }
        if (source.getDsFactoryDef() != null) {
            target.setDsFactoryDef(source.getDsFactoryDef());
        }
        for (ResDef resource : source.getResources()) {
            target.addResource(resource);
        }
    }
}

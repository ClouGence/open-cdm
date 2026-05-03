package com.clougence.clouddm.console.web.component.detectrule.local;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.RuleTarget;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.codec.MD5;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public class SecRulesScriptUtils {

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    private static List<SecRuleScriptInfo>      innerRules;

    public static List<SecRuleScriptInfo> innerRules() throws IOException {
        if (innerRules == null) {
            innerRules = SecRulesScriptUtils.loadLocalData(ResourcesUtils.getResourceAsStream("/dm/rules/sec-rules.xml"));
            SecRulesScriptUtils.loadRuleApply(ResourcesUtils.getResourceAsStream("/dm/rules/sec-apply.xml"), innerRules);
        }
        return innerRules;
    }

    public static void loadRuleApply(InputStream stream, List<SecRuleScriptInfo> infos) throws IOException {
        Objects.requireNonNull(stream, "load InputStream is null.");
        try {
            if (stream == null) {
                throw new NullPointerException("stream is null.");
            }

            DocumentBuilder documentBuilder = FACTORY.newDocumentBuilder();
            documentBuilder.setEntityResolver((publicId, systemId) -> {
                if (systemId.startsWith("classpath://")) {
                    String localFile = systemId.substring("classpath://".length());
                    InputSource source = new InputSource(ResourcesUtils.getResourceAsStream(localFile));
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    return source;
                } else {
                    return new DefaultHandler().resolveEntity(publicId, systemId);
                }
            });

            Document document = documentBuilder.parse(new InputSource(stream));

            loadRuleApplyData(document.getDocumentElement(), infos);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    private static void loadRuleApplyData(Element root, List<SecRuleScriptInfo> infos) throws IOException {
        Map<String, SecRuleScriptInfo> infoMap = new HashMap<>();
        for (SecRuleScriptInfo info : infos) {
            infoMap.put(info.getRuleId(), info);
        }

        NodeList childNodes = root.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String elementName = node.getNodeName();
            if (StringUtils.equalsIgnoreCase("ruleDef", elementName)) {
                resolveRuleApply(node, infoMap);
            }
        }
    }

    private static void resolveRuleApply(Node refNode, Map<String, SecRuleScriptInfo> infoMap) {
        NamedNodeMap attr = refNode.getAttributes();

        String refID = strFromXmlAttribute(attr, "id");
        SecRuleScriptInfo target;
        if (!infoMap.containsKey(refID)) {
            return;
        } else {
            target = infoMap.get(refID);
        }

        NodeList childNodes = refNode.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (StringUtils.equalsIgnoreCase("applyTo", node.getNodeName())) {
                String applyTo = node.getTextContent();
                if (StringUtils.isNotBlank(applyTo)) {
                    for (String str : StringUtils.split(applyTo, ",")) {
                        target.getDsRange().add(DataSourceType.valueOf(str.trim()));
                    }
                }
            }
        }
    }

    public static List<SecRuleScriptInfo> loadLocalData(InputStream stream) throws IOException {
        Objects.requireNonNull(stream, "load InputStream is null.");
        try {
            if (stream == null) {
                throw new NullPointerException("stream is null.");
            }

            DocumentBuilder documentBuilder = FACTORY.newDocumentBuilder();
            documentBuilder.setEntityResolver((publicId, systemId) -> {
                if (systemId.startsWith("classpath://")) {
                    String localFile = systemId.substring("classpath://".length());
                    InputSource source = new InputSource(ResourcesUtils.getResourceAsStream(localFile));
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    return source;
                } else {
                    return new DefaultHandler().resolveEntity(publicId, systemId);
                }
            });

            Document document = documentBuilder.parse(new InputSource(stream));

            return loadExportData(document.getDocumentElement());
        } catch (NoSuchAlgorithmException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    private static List<SecRuleScriptInfo> loadExportData(Element root) throws NoSuchAlgorithmException {
        List<SecRuleScriptInfo> result = new ArrayList<>();

        NodeList childNodes = root.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String elementName = node.getNodeName();
            if (StringUtils.equalsIgnoreCase("queryRules", elementName)) {
                resolveRuleScriptSet(result, node, RuleKind.QUERY);
            } else if (StringUtils.equalsIgnoreCase("sensitiveRules", elementName)) {
                resolveRuleScriptSet(result, node, RuleKind.SENSITIVE);
            }
        }

        return result;
    }

    private static void resolveRuleScriptSet(List<SecRuleScriptInfo> result, Node ruleSet, RuleKind ruleKind) throws NoSuchAlgorithmException {
        NodeList childNodes = ruleSet.getChildNodes();
        for (int i = 0, len = childNodes.getLength(); i < len; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String elementName = node.getNodeName();
            if (StringUtils.equalsIgnoreCase("ruleDef", elementName)) {
                resolveRuleScript(result, node, ruleKind);
            }
        }
    }

    private static void resolveRuleScript(List<SecRuleScriptInfo> result, Node node, RuleKind ruleKind) throws NoSuchAlgorithmException {
        NamedNodeMap attr = node.getAttributes();

        SecRuleScriptInfo info = new SecRuleScriptInfo();
        info.setRuleId(strFromXmlAttribute(attr, "id"));
        info.setRuleName(strFromXmlAttribute(attr, "ruleName"));
        info.setRuleDesc(strFromXmlAttribute(attr, "ruleDesc"));
        info.setRuleKind(ruleKind);
        info.setScriptType(scriptTypeFromXmlAttribute(attr, "scriptType"));
        info.setOldId(numFromXmlAttribute(attr, "oldId"));
        info.setOldId(numFromXmlAttribute(attr, "oldId"));
        info.setDeprecated(boolFromXmlAttribute(attr, "deprecated"));

        if (ruleKind == RuleKind.QUERY) {
            info.setRuleTarget(ruleTargetFromXmlAttribute(attr, "ruleTarget"));
            info.setDsRange(new ArrayList<>());
            //            String dsRange = strFromXmlAttribute(attr, "dsRange");
            //            if (StringUtils.isNotBlank(dsRange)) {
            //                for (String str : StringUtils.split(dsRange, ",")) {
            //                    info.getDsRange().add(DataSourceType.valueOf(str));
            //                }
            //            }
        } else if (ruleKind == RuleKind.SENSITIVE) {
            info.setSenMode(sensitiveModeFromXmlAttribute(attr, "senMode"));
        }

        String content = node.getTextContent();
        if (StringUtils.isNotBlank(content)) {
            info.setContent(content.trim());
        } else {
            info.setContent("");
        }
        info.setContentMD5(MD5.getMD5(info.toString()));

        result.add(info);
    }

    private static String strFromXmlAttribute(NamedNodeMap nodeAttributes, String key) {
        Node node = nodeAttributes.getNamedItem(key);
        return (node != null) ? node.getNodeValue() : null;
    }

    private static Long numFromXmlAttribute(NamedNodeMap nodeAttributes, String key) {
        Node node = nodeAttributes.getNamedItem(key);
        return (node != null) ? Long.parseLong(node.getNodeValue()) : null;
    }

    private static boolean boolFromXmlAttribute(NamedNodeMap nodeAttributes, String key) {
        Node node = nodeAttributes.getNamedItem(key);
        return node != null && Boolean.parseBoolean(node.getNodeValue());
    }

    private static RuleScriptType scriptTypeFromXmlAttribute(NamedNodeMap attr, String key) {
        String typeStr = strFromXmlAttribute(attr, key);
        for (RuleScriptType scriptType : RuleScriptType.values()) {
            if (StringUtils.equalsIgnoreCase(scriptType.name(), typeStr)) {
                return scriptType;
            }
        }
        throw new IllegalArgumentException("missing scriptType in sec-rules.xml");
    }

    private static RuleTarget ruleTargetFromXmlAttribute(NamedNodeMap attr, String key) {
        String typeStr = strFromXmlAttribute(attr, key);
        if (StringUtils.isBlank(typeStr)) {
            return null;
        }
        for (RuleTarget ruleTarget : RuleTarget.values()) {
            if (StringUtils.equalsIgnoreCase(ruleTarget.name(), typeStr)) {
                return ruleTarget;
            }
        }
        throw new IllegalArgumentException("missing ruleTarget in sec-rules.xml");
    }

    private static RuleSensitiveMode sensitiveModeFromXmlAttribute(NamedNodeMap attr, String key) {
        String typeStr = strFromXmlAttribute(attr, key);
        for (RuleSensitiveMode sensitiveMode : RuleSensitiveMode.values()) {
            if (StringUtils.equalsIgnoreCase(sensitiveMode.name(), typeStr)) {
                return sensitiveMode;
            }
        }
        throw new IllegalArgumentException("missing senMode in sec-rules.xml");
    }
}

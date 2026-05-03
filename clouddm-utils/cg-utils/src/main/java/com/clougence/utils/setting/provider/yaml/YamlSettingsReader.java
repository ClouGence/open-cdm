package com.clougence.utils.setting.provider.yaml;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.clougence.utils.setting.SettingNode;
import com.clougence.utils.setting.Settings;
import com.clougence.utils.setting.data.TreeNode;
import com.clougence.utils.setting.provider.SettingsReader;

public class YamlSettingsReader implements SettingsReader {

    @Override
    public void readSetting(Reader reader, Settings readTo) throws IOException {
        final Object yamlObject;
        try {
            yamlObject = new Yaml().load(reader);
        } catch (Exception e) {
            throw new IOException("parsing failed -> " + e.getMessage(), e);
        }

        TreeNode root = new TreeNode("", Settings.DefaultNameSpace);
        bindNode(root, yamlObject);
        for (SettingNode node : root.getSubNodes()) {
            readTo.addSetting(node.getName(), node, Settings.DefaultNameSpace);
        }
    }

    private void bindNode(TreeNode parent, Object value) {
        if (value instanceof Map) {
            ((Map<?, ?>) value).forEach((key, item) -> {
                if (key != null) {
                    bindChild(parent, key.toString(), item);
                }
            });
            return;
        }
        if (value instanceof List) {
            for (Object item : (List<?>) value) {
                bindNode(parent, item);
            }
            return;
        }
        if (value != null) {
            parent.addValue(String.valueOf(value));
        }
    }

    private void bindChild(TreeNode parent, String name, Object value) {
        if (value instanceof Map) {
            TreeNode child = parent.newNode(name);
            bindNode(child, value);
            return;
        }
        if (value instanceof List) {
            List<?> items = (List<?>) value;
            if (items.isEmpty()) {
                parent.newNode(name);
                return;
            }

            boolean allScalar = items.stream().noneMatch(item -> item instanceof Map || item instanceof List);
            if (allScalar) {
                TreeNode child = parent.newNode(name);
                for (Object item : items) {
                    child.addValue(item == null ? null : String.valueOf(item));
                }
            } else {
                for (Object item : items) {
                    TreeNode child = parent.newNode(name);
                    bindNode(child, item);
                }
            }
            return;
        }

        TreeNode child = parent.newNode(name);
        if (value != null) {
            child.addValue(String.valueOf(value));
        }
    }
}
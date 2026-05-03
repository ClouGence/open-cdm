package com.clougence.clouddm.console.web.util;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.base.metadata.ui.form.value.*;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorFieldForm;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorPanelForm;
import com.clougence.clouddm.console.web.model.vo.faker.FakerDefVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerFieldVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerPanelVO;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.faker.FakerUiPanel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class UiWebUtil extends UiUtils {

    public static TableEditorForm tableEditorUi2Form(TableEditorUiPanel uiPanel) {
        if (uiPanel == null) {
            return null;
        }

        TableEditorForm panelVO = new TableEditorForm();
        panelVO.setOrder(new ArrayList<>());
        panelVO.setUiPanels(new LinkedHashMap<>());

        panelVO.getOrder().add("tableInfo");
        panelVO.getUiPanels().put("tableInfo", passerPanel(uiPanel.getTableInfo()));

        if (uiPanel.getColumns() != null && !uiPanel.getColumns().getChildren().isEmpty()) {
            panelVO.getOrder().add("columns");
            panelVO.getUiPanels().put("columns", passerPanel(uiPanel.getColumns()));
        }
        if (uiPanel.getKeys() != null && !uiPanel.getKeys().getChildren().isEmpty()) {
            panelVO.getOrder().add("keys");
            panelVO.getUiPanels().put("keys", passerPanel(uiPanel.getKeys()));
        }
        if (uiPanel.getIndexes() != null && !uiPanel.getIndexes().getChildren().isEmpty()) {
            panelVO.getOrder().add("indexes");
            panelVO.getUiPanels().put("indexes", passerPanel(uiPanel.getIndexes()));
        }
        if (uiPanel.getPartitions() != null && !uiPanel.getPartitions().getChildren().isEmpty()) {
            panelVO.getOrder().add("partitions");
            panelVO.getUiPanels().put("partitions", passerPanel(uiPanel.getPartitions()));
        }
        if (uiPanel.getConstraints() != null && !uiPanel.getConstraints().getChildren().isEmpty()) {
            panelVO.getOrder().add("constraints");
            panelVO.getUiPanels().put("constraints", passerPanel(uiPanel.getConstraints()));
        }

        if (uiPanel.getForeignKeys() != null && !uiPanel.getForeignKeys().getChildren().isEmpty()) {
            panelVO.getOrder().add("foreignKeys");
            panelVO.getUiPanels().put("foreignKeys", passerPanel(uiPanel.getForeignKeys()));
        }

        return panelVO;
    }

    public static TableEditorForm tableEditorUi2Form(PropertyUiPanel uiPanel) {
        if (uiPanel == null) {
            return null;
        }

        TableEditorForm panelVO = new TableEditorForm();
        panelVO.setOrder(new ArrayList<>());
        panelVO.setUiPanels(new LinkedHashMap<>());

        panelVO.getOrder().add("baseInfo");
        panelVO.getUiPanels().put("baseInfo", passerPanel(uiPanel.getBaseInfo()));

        return panelVO;
    }

    public static FakerDefVO fakerDefUi2VO(FakerUiPanel uiPanel) {
        if (uiPanel == null) {
            return null;
        }

        FakerDefVO panelVO = new FakerDefVO();
        panelVO.setUiPanels(new LinkedHashMap<>());

        panelVO.getUiPanels().put("globalConfig", passerFakerPanel(uiPanel.getGlobalConfig()));
        panelVO.getUiPanels().put("tableConfig", passerFakerPanel(uiPanel.getTableConfig()));
        if (uiPanel.getColumnConfig() != null && !uiPanel.getColumnConfig().getChildren().isEmpty()) {
            panelVO.getUiPanels().put("columnConfig", passerFakerPanel(uiPanel.getColumnConfig()));
        }

        return panelVO;
    }

    private static FakerPanelVO passerFakerPanel(UiPanel tableInfo) {
        FakerPanelVO fakerPanelVO = new FakerPanelVO();
        fakerPanelVO.setTitleI18N(tableInfo.getTitleI18N());
        fakerPanelVO.setDescI18N(tableInfo.getDescI18N());
        fakerPanelVO.setChildren(passerFakerChildren(tableInfo.getChildren()));
        return fakerPanelVO;
    }

    private static List<FakerFieldVO> passerFakerChildren(List<UiPanelField> children) {
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        List<FakerFieldVO> childrenVos = new ArrayList<>();
        for (UiPanelField uiField : children) {
            FakerFieldVO fieldVO = new FakerFieldVO();

            fieldVO.setField(uiField.getField());
            fieldVO.setType(uiField.getType() == null ? null : uiField.getType().getType());
            fieldVO.setRequire(uiField.isRequire());
            fieldVO.setReadOnly(uiField.isReadOnly());
            fieldVO.setHide(uiField.isHide());
            fieldVO.setDefaultVal(passerPanel(uiField.getDefaultValue()));
            if (uiField.getOptions() != null) {
                fieldVO.setOptions(uiField.getOptions().stream().map(UiWebUtil::passerPanel).collect(Collectors.toList()));
            } else {
                fieldVO.setOptions(Collections.emptyList());
            }
            fieldVO.setTitleI18N(uiField.getTitleI18N());
            fieldVO.setDescI18N(uiField.getDescI18N());
            fieldVO.setChildren(passerFakerChildren(uiField.getChildren()));

            childrenVos.add(fieldVO);
        }
        return childrenVos;
    }

    public static TableEditorPanelForm passerPanel(UiPanel tableInfo) {
        TableEditorPanelForm panelForm = new TableEditorPanelForm();
        panelForm.setTitleI18N(tableInfo.getTitleI18N());
        panelForm.setDescI18N(tableInfo.getDescI18N());
        panelForm.setChildren(passerChildren(tableInfo.getChildren()));
        return panelForm;
    }

    private static List<TableEditorFieldForm> passerChildren(List<UiPanelField> children) {
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        List<TableEditorFieldForm> childrenForms = new ArrayList<>();
        for (UiPanelField uiField : children) {
            TableEditorFieldForm fieldForm = new TableEditorFieldForm();

            fieldForm.setField(uiField.getField());
            fieldForm.setType(uiField.getType().getType());
            fieldForm.setTypeConfig(uiField.getTypeConfig());
            fieldForm.setRequire(uiField.isRequire());
            fieldForm.setReadOnly(uiField.isReadOnly());
            fieldForm.setHide(uiField.isHide());
            fieldForm.setDefaultVal(passerPanel(uiField.getDefaultValue()));
            if (uiField.getOptions() != null) {
                fieldForm.setOptions(uiField.getOptions().stream().map(UiWebUtil::passerPanel).collect(Collectors.toList()));
            } else {
                fieldForm.setOptions(Collections.emptyList());
            }
            fieldForm.setTitleI18N(uiField.getTitleI18N());
            fieldForm.setDescI18N(uiField.getDescI18N());
            fieldForm.setChildren(passerChildren(uiField.getChildren()));

            childrenForms.add(fieldForm);
        }
        return childrenForms;
    }

    private static Object passerPanel(ValueDef valueDef) {
        if (valueDef == null) {
            return null;
        }
        if (valueDef instanceof ObjectValueDef) {
            return ((ObjectValueDef) valueDef).getValue();
        }
        if (valueDef instanceof MapValueDef) {
            return ((MapValueDef) valueDef).getData();
        }
        if (valueDef instanceof OptionValueDef) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("value", ((OptionValueDef) valueDef).getValue());
            value.put("label", ((OptionValueDef) valueDef).getLabelI18N());
            return value;
        }
        //        if (valueDef instanceof FieldValueDef) {
        //            Map<String, Object> value = new LinkedHashMap<>();
        //            value.put("children", passerChildren(((FieldValueDef) valueDef).getChildren()));
        //            return value;
        //        }
        if (valueDef instanceof FieldOptionValueDef) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("value", ((FieldOptionValueDef) valueDef).getValue());
            value.put("label", ((FieldOptionValueDef) valueDef).getLabelI18N());
            value.put("desc", ((FieldOptionValueDef) valueDef).getDescI18N());
            value.put("children", passerChildren(((FieldOptionValueDef) valueDef).getChildren()));
            value.put("readOnly", ((FieldOptionValueDef) valueDef).isReadOnly());
            return value;
        }
        throw new UnsupportedOperationException("Unsupported valueDef type of '" + valueDef.getClass().getName() + "'");
    }

}

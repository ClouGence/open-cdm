//package com.clougence.reactor.exporter;
//
//import com.clougence.adapter.kingbasees.domain.KingbaseESTypes;
//import com.clougence.schema.DsType;
//import com.clougence.schema.editor.TableEditor;
//import com.clougence.schema.editor.domain.EColumn;
//import com.clougence.schema.editor.triggers.TriggerContext;
//import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
//import com.clougence.schema.handlers.TableBeforeMappingHandler;
//import com.clougence.schema.metadata.MainVersion;
//import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
//
///**
// * @author chunlin create time is 2025/11/25
// */
//public class KingbaseESTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {
//
//    @Override
//    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, //
//                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
//
//    }
//
//    @Override
//    public void beforeMapping(TableEditor tableEditor, TableEditor.ColumnEditor columnEditor, TriggerContext triggerContext, //
//                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
//        // special type
//        processSpecialType(columnEditor, dstType);
//    }
//
//    private void processSpecialType(TableEditor.ColumnEditor columnEditor, DsType dstType) {
//        EColumn eColumn = columnEditor.getSource();
//        if (eColumn.getDbType() == null) {
//            return;
//        }
//
//        KingbaseESTypes pgType = KingbaseESTypes.valueOfCode(eColumn.getDbType());
//        if (pgType == KingbaseESTypes.UUID || pgType == KingbaseESTypes.UUID_ARRAY) {
//            eColumn.setLength(36L);
//        }
//
//        if (pgType == KingbaseESTypes.NUMERIC && eColumn.getNumericPrecision() == null && eColumn.getNumericScale() == null) {
//            columnEditor.setNumberLength(64, 16);
//            columnEditor.addAttr(RdbAttributeNames.NUMBER_NO_SCALE_PREC.getCodeKey(), "true");
//        }
//
//        if (pgType == KingbaseESTypes.MONEY) {
//            columnEditor.setNumberLength(22, 2);
//        }
//    }
//}

//package com.clougence.reactor.exporter;
//
//import java.sql.JDBCType;
//
//import com.clougence.schema.DsType;
//import com.clougence.schema.editor.TableEditor;
//import com.clougence.schema.editor.TableEditor.ColumnEditor;
//import com.clougence.schema.editor.domain.EColumn;
//import com.clougence.schema.editor.triggers.TriggerContext;
//import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
//import com.clougence.schema.metadata.MainVersion;
//
//public class HiveTableExporter implements ColumnBeforeMappingHandler {
//
//    @Override
//    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
//                              DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
//
//        // decimal upgrade
//        decimalUpgrade(columnEditor);
//    }
//
//    private void decimalUpgrade(ColumnEditor columnEditor) {
//        EColumn ecolumn = columnEditor.getSource();
//        if (ecolumn.getDbType().equals(JDBCType.DECIMAL.getName())) {
//            ecolumn.setNumericPrecision(ecolumn.getNumericPrecision() + 1);
//        }
//    }
//
//}

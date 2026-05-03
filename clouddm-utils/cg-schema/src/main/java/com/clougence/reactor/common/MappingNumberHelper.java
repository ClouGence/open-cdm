package com.clougence.reactor.common;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author bucketli 2024/7/9 14:10:43
 */
public class MappingNumberHelper {

    public static void myFamilyFloatDoubleCompletion(TableEditor.ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        if (srcType == dstType) {
            return;
        }

        String type = columnEditor.getSource().getDbType();
        if (type == null) {
            return;
        }

        if (JDBCType.FLOAT.getName().equals(type.toUpperCase())) {
            Integer numericPrecision = columnEditor.getSource().getNumericPrecision();
            if (numericPrecision == null) {
                numericPrecision = 8;
            }

            columnEditor.setNumberLength(numericPrecision + 8, 8);
        } else if (JDBCType.DOUBLE.getName().equals(type.toUpperCase())) {
            Integer numericPrecision = columnEditor.getSource().getNumericPrecision();
            if (numericPrecision == null) {
                numericPrecision = 16;
            }

            columnEditor.setNumberLength(numericPrecision + 16, 16);
        }
    }
}

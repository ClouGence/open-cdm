package com.clougence.schema.handlers;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/5/21 19:56
 */
public interface ColumnAfterMappingHandler extends Handler {

    void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                      DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion);

}

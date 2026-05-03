package com.clougence.clouddm.console.web.service.browse;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.fo.object.ObjectEditorDefFO;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorFieldForm;
import com.clougence.clouddm.console.web.service.browse.model.ActionTargetMO;
import com.clougence.schema.umi.struts.UmiTypes;

public interface ActionService {

    /** for service API '/browse/genAction' */
    List<String> genAction(DsLevels levels, ActionTargetMO mo);

    /** for service API '/browse/doAction' */
    List<String> doAction(String puid, String uid, DsLevels levels, ActionTargetMO mo, String clientIp);

    /** for service API '/browse/actions/requestScript' */
    String requestObjectScript(String uid, DsLevels levels, ActionTargetMO mo);

    /** for service API '/browse/actions/generateScript' */
    String generateObjectScript(String uid, DsLevels levels, ActionTargetMO mo);

    /** for service API '/browse/actions/instanceRemark' */
    void instanceRemarks(String puid, String uid, DsLevels levels, String newRemark);

    /** for service API '/browse/actions/instanceDelete' */
    void instanceDelete(String puid, String uid, DsLevels levels);

    /** for service API '/browse/actions/convertDDL' */
    List<String> convertDDL(String puid, String uid, DsLevels levels, ActionTargetMO mo, DataSourceType dstType);

    /** for service API '/browse/actions/loadObjectTemplate' */
    Map<String, Object> loadObject(String puid, String uid, List<String> levels, UmiTypes leafType, String leafName);

    List<TableEditorFieldForm> loadObjectEditorDef(String puid, String uid, DsLevels levels, ObjectEditorDefFO defFO);
}

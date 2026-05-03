package com.clougence.schema.umi.struts;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.FUNC_RETURN_TYPE;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.OBJ_UI_TIPS;

import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.utils.StringUtils;

/**
 * 
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public class UmiAttributeUtils {

    public static void appendProcParamToDesc(RdbValue procedure, String pName, String pMode, String pType) {
        String descStr = procedure.getAttribute(OBJ_UI_TIPS);
        if (StringUtils.isNotBlank(descStr)) {
            descStr = descStr.substring(1, descStr.length() - 1);
            procedure.setAttribute(OBJ_UI_TIPS, "(" + descStr + ", " + pMode + " " + pType + ")");
        } else {
            procedure.setAttribute(OBJ_UI_TIPS, "(" + pMode + " " + pType + ")");
        }
    }

    public static void appendFuncParamToDesc(RdbValue func, String pName, String pMode, String pType) {
        if (StringUtils.equals(pMode, "OUT")) {
            String returnType = func.getAttribute(FUNC_RETURN_TYPE);
            if (StringUtils.isBlank(returnType)) {
                func.setAttribute(FUNC_RETURN_TYPE, pType);
            } else {
                if (returnType.startsWith("table(")) {
                    returnType = returnType.substring("table(".length(), returnType.length() - 1);
                }
                func.setAttribute(FUNC_RETURN_TYPE, "table(" + returnType + ", " + pType + ")");
            }
        }

        String descStr = func.getAttribute(OBJ_UI_TIPS);
        String returnStr = func.getAttribute(FUNC_RETURN_TYPE);
        if (StringUtils.isBlank(descStr)) {
            if (StringUtils.equals(pMode, "P") || StringUtils.equals(pMode, "IN")) {
                func.setAttribute(OBJ_UI_TIPS, "(" + descStr + "):" + returnStr);
            } else {
                func.setAttribute(OBJ_UI_TIPS, "():" + returnStr);
            }
        } else {
            descStr = descStr.substring(1, descStr.indexOf("):"));
            if (StringUtils.equals(pMode, "P") || StringUtils.equals(pMode, "IN")) {
                if (StringUtils.isBlank(descStr)) {
                    func.setAttribute(OBJ_UI_TIPS, "(" + pType + "): " + returnStr);
                } else {
                    func.setAttribute(OBJ_UI_TIPS, "(" + descStr + ", " + pType + "): " + returnStr);
                }
            } else {
                func.setAttribute(OBJ_UI_TIPS, "(" + descStr + "):" + returnStr);
            }
        }
    }

    public static void appendTriggerDesc(RdbValue trigger, String targetTab, boolean eventInsert, boolean eventUpdate, boolean eventDelete, boolean eventOther) {
        StringBuilder strBuild = new StringBuilder(" → ");
        strBuild.append(targetTab);
        strBuild.append("(");
        int eventCnt = 0;
        if (eventInsert) {
            strBuild.append(eventCnt > 0 ? ",INSERT" : "INSERT");
            eventCnt++;
        }
        if (eventUpdate) {
            strBuild.append(eventCnt > 0 ? ",UPDATE" : "UPDATE");
            eventCnt++;
        }
        if (eventDelete) {
            strBuild.append(eventCnt > 0 ? ",DELETE" : "DELETE");
            eventCnt++;
        }
        if (eventOther) {
            strBuild.append(eventCnt > 0 ? ",OTHER" : "OTHER");
            eventCnt++;
        }
        strBuild.append(")");

        trigger.setAttribute(OBJ_UI_TIPS, strBuild.toString());
    }

    public static void appendSequenceDesc(RdbValue seq, String minValue, String maxValue, String startValue, boolean cycle) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("(");
        strBuild.append(minValue);
        strBuild.append(" TO ");
        strBuild.append(maxValue);
        strBuild.append(", ");
        strBuild.append(startValue);
        strBuild.append(")");

        if (cycle) {
            strBuild.append(" CYCLE");
        }

        seq.setAttribute(OBJ_UI_TIPS, strBuild.toString());
    }

}

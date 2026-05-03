package com.clougence.clouddm.team.provider.dingtalk.client;

import java.util.ArrayList;
import java.util.List;

import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastRequest.ProcessForecastRequestFormComponentValues;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingConstant;
import com.clougence.clouddm.team.provider.dingtalk.domain.field.AuthApprovalTableField;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

class DingApiUtils {

    private static String safeLength(String str, int length, boolean inline) {
        if (StringUtils.isBlank(str)) {
            return "";
        } else if (str.length() > length) {
            if (inline) {
                return str.substring(0, length) + " ...";
            } else {
                return str.substring(0, length) + "\n\n......";
            }
        } else {
            return str;
        }
    }

    public static List<StartProcessInstanceRequestFormComponentValues> getQueryFormParams(QueryForm form) {
        List<StartProcessInstanceRequestFormComponentValues> params = new ArrayList<>();
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_SQL_TARGET).setValue(safeLength(form.getTargetDs(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_SQL_EXECUTE_SQL).setValue(safeLength(form.getExecuteSql(), 4000, false)));
        if (form.getRollBackSql() != null) {
            params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_SQL_ROLLBACK_SQL).setValue(safeLength(form.getRollBackSql(), 4000, false)));
        }
        if (form.getAffectCount() != null) {
            params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_SQL_AFFECT_LINE).setValue(String.valueOf(form.getAffectCount())));
        }
        return params;
    }

    public static List<StartProcessInstanceRequestFormComponentValues> getAuthFormParam(AuthForm form) {
        List<StartProcessInstanceRequestFormComponentValues> params = new ArrayList<>();
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));

        ArrayList<List<AuthApprovalTableField>> lists = new ArrayList<>();

        for (AuthForm.ApplyAuth applyAuth : form.getApplyAuths()) {
            lists.add(convertToRowData(applyAuth));
        }

        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_AUTH_TABLE).setValue(JsonUtils.toJson(lists)));
        return params;
    }

    public static List<StartProcessInstanceRequestFormComponentValues> getChangeFormParam(ChangeForm form) {
        List<StartProcessInstanceRequestFormComponentValues> params = new ArrayList<>();
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_TARGET).setValue(safeLength(form.getTargetDs(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_PROJECT).setValue(safeLength(form.getProjectName(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_NAME).setValue(safeLength(form.getChangeName(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_BRANCH).setValue(safeLength(form.getBranch(), 400, true)));
        params.add(new StartProcessInstanceRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_EXECUTE_SQL).setValue(safeLength(form.getExecuteSql(), 4000, false)));
        return params;
    }

    public static List<ProcessForecastRequestFormComponentValues> getQueryActivityParams(QueryForm form) {
        List<ProcessForecastRequestFormComponentValues> params = new ArrayList<>();
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_SQL_TARGET).setValue(safeLength(form.getTargetDs(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_SQL_EXECUTE_SQL).setValue(safeLength(form.getExecuteSql(), 4000, false)));
        if (form.getRollBackSql() != null) {
            params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_SQL_ROLLBACK_SQL).setValue(safeLength(form.getRollBackSql(), 4000, false)));
        }
        if (form.getAffectCount() != null) {
            params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_SQL_AFFECT_LINE).setValue(String.valueOf(form.getAffectCount())));
        }
        return params;
    }

    public static List<ProcessForecastRequestFormComponentValues> getAuthActivityParam(AuthForm form) {
        List<ProcessForecastRequestFormComponentValues> params = new ArrayList<>();
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));

        ArrayList<List<AuthApprovalTableField>> lists = new ArrayList<>();

        for (AuthForm.ApplyAuth applyAuth : form.getApplyAuths()) {
            lists.add(convertToRowData(applyAuth));
        }

        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_AUTH_TABLE).setValue(JsonUtils.toJson(lists)));
        return params;
    }

    public static List<ProcessForecastRequestFormComponentValues> getChangeActivityParam(ChangeForm form) {
        List<ProcessForecastRequestFormComponentValues> params = new ArrayList<>();
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_TITLE).setValue(safeLength(form.getTicketTitle(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_DESCRIPTION).setValue(safeLength(form.getTicketDesc(), 4000, false)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_TARGET).setValue(safeLength(form.getTargetDs(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_PROJECT).setValue(safeLength(form.getProjectName(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_NAME).setValue(safeLength(form.getChangeName(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_BRANCH).setValue(safeLength(form.getBranch(), 400, true)));
        params.add(new ProcessForecastRequestFormComponentValues().setName(DingConstant.FORM_CHANGE_EXECUTE_SQL).setValue(safeLength(form.getExecuteSql(), 4000, false)));
        return params;
    }

    //

    private static List<AuthApprovalTableField> convertToRowData(AuthForm.ApplyAuth applyAuth) {
        List<AuthApprovalTableField> result = new ArrayList<>();

        AuthApprovalTableField desc = new AuthApprovalTableField();
        desc.setName(DingConstant.FORM_AUTH_TABLE_RES_DESC);
        desc.setValue(safeLength(applyAuth.getResInstId() + "(" + applyAuth.getResDesc() + ")", 400, true));
        result.add(desc);

        AuthApprovalTableField resPath = new AuthApprovalTableField();
        resPath.setName(DingConstant.FORM_AUTH_TABLE_PATH);
        resPath.setValue(safeLength("/" + String.join("/", applyAuth.getResPaths()), 400, true));
        result.add(resPath);

        AuthApprovalTableField time = new AuthApprovalTableField();
        time.setName(DingConstant.FORM_AUTH_TABLE_TIME);
        if (applyAuth.getStartTime() == null && applyAuth.getEndTime() == null) {
            time.setValue("永久");
        } else if (applyAuth.getStartTime() != null && applyAuth.getEndTime() != null) {
            time.setValue(safeLength(applyAuth.getStartTime() + " - " + applyAuth.getEndTime(), 400, true));
        } else if (applyAuth.getStartTime() != null) {
            time.setValue(safeLength("从 " + applyAuth.getStartTime() + " 开始 至 永久", 400, true));
        } else {
            time.setValue(safeLength("从审批通过到 " + applyAuth.getEndTime() + " 结束", 400, true));
        }
        result.add(time);

        AuthApprovalTableField auth = new AuthApprovalTableField();
        auth.setName(DingConstant.FORM_AUTH_TABLE_AUTH);
        auth.setValue(safeLength(String.join(",\n", applyAuth.getAuthLabels()), 4000, false));
        result.add(auth);

        return result;
    }
}

package com.clougence.clouddm.team.provider.feishu.approval;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.clougence.clouddm.team.provider.feishu.constants.approval.FeishuConstant;
import com.clougence.clouddm.team.provider.feishu.domain.mo.FeishuWidget;
import com.clougence.clouddm.sdk.approval.ApprovalForm;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class FeishuApiUtils {

    public static String getFormParams(List<FeishuWidget> widgets, ApprovalForm info) {
        if (info instanceof QueryForm) {
            return getQueryFormParams(widgets, (QueryForm) info);
        } else if (info instanceof AuthForm) {
            return getAuthFormParams(widgets, (AuthForm) info);
        } else if (info instanceof ChangeForm) {
            return getChangeFormParam(widgets, (ChangeForm) info);
        } else {
            String message = String.format("Unsupported approval form type %s", info.getClass().getName());
            throw ThirdPartyApiException.asRDP().with(ThirdPartyApiErrorType.OTHER, FeishuI18nKeys2.FEISHU_UNKNOWN_CALL_API_ERROR, message);
        }
    }

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

    private static String getQueryFormParams(List<FeishuWidget> widgets, QueryForm info) {
        for (FeishuWidget widget : widgets) {
            switch (widget.getName()) {
                case FeishuConstant.FORM_TITLE: {
                    widget.setValue(safeLength(info.getTicketTitle(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_SQL_TARGET: {
                    widget.setValue(safeLength(info.getTargetDs(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_SQL_DESCRIPTION: {
                    widget.setValue(safeLength(info.getTicketDesc(), 4000, false));
                    break;
                }
                case FeishuConstant.FORM_SQL_EXECUTE_SQL: {
                    widget.setValue(safeLength(info.getExecuteSql(), 4000, false));
                    break;
                }
                case FeishuConstant.FORM_SQL_ROLLBACK_SQL: {
                    widget.setValue(safeLength(info.getRollBackSql(), 4000, false));
                    break;
                }
                case FeishuConstant.FORM_SQL_AFFECT_LINE: {
                    if (info.getAffectCount() != null) {
                        widget.setValue(info.getAffectCount().toString());
                    }
                    break;
                }
            }
        }
        return JsonUtils.toJson(widgets);
    }

    private static String getAuthFormParams(List<FeishuWidget> widgets, AuthForm info) {
        for (FeishuWidget widget : widgets) {
            switch (widget.getName()) {
                case FeishuConstant.FORM_TITLE: {
                    widget.setValue(safeLength(info.getTicketTitle(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_SQL_DESCRIPTION: {
                    widget.setValue(safeLength(info.getTicketDesc(), 4000, false));
                    break;
                }
                case FeishuConstant.FORM_AUTH_TABLE: {
                    List<List<FeishuWidget>> table = new ArrayList<>();
                    for (AuthForm.ApplyAuth applyAuth : info.getApplyAuths()) {
                        List<FeishuWidget> rowData = new ArrayList<>();
                        for (FeishuWidget child : widget.getChildren()) {
                            switch (child.getName()) {
                                case FeishuConstant.FORM_AUTH_TABLE_RES_DESC: {
                                    child.setValue(safeLength(applyAuth.getResInstId() + "(" + applyAuth.getResDesc() + ")", 400, true));
                                    break;
                                }
                                case FeishuConstant.FORM_AUTH_TABLE_PATH: {
                                    child.setValue(safeLength("/" + String.join("/", applyAuth.getResPaths()), 400, true));
                                    break;
                                }
                                case FeishuConstant.FORM_AUTH_TABLE_TIME: {
                                    String time;
                                    if (applyAuth.getStartTime() == null && applyAuth.getEndTime() == null) {
                                        time = ("永久");
                                    } else if (applyAuth.getStartTime() != null && applyAuth.getEndTime() != null) {
                                        time = applyAuth.getStartTime() + " - " + applyAuth.getEndTime();
                                    } else if (applyAuth.getStartTime() != null) {
                                        time = ("从 " + applyAuth.getStartTime() + " 开始 至 永久");
                                    } else {
                                        time = ("从审批通过到 " + applyAuth.getEndTime() + " 结束");
                                    }
                                    child.setValue(safeLength(time, 400, true));
                                    break;
                                }
                                case FeishuConstant.FORM_AUTH_TABLE_AUTH: {
                                    child.setValue(safeLength(String.join(",\n", applyAuth.getAuthLabels()), 4000, false));
                                    break;
                                }
                            }
                            rowData.add(child);
                        }
                        table.add(rowData);
                    }

                    widget.setValue(table);
                    break;
                }
            }
        }
        return JsonUtils.toJson(widgets);
    }

    private static String getChangeFormParam(List<FeishuWidget> widgets, ChangeForm info) {
        for (FeishuWidget widget : widgets) {
            switch (widget.getName()) {
                case FeishuConstant.FORM_TITLE: {
                    widget.setValue(safeLength(info.getTicketTitle(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_DESCRIPTION: {
                    widget.setValue(safeLength(info.getTicketDesc(), 4000, false));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_TARGET: {
                    widget.setValue(safeLength(info.getTargetDs(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_PROJECT: {
                    widget.setValue(safeLength(info.getProjectName(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_NAME: {
                    widget.setValue(safeLength(info.getChangeName(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_BRANCH: {
                    widget.setValue(safeLength(info.getBranch(), 400, true));
                    break;
                }
                case FeishuConstant.FORM_CHANGE_EXECUTE_SQL: {
                    widget.setValue(safeLength(info.getExecuteSql(), 4000, false));
                    break;
                }
            }
        }

        return JsonUtils.toJson(widgets);
    }

}

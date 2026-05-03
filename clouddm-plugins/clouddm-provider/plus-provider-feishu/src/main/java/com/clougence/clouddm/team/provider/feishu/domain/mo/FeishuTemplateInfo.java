package com.clougence.clouddm.team.provider.feishu.domain.mo;

import java.util.List;

import com.lark.oapi.service.approval.v4.model.ApprovalNodeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeishuTemplateInfo {

    List<FeishuWidget>     widgets;

    List<ApprovalNodeInfo> nodeInfoList;
}

package com.clougence.rdp.controller.model.vo;

import java.util.List;

import lombok.Data;

@Data
public class OpAuditConditionVO {

    private List<ResourceTypeVO> resourceTypeVOS;

    private List<AuditTypeVO>    auditTypeVOS;

}

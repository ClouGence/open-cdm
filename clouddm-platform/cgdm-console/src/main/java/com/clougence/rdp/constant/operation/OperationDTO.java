package com.clougence.rdp.constant.operation;

import lombok.Data;

@Data
public class OperationDTO {

    private AuditType type;

    private String    key;

    private String    resId;
}

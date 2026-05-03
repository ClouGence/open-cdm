package com.clougence.clouddm.sdk.approval.form;

import com.clougence.clouddm.sdk.approval.ApprovalForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryForm extends ApprovalForm {

    private String targetDs;

    private String executeSql;
    private String rollBackSql;
    private Long   affectCount;
}

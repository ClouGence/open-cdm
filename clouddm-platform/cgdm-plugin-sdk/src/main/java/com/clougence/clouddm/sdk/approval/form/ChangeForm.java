package com.clougence.clouddm.sdk.approval.form;

import com.clougence.clouddm.sdk.approval.ApprovalForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeForm extends ApprovalForm {

    private String targetDs;
    private String projectName;
    private String changeName;
    private String branch;
    private String executeSql;
}

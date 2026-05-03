package com.clougence.clouddm.sdk.approval.form;

import java.util.List;

import com.clougence.clouddm.sdk.approval.ApprovalForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthForm extends ApprovalForm {

    private List<ApplyAuth> applyAuths;

    @Getter
    @Setter
    public static class ApplyAuth {

        private List<String> resPaths;

        private List<String> authLabels;

        private String       startTime;

        private String       endTime;

        private String       resInstId;

        private String       resDesc;
    }

}

package com.clougence.clouddm.console.web.component.approval.model;

import java.util.Map;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalConnectionTestFO {
    @NotNull
    private ApprovalProvider    provider;
    @NotNull
    private Map<String, String> configs;
}

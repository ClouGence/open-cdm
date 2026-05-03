package com.clougence.clouddm.sdk.approval;

public enum ApprovalInstanceStatus {

    RUNNING,
    TERMINATED,
    COMPLETED,
    CANCELED,
    REFUSE,
    FAILED;

    public static ApprovalInstanceStatus valueOfIgnoreCase(String tarFormat) {
        if (tarFormat == null || tarFormat.trim().equals("")) {
            return null;
        }

        for (ApprovalInstanceStatus format : ApprovalInstanceStatus.values()) {
            if (format.name().equalsIgnoreCase(tarFormat.trim())) {
                return format;
            }
        }
        return null;
    }
}

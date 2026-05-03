package com.clougence.clouddm.sdk.service.approval;

public enum RdpApprovalActivityStatus {

    NEW,
    RUNNING,
    CANCELED,
    COMPLETED,
    REFUSE,
    CLOSE; // or approval if one task agree the activity another task will close, will delete task from context

    public static RdpApprovalActivityStatus valueOfIgnoreCase(String tarFormat) {
        if (tarFormat == null || tarFormat.trim().equals("")) {
            return null;
        }

        for (RdpApprovalActivityStatus format : RdpApprovalActivityStatus.values()) {
            if (format.name().equalsIgnoreCase(tarFormat.trim())) {
                return format;
            }
        }
        return null;
    }

    public static boolean canUpdate(RdpApprovalActivityStatus oldStatus, RdpApprovalActivityStatus newStatus) {
        if (oldStatus == NEW) {
            return true;
        }
        return oldStatus == RUNNING && newStatus != NEW;
    }
}

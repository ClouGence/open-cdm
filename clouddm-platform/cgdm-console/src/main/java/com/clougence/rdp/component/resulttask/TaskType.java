package com.clougence.rdp.component.resulttask;

public enum TaskType {

    APPROVAL_LAST_STATUS,;

    public static String getKey(TaskType taskType, Object id) {
        return taskType.name() + "-" + id.toString();
    }
}

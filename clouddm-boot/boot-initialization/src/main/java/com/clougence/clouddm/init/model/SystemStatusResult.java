package com.clougence.clouddm.init.model;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.console.web.constants.SystemStatus;

/**
 * 系统状态检测结果。
 */
public class SystemStatusResult {

    private SystemStatus status;
    private String       initReason;
    private String       dbError;
    private List<String> upgradeScripts = new ArrayList<>();

    public SystemStatus getStatus() { return status; }

    public void setStatus(SystemStatus status) { this.status = status; }

    public String getInitReason() { return initReason; }

    public void setInitReason(String initReason) { this.initReason = initReason; }

    public String getDbError() { return dbError; }

    public void setDbError(String dbError) { this.dbError = dbError; }

    public List<String> getUpgradeScripts() { return upgradeScripts; }

    public void setUpgradeScripts(List<String> upgradeScripts) { this.upgradeScripts = upgradeScripts == null ? new ArrayList<>() : new ArrayList<>(upgradeScripts); }
}

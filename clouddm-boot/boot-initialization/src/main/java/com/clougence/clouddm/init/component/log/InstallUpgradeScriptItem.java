package com.clougence.clouddm.init.component.log;

public record InstallUpgradeScriptItem(String scriptName, String status, String failedSql, String errorDetail) {
}
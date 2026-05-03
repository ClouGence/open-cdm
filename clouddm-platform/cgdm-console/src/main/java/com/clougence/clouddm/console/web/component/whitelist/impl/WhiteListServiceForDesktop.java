package com.clougence.clouddm.console.web.component.whitelist.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteListServiceForDesktop implements WhiteListService, DsFeatureIDs, UnifiedPostConstruct {

    @Override
    public void init() throws Exception {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean checkMenuQuery(String menuId) {
        return true;
    }

    @Override
    public boolean checkMenuManager(String menuId) {
        return true;
    }

    @Override
    public boolean checkMenuMaintenance(String menuId) {
        return true;
    }

    @Override
    public boolean checkDs(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeCatalog(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeSchema(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeIsolation(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeAutoCommit(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeReadOnly(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkCancelQuery(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkExplain(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkFormat(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkArgs(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkUserConfigNumber(String configKey, String configValue) {
        return true;
    }
}

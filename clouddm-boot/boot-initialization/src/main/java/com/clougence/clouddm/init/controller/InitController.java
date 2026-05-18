/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.init.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.clouddm.init.model.TestDbResult;
import com.clougence.clouddm.init.service.SysInitDefService;
import com.clougence.clouddm.init.service.InitMysqlDriverService;
import com.clougence.clouddm.init.service.SysInitService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;

import jakarta.annotation.Resource;

/**
 * REST API for system initialization.
 * All endpoints are exposed while the system is in the Initial state and therefore bypass authentication.
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/init")
public class InitController {

    @Resource
    private SysInitService    initService;
    @Resource
    private SysInitDefService defService;
    @Resource
    private InitMysqlDriverService initMysqlDriverService;

    /**
     * Returns the default configuration field definitions.
     * The schema comes from init-fields.json and default values come from classpath property files.
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/defaultConfig", method = { RequestMethod.POST })
    public ResWebData<?> defaultConfig() {
        List<InitFieldDef> fields = this.defService.loadInitFieldDefs();
        return ResWebDataUtils.buildSuccess(fields);
    }

    /**
     * Tests database connectivity and determines whether the target database is empty or already initialized.
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/testDb", method = { RequestMethod.POST })
    public ResWebData<?> testDb(@RequestBody Map<String, String> params) {
        String jdbcUrl = params.get("spring.datasource.jdbcurl");
        String username = params.get("spring.datasource.username");
        String password = params.get("spring.datasource.password");
        String rebuildIfNotEmpty = params.get("clougence.init.db.rebuildIfNotEmpty");
        String confirmDatabaseName = params.get("clougence.init.db.confirmDatabaseName");
        TestDbResult result = initService.testDbConnection(jdbcUrl, username, password, rebuildIfNotEmpty, confirmDatabaseName);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/checkMysqlDriverStatus", method = { RequestMethod.POST })
    public ResWebData<?> checkMysqlDriverStatus() {
        return ResWebDataUtils.buildSuccess(this.initMysqlDriverService.checkDriverStatus());
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/downloadMysqlDriver", method = { RequestMethod.POST })
    public ResWebData<?> downloadMysqlDriver() {
        this.initMysqlDriverService.downloadDriver();
        return ResWebDataUtils.buildSuccess(null);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/previewScripts", method = { RequestMethod.POST })
    public ResWebData<?> previewScripts(@RequestBody Map<String, String> params) {
        return ResWebDataUtils.buildSuccess(initService.previewExecutionScripts(params));
    }

    /**
     * Applies the full initialization flow: save configuration, run Flyway migration, and update the administrator account.
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/applyConfig", method = { RequestMethod.POST })
    public ResWebData<?> applyConfig(@RequestBody Map<String, String> config) throws Exception {
        initService.applyInitConfig(config);
        return ResWebDataUtils.buildSuccess(null);
    }

    /**
     * Updates only the database configuration in dbOnly mode.
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updateDbConfig", method = { RequestMethod.POST })
    public ResWebData<?> updateDbConfig(@RequestBody Map<String, String> config) throws Exception {
        initService.updateDbConfig(config);
        return ResWebDataUtils.buildSuccess(null);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/upgrade", method = { RequestMethod.POST })
    public ResWebData<?> upgrade(@RequestBody(required = false) Map<String, String> config) {
        try {
            initService.upgradeSystem(config == null ? Collections.emptyMap() : config);
            return ResWebDataUtils.buildSuccess(null);
        } catch (Exception e) {
            return ResWebDataUtils.buildError(initService.buildDetailedErrorMessage(e));
        }
    }

    /**
     * Requests a system restart.
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/restart", method = { RequestMethod.POST })
    public ResWebData<?> restart() {
        initService.scheduleRestart();
        return ResWebDataUtils.buildSuccess(null);
    }
}

package com.clougence.clouddm.init.controller;

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
import com.clougence.clouddm.init.service.SysInitService;
import com.clougence.rdp.constant.auth.RequestAuth;

import jakarta.annotation.Resource;

/**
 * 系统初始化 REST API。
 * 所有接口在 Initial 状态下开放（Ignore 认证）。
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/init")
public class InitController {

    @Resource
    private SysInitService    initService;
    @Resource
    private SysInitDefService defService;

    /**
     * 获取默认配置字段定义。
     * schema 来自 init-fields.json，defaultValue 来自 classpath *.properties。
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/defaultConfig", method = { RequestMethod.POST })
    public ResWebData<?> defaultConfig() {
        List<InitFieldDef> fields = this.defService.loadInitFieldDefs();
        return ResWebDataUtils.buildSuccess(fields);
    }

    /**
     * 测试数据库连接 + 空库检测 + 已安装检测。
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
    @RequestMapping(value = "/previewScripts", method = { RequestMethod.POST })
    public ResWebData<?> previewScripts(@RequestBody Map<String, String> params) {
        return ResWebDataUtils.buildSuccess(initService.previewExecutionScripts(params));
    }

    /**
     * 保存初始化配置（完整模式：写配置 + Flyway 迁移 + 更新管理员）。
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/applyConfig", method = { RequestMethod.POST })
    public ResWebData<?> applyConfig(@RequestBody Map<String, String> config) throws Exception {
        initService.applyInitConfig(config);
        return ResWebDataUtils.buildSuccess(null);
    }

    /**
     * 仅更新数据库配置（dbOnly 模式）。
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updateDbConfig", method = { RequestMethod.POST })
    public ResWebData<?> updateDbConfig(@RequestBody Map<String, String> config) throws Exception {
        initService.updateDbConfig(config);
        return ResWebDataUtils.buildSuccess(null);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/upgrade", method = { RequestMethod.POST })
    public ResWebData<?> upgrade() {
        try {
            initService.upgradeSystem();
            return ResWebDataUtils.buildSuccess(null);
        } catch (Exception e) {
            return ResWebDataUtils.buildError(initService.buildDetailedErrorMessage(e));
        }
    }

    /**
     * 触发系统重启。
     */
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/restart", method = { RequestMethod.POST })
    public ResWebData<?> restart() {
        initService.scheduleRestart();
        return ResWebDataUtils.buildSuccess(null);
    }
}

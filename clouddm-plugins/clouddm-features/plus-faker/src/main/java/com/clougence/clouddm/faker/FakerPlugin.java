package com.clougence.clouddm.faker;

import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.faker.config.i18n.FakerI18nKeys;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerDefService;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;

/**
 * @author olddream
 */
@Plugin()
public class FakerPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SessionService sessionService = dsPlugin.findGlobalService(SessionService.class);
        ConfigService configService = dsPlugin.findGlobalService(ConfigService.class);

        dsPlugin.bindGlobalToolService(FakerPluginConfig.TOOL_NAME, (toolsConfig) -> {
            return new FakerTools(sessionService, configService, (FakerPluginConfig) toolsConfig);
        });
        dsPlugin.addGlobalService(new FakerDefService());

        dsPlugin.bindGlobalI18n(FakerI18nKeys.class);
        dsPlugin.bindGlobalI18n(FakerSeedI18nKeys.class);
    }
}

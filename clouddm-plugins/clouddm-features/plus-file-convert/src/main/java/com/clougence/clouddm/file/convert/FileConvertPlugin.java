package com.clougence.clouddm.file.convert;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.file.convert.constants.ConvertI18nKeys;
import com.clougence.clouddm.file.convert.json.JsonFileFormatConvert;
import com.clougence.clouddm.file.convert.sql.SqlFileFormatConvert;
import com.clougence.clouddm.file.convert.xlsx.MsExcelFileFormatConvert;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Plugin()
public class FileConvertPlugin implements DsPlugin, DsFeatureIDs {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // initI18n
        dsPlugin.bindGlobalI18n(ConvertI18nKeys.class);

        // spi
        SessionService sessionService = dsPlugin.findGlobalService(SessionService.class);
        ConfigService configService = dsPlugin.findGlobalService(ConfigService.class);
        dsPlugin.addGlobalSpi(new MsExcelFileFormatConvert(sessionService));
        dsPlugin.addGlobalSpi(new JsonFileFormatConvert(sessionService));
        dsPlugin.addGlobalSpi(new SqlFileFormatConvert(sessionService, configService));
    }
}

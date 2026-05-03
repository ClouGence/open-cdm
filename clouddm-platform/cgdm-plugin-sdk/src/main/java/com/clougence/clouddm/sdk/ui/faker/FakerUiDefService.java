package com.clougence.clouddm.sdk.ui.faker;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.model.faker.FakerRunModel;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.i18n.I18nUtils;

/**
 * @author olddream
 */
public interface FakerUiDefService extends Service {

    FakerUiPanel fetchFakerUiPanelForFull(I18nUtils i18nMessages);

    FakerUiPanel fetchFakerUiPanelForIncrement(I18nUtils i18nMessages);

    FakerUiData fetchFakerUiData(DataSourceType dsType, List<RdbColumn> columns, FakerRunModel type);
}

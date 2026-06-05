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

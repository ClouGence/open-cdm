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
package com.clougence.clouddm.faker.seed.geometry;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class GeometrySeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(GEOMETRY_TYPE)
            .type(UiPanelFieldType.Options)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_GEOMETRYTYPE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_GEOMETRYTYPE_DESC)
            .options(fetchGeometryType())
            .build());
        fields.add(UiPanelField.builder()
            .field(FORMAT_TYPE)
            .type(UiPanelFieldType.Options)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_FORMATTYPE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_FORMATTYPE_DESC)
            .options(fetchFormatType())
            .build());
        fields.add(UiPanelField.builder()
            .field(PRECISION)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_PRECISION_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_PRECISION_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(MIN_POINT_SIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_MINPOINTSIZE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_MINPOINTSIZE_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(MAX_POINT_SIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_MAXPOINTSIZE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_MAXPOINTSIZE_DESC)
            .build());
        return fields;
    }

    private List<ValueDef> fetchGeometryType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_RANDOM_LABEL, "Random"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_POINT_LABEL, "Point"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_LINE_LABEL, "Line"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_LSEG_LABEL, "Lseg"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_BOX_LABEL, "Box"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_PATH_LABEL, "Path"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_POLYGON_LABEL, "Polygon"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_MULTIPOLYGON_LABEL, "MultiPolygon"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_CIRCLE_LABEL, "Circle"));
        return result;
    }

    private List<ValueDef> fetchFormatType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_WKT_LABEL, "WKT"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_WKB_LABEL, "WKB"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GEOMETRY_WKBHEX_LABEL, "WKBHEX"));
        return result;
    }

}

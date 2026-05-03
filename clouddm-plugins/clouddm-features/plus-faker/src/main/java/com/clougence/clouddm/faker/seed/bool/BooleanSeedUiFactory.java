package com.clougence.clouddm.faker.seed.bool;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class BooleanSeedUiFactory implements SeedUiFactory {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        return new ArrayList<>();
    }

}

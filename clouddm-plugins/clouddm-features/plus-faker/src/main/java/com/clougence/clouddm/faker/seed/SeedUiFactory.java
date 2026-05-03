package com.clougence.clouddm.faker.seed;

import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;

/**
 * @author olddream
 */
public interface SeedUiFactory {

    List<UiPanelField> buildSeedUi();
}

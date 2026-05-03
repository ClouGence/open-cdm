package com.clougence.clouddm.sdk.ui.browser;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * @author mode 2021/1/18 17:37
 */
public interface DsBrowseSpi extends Spi {

    List<UmiTypes> getLevels();

    Map<UmiTypes, List<UmiTypes>> getLeafGroupMap();

    List<UmiTypes> getLeafExpand();

    String getLeftQualifier();

    String getRightQualifier();

    DsCaseType getCaseType();

    List<String> getMenus(DsMenuType targetType);
}

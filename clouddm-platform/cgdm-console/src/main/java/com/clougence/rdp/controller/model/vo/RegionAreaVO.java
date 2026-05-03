package com.clougence.rdp.controller.model.vo;

import com.clougence.rdp.dal.enumeration.RegionArea;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/4/14 22:23:48
 */
@Getter
@Setter
public class RegionAreaVO {

    private RegionArea regionArea;

    private String     i18nName;

    public RegionAreaVO(RegionArea regionArea){
        this.regionArea = regionArea;
        if (regionArea != null) {
            this.i18nName = RdpI18nUtils.getMessage(regionArea.name());
        }
    }
}

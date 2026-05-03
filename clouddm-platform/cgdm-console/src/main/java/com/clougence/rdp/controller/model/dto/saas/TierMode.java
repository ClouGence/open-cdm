package com.clougence.rdp.controller.model.dto.saas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum TierMode {

    @JsonProperty("volume")
    VOLUME,
    @JsonProperty("graduated")
    GRADUATED;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TierMode fromString(String mode) {
        for (TierMode modeEnum : TierMode.values()) {
            if (modeEnum.name().equalsIgnoreCase(mode)) {
                return modeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + mode);
    }
}

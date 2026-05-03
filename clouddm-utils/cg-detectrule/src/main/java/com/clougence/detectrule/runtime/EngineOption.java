package com.clougence.detectrule.runtime;

import com.clougence.detectrule.parser.DetectRulesFeature;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EngineOption {

    private DataTimeValueParser  dataTimeValueParser;

    private DetectRulesFeature[] features = DetectRulesFeature.values();
}

package com.clougence.detectrule.parser.antlr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.detectrule.parser.DetectRulesFeature;

public abstract class DetectRulesBasicParser extends Parser {

    private final Set<DetectRulesFeature> feature = new HashSet<>();

    public DetectRulesBasicParser(TokenStream input){
        super(input);
        this.initFeatures(null);
    }

    public boolean support(DetectRulesFeature feature) {
        return this.feature.contains(feature);
    }

    protected void initFeatures(List<DetectRulesFeature> features) {
        if (features != null) {
            this.feature.addAll(features);
        }
    }
}

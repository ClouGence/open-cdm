package com.clougence.detectrule.parser.antlr;

import static com.clougence.detectrule.parser.DetectRulesFeature.*;

import java.util.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

import com.clougence.detectrule.parser.DetectRulesFeature;

public abstract class DetectRulesBasicLexer extends Lexer {

    private final Set<DetectRulesFeature> feature  = new HashSet<>();
    protected Map<String, Integer>        keyWords = new HashMap<>();

    public DetectRulesBasicLexer(CharStream input){
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

        if (this.support(AllowDefineFeature)) {
            this.keyWords.putAll(DetectRulesAlias.define);
        }

        if (this.support(AllowAdvancedTypeFeature)) {
            this.keyWords.putAll(DetectRulesAlias.advancedTypeAlias);
        }

        if (support(AllowSymbolAliasFeature)) {
            this.keyWords.putAll(DetectRulesAlias.compareSymbolAlias);
            this.keyWords.putAll(DetectRulesAlias.logicSymbolAlias);
            this.keyWords.putAll(DetectRulesAlias.arithmeticSymbolAlias);
            this.keyWords.putAll(DetectRulesAlias.bitwiseSymbolAlias);
        }

        if (support(AllowProgramFeature)) {
            this.keyWords.putAll(DetectRulesAlias.programFeature);
        }
    }
}

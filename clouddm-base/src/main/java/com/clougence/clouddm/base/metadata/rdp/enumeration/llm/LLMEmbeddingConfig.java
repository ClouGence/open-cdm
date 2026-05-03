package com.clougence.clouddm.base.metadata.rdp.enumeration.llm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LLMEmbeddingConfig {

    private int dimension;

    public LLMEmbeddingConfig(){
    }

    public LLMEmbeddingConfig(int dimension){
        this.dimension = dimension;
    }
}

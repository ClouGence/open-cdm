package com.clougence.clouddm.faker.config.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.config.dsl.DslFunction;
import com.clougence.clouddm.faker.config.dsl.DslFunctionLoopUp;
import com.clougence.clouddm.faker.config.dsl.DslFunctionRegistry;
import com.clougence.clouddm.faker.config.parameter.ParameterProcessorLookUp;
import com.clougence.clouddm.faker.config.parameter.ParameterRegistry;
import com.clougence.utils.StringUtils;

/**
 * @author chunlin
 * @date 2024/4/10
 */
public class HanaSpiRegistry implements DslFunctionLoopUp, ParameterProcessorLookUp {

    @Override
    public void loopUp(DslFunctionRegistry registry) {
        registry.register("hanaIgnoreActSupport", hanaIgnoreActSupport());
    }

    @Override
    public void loopUp(ParameterRegistry registry) {
    }

    private static DslFunction hanaIgnoreActSupport() {
        return (args, context) -> {
            List<UseFor> ignoreAct = new ArrayList<>();
            Map attributes = (Map) context.get("attributes");
            if (attributes == null || attributes.isEmpty()) {
                return ignoreAct;
            }
            String rdbAuto = (String) attributes.get("rdb_auto");
            if (!StringUtils.isBlank(rdbAuto) && "true".equals(rdbAuto)) {
                ignoreAct.add(UseFor.Insert);
                ignoreAct.add(UseFor.UpdateSet);
            }

            return ignoreAct;
        };
    }
}

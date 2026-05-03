package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.KeyValueDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class OptionsBuilder extends AbstractDomainBuilder {

    Map<String, String> options = new HashMap<>();

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new OptionsDomain(options));
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.KEY_VALUE) {
            KeyValueDomain domain = (KeyValueDomain) list.get(0);
            String put = options.put(domain.getKey(), domain.getValue());
            if (put != null) {
                throw new UnsupportedOperationException("options contains same option: " + domain.getKey());
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}

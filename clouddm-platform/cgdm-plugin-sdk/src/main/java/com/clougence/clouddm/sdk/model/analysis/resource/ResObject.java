package com.clougence.clouddm.sdk.model.analysis.resource;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResObject {

    private static final DsResPathObj EMPTY = new DsResPathObj("/");

    protected String                  name;
    protected TargetType              type;

    public DsResPath toDsResPath() {
        if (StringUtils.isBlank(this.name)) {
            return EMPTY;
        } else {
            return new DsResPathObj("/" + this.name);
        }
    }
}

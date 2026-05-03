package com.clougence.clouddm.sec.rules.domain;

import com.clougence.clouddm.sec.rules.domain.func.FuncGroup;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public abstract class CheckerDomain {

    private FuncGroup func = FuncGroup.INSTANCE;
}

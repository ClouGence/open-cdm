package com.clougence.dslparser.detectrule.test;

import java.util.HashMap;
import java.util.Map;

import com.clougence.dslparser.detectrule.test.func.FuncGroupUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestMapDomain {

    private FuncGroupUtils fun    = new FuncGroupUtils();

    private Map            values = new HashMap();

}

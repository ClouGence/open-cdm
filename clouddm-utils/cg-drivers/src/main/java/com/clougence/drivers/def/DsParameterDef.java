package com.clougence.drivers.def;

import java.util.List;

public interface DsParameterDef {

    List<DsParameter> getParameters();

    DsParameter getParameter(String parameter);
}

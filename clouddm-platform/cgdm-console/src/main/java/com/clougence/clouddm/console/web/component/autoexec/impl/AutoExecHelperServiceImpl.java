package com.clougence.clouddm.console.web.component.autoexec.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;

@Service
public class AutoExecHelperServiceImpl implements AutoExecHelperService {

    private final Map<SQLJobBizType, AutoExecHelper> helperMap = new HashMap<>();

    public AutoExecHelperServiceImpl(List<AutoExecHelper> helpers){
        for (AutoExecHelper helper : helpers) {
            SQLJobBizType type = helper.getHandleType();
            if (!helperMap.containsKey(type)) {
                helperMap.put(type, helper);
            } else {
                throw new RuntimeException("AutoExecHelper about " + type + " already exists");
            }
        }
    }

    @Override
    public AutoExecHelper getHelper(SQLJobBizType type) {
        return helperMap.get(type);
    }
}

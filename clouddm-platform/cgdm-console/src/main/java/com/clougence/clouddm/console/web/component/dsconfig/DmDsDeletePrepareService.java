package com.clougence.clouddm.console.web.component.dsconfig;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.global.exception.ErrorMessageException;

@Service
@Deprecated
public class DmDsDeletePrepareService {

    @Resource
    private DmDsService dmDsService;

    public void prepareDelete(String puid, long dsId) {
        ResWebData<Boolean> disableDevOpsResult = this.dmDsService.disableDsDevOps(puid, dsId);
        if (!disableDevOpsResult.isSuccess()) {
            throw new ErrorMessageException(disableDevOpsResult.getMsg());
        }

        ResWebData<Boolean> disableQueryResult = this.dmDsService.disableDsQuery(puid, dsId);
        if (!disableQueryResult.isSuccess()) {
            throw new ErrorMessageException(disableQueryResult.getMsg());
        }
    }
}

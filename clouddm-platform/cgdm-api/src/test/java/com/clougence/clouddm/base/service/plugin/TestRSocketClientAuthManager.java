package com.clougence.clouddm.base.service.plugin;

import java.io.IOException;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class TestRSocketClientAuthManager implements RSocketClientAuthManager {

    @Override
    public ConnAuthDTO acquireClientAuthInfo() throws IOException {
        ConnAuthDTO authDto = GlobalConfUtils.loadGlobalConf();
        if (StringUtils.isBlank(authDto.getWsn()) || StringUtils.isBlank(authDto.getAk()) || StringUtils.isBlank(authDto.getSk())) {
            throw new IllegalArgumentException("properties in global config (" + authDto + ") are empty.");
        }

        return authDto;
    }

    @Override
    public String acquireServerDomain() throws IOException {
        ConnAuthDTO dto = GlobalConfUtils.loadGlobalConf();

        //        if (StringUtils.isBlank(dto.getConsoleDomain())) {
        //            throw new IllegalArgumentException("properties console domain in global config (" + dto.getGlobalConfResource() + ") are empty.");
        //        }
        //
        return dto.getConsoleHost();
    }

}

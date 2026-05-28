/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.rdp.service.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.model.fo.AddWebViewLogFO;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonWebViewLogDO;
import com.clougence.rdp.service.RdpWebViewLogService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/5/26 17:12:09
 */
@Service
@Slf4j
public class RdpWebViewLogServiceImpl implements RdpWebViewLogService {
    @Resource
    private MonitorDal monitorDal;

    @Override
    public void addOneLog(AddWebViewLogFO logFO, String uid) {
        DmMonWebViewLogDO l = new DmMonWebViewLogDO();
        dataTruncation(logFO);
        l.setUid(uid);
        l.setUri(logFO.getUri());
        l.setClientId(logFO.getClientId());
        l.setSrc(logFO.getSrc());
        l.setKeyword(logFO.getKw());
        l.setVbId(logFO.getVbId());
        monitorDal.webViewLogMapper().insert(l);
    }

    private void dataTruncation(AddWebViewLogFO logFO) {
        if (logFO.getKw() != null && logFO.getKw().length() >= KEY_WORD_CONTENT_LENGTH) {
            logFO.setKw(logFO.getKw().substring(0, KEY_WORD_CONTENT_LENGTH));
        }

        if (logFO.getSrc() != null && logFO.getSrc().length() >= SRC_CONTENT_LENGTH) {
            logFO.setSrc(logFO.getSrc().substring(0, SRC_CONTENT_LENGTH));
        }

        if (logFO.getVbId() != null && logFO.getVbId().length() >= VB_ID_CONTENT_LENGTH) {
            logFO.setVbId(logFO.getVbId().substring(0, VB_ID_CONTENT_LENGTH));
        }

        if (logFO.getClientId() != null && logFO.getClientId().length() >= CLIENT_ID_CONTENT_LENGTH) {
            logFO.setClientId(logFO.getClientId().substring(0, CLIENT_ID_CONTENT_LENGTH));
        }
    }
}

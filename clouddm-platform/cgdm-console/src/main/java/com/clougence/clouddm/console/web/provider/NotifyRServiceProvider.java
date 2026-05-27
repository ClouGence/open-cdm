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
package com.clougence.clouddm.console.web.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.notify.NotifyRService;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.model.vo.export.DmExportStatus;
import com.clougence.clouddm.console.web.model.vo.export.DmExportVO;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.FileStatus;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class NotifyRServiceProvider extends AbstractBasicProvider implements NotifyRService {
    @Resource
    private ExecutionDal executionDal;

    @Override
    public void notifyConvertFailed(String puid, String userId, String srcFileId, String exportId, String message) {
        this.executionDal.fileMapper().updateStatusByUniqueId(exportId, FileStatus.Failed, message);

        DmExportVO exportVO = new DmExportVO();
        exportVO.setUid(userId);
        exportVO.setTrackId(exportId);
        exportVO.setStatus(DmExportStatus.FAILED);
        exportVO.setMessage(message);
        exportVO.setPercent(0);
        exportVO.setCurrent(0);
        exportVO.setTotal(0);
        DmGlobalEventBus.triggerQueryResultExportEvent(exportVO);
    }

    @Override
    public void notifyConvertFinish(String puid, String userId, String srcFileId, String exportId, String message, long total) {
        this.executionDal.fileMapper().updateStatusByUniqueId(exportId, FileStatus.Ready, message);

        DmExportVO exportVO = new DmExportVO();
        exportVO.setUid(userId);
        exportVO.setTrackId(exportId);
        exportVO.setStatus(DmExportStatus.FINISHED);
        exportVO.setMessage(message);
        exportVO.setPercent(100);
        exportVO.setCurrent(total);
        exportVO.setTotal(total);
        DmGlobalEventBus.triggerQueryResultExportEvent(exportVO);
    }

    @Override
    public void notifyConvertProgress(String puid, String userId, String srcFileId, String exportId, String message, long from, long to, long current) {
        this.executionDal.fileMapper().updateAccessTimeByUniqueId(srcFileId, message);
        this.executionDal.fileMapper().updateAccessTimeByUniqueId(exportId, message);

        int i = BigDecimal.valueOf(current).divide(BigDecimal.valueOf(to), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();

        DmExportVO exportVO = new DmExportVO();
        exportVO.setUid(userId);
        exportVO.setTrackId(exportId);
        exportVO.setStatus(DmExportStatus.PROGRESS);
        exportVO.setMessage(message);
        exportVO.setPercent(i);
        exportVO.setCurrent(current);
        exportVO.setTotal(to);
        DmGlobalEventBus.triggerQueryResultExportEvent(exportVO);
    }
}

package com.clougence.clouddm.console.web.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.notify.NotifyRService;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.console.web.dal.enumeration.FileStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmFileMapper;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.model.vo.export.DmExportStatus;
import com.clougence.clouddm.console.web.model.vo.export.DmExportVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class NotifyRServiceProvider extends AbstractBasicProvider implements NotifyRService {

    @Resource
    private DmFileMapper dmFileMapper;

    @Override
    public void notifyConvertFailed(String puid, String userId, String srcFileId, String exportId, String message) {
        this.dmFileMapper.updateStatusByUniqueId(exportId, FileStatus.Failed, message);

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
        this.dmFileMapper.updateStatusByUniqueId(exportId, FileStatus.Ready, message);

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
        this.dmFileMapper.updateAccessTimeByUniqueId(srcFileId, message);
        this.dmFileMapper.updateAccessTimeByUniqueId(exportId, message);

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

package com.clougence.rdp.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.controller.model.fo.AddWebViewLogFO;
import com.clougence.rdp.dal.mapper.RdpWebViewLogMapper;
import com.clougence.rdp.dal.model.RdpWebViewLogDO;
import com.clougence.rdp.service.RdpWebViewLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/5/26 17:12:09
 */
@Service
@Slf4j
public class RdpWebViewLogServiceImpl implements RdpWebViewLogService {

    @Resource
    private RdpWebViewLogMapper rdpWebViewLogMapper;

    @Override
    public void addOneLog(AddWebViewLogFO logFO, String uid) {
        RdpWebViewLogDO l = new RdpWebViewLogDO();
        dataTruncation(logFO);
        l.setUid(uid);
        l.setUri(logFO.getUri());
        l.setClientId(logFO.getClientId());
        l.setSrc(logFO.getSrc());
        l.setKeyword(logFO.getKw());
        l.setVbId(logFO.getVbId());
        rdpWebViewLogMapper.insert(l);
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

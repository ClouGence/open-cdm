package com.clougence.clouddm.console.web.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.dal.enumeration.DmEventType;
import com.clougence.clouddm.console.web.dal.mapper.AlertConfigDetailMapper;
import com.clougence.clouddm.console.web.dal.model.AlertConfigDetailDO;
import com.clougence.clouddm.console.web.model.fo.cluster.OnOffWorkerAlertFO;
import com.clougence.clouddm.console.web.model.vo.AlertConfigVO;
import com.clougence.clouddm.console.web.service.system.AlertConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Service
@Slf4j
public class AlertConfigServiceImpl implements AlertConfigService {

    @Resource
    private AlertConfigDetailMapper detailMapper;

    //    @Override
    //    public List<AlertConfigVO> listSystemAlertConfig() {
    //        List<AlertConfigDetailDO> configDetails = detailMapper.listByEventTypes(Lists.newArrayList(DmEventType.CONSOLE_EXCEPTION, DmEventType.WORKER_EXCEPTION));
    //        return convertToAlertConfigVO(configDetails);
    //    }

    //    @Override
    //    public void updateByIdAndUid(AlertConfigVO alertConfigVO) {
    //        AlertConfigDetailDO alertConfigDetailDO = convertToDetailDO(alertConfigVO);
    //        detailMapper.updateByIdAndUid(alertConfigDetailDO);
    //    }

    @Override
    public void onOffWorkerAlert(OnOffWorkerAlertFO configFo, String uid) {
        detailMapper.updateSwitchByWorker(configFo.isPhone(), configFo.isEmail(), configFo.isDingding(), configFo.isSms(), uid, configFo.getWorkerId());
    }

    @Override
    public void addAlertConfig(List<AlertConfigDetailDO> alertConfigVOList, DmEventType eventType) {
        for (AlertConfigDetailDO config : alertConfigVOList) {
            config.setEventType(eventType);
            detailMapper.insert(config);
        }
    }

    //    @Override
    //    public List<AlertConfigDetailDO> getAlertConfigByEventType(DmEventType eventType) {
    //        return detailMapper.listByEventType(eventType);
    //    }

    private AlertConfigDetailDO convertToDetailDO(AlertConfigVO alertConfigVO) {
        AlertConfigDetailDO detailDO = new AlertConfigDetailDO();
        detailDO.setId(alertConfigVO.getId());
        detailDO.setUid(alertConfigVO.getUid());
        detailDO.setPhone(alertConfigVO.isPhone());
        detailDO.setEmail(alertConfigVO.isEmail());
        detailDO.setDingding(alertConfigVO.isDingding());
        detailDO.setSms(alertConfigVO.isSms());
        detailDO.setDuplicated(alertConfigVO.isDuplicated());
        detailDO.setExpression(alertConfigVO.getExpression());
        detailDO.setRuleName(alertConfigVO.getRuleName());
        detailDO.setSendAdmin(alertConfigVO.isSendAdmin());
        detailDO.setSendSystem(alertConfigVO.isSendSystem());
        detailDO.setWorkerId(alertConfigVO.getWorkerId());
        return detailDO;
    }

    @Override
    public AlertConfigVO convertToAlertConfigVO(AlertConfigDetailDO alertConfigDetailDO) {
        AlertConfigVO vo = new AlertConfigVO();
        //        vo.setId(alertConfigDetailDO.getId());
        vo.setDingding(alertConfigDetailDO.isDingding());
        vo.setPhone(alertConfigDetailDO.isPhone());
        vo.setEmail(alertConfigDetailDO.isEmail());
        vo.setSms(alertConfigDetailDO.isSms());
        vo.setUid(alertConfigDetailDO.getUid());
        vo.setDuplicated(alertConfigDetailDO.isDuplicated());
        vo.setExpression(alertConfigDetailDO.getExpression());
        vo.setRuleName(alertConfigDetailDO.getRuleName());
        vo.setSendAdmin(alertConfigDetailDO.isSendAdmin());
        vo.setSendSystem(alertConfigDetailDO.isSendSystem());
        vo.setWorkerId(alertConfigDetailDO.getWorkerId());
        return vo;
    }

    //    @Override
    //    public AlertConfigVO getWorkerAlertConfig(long workerId, String uid) {
    //        AlertConfigDetailDO alertConfigDetailDO = detailMapper.listByWorkerId(workerId);
    //        if (alertConfigDetailDO == null) {
    //            AlertConfigVO alertConfigVO = workerService.generateDefaultWorkerAlertConfigVO(uid, workerId);
    //            this.addAlertConfig(Lists.newArrayList(alertConfigVO), DmEventType.WORKER_EXCEPTION);
    //            return alertConfigVO;
    //        } else {
    //            return convertToAlertConfigVO(alertConfigDetailDO);
    //        }
    //    }

    //    @Override
    //    public AlertConfigDetailDO getWorkerAlertConfigDO(long workerId) {
    //        return detailMapper.listByWorkerId(workerId);
    //    }

    @Override
    public void deleteByWorkerId(long workerId) {
        detailMapper.deleteByWorkerId(workerId);
    }

    private List<AlertConfigVO> convertToAlertConfigVO(List<AlertConfigDetailDO> alertConfigDetailDOList) {
        List<AlertConfigVO> alertConfigVOList = new ArrayList<>();
        for (AlertConfigDetailDO alertConfigDetailDO : alertConfigDetailDOList) {
            alertConfigVOList.add(convertToAlertConfigVO(alertConfigDetailDO));
        }
        return alertConfigVOList;
    }
}

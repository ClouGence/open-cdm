package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.model.RdpVerifyDO;

/**
 * @author bucketli 2020/2/27 19:57
 */
public interface RdpVerifyMapper extends BaseMapper<RdpVerifyDO> {

    /** query by uid ,get all uid's {@link RdpVerifyDO}s */
    List<RdpVerifyDO> queryByUid(String uid);

    RdpVerifyDO queryByUidAndType(VerifyType verifyType, VerifyCodeType verifyCodeType, String uid);

    /** query by email（sql condition contain {@link VerifyType#EMAIL_VERIFY_CODE},{@link VerifyCodeType},email） */
    RdpVerifyDO queryByPrimaryEmail(VerifyType verifyType, VerifyCodeType verifyCodeType, String email);

    /** query by phone (sql condition contain {@link VerifyType#SMS_VERIFY_CODE},{@link VerifyCodeType},phone) */
    RdpVerifyDO queryByPrimaryPhone(VerifyType verifyType, VerifyCodeType verifyCodeType, String phone);

    RdpVerifyDO queryByPrimaryPhoneAndAreaCode(VerifyType verifyType, VerifyCodeType verifyCodeType, String phone, AreaCode areaCode);

    /** update failTimes by {@link RdpVerifyDO id} */
    void updateFailTimesAndDateById(Integer failTimes, Date failDate, Long id);

    /** update sent verify code and time by {@link RdpVerifyDO id} */
    int updateVerifyCodeAndSendTime(String verifyCode, Date sendTime, Long id);

    void deleteByUid(String uid);

    /** query by uid ,get all uid's {@link RdpVerifyDO}s */
    void deleteOldData(Date beforeThisTime);

    void updatePhoneOrEmailByUid(String uid, String phone, String email, VerifyCodeType verifyCodeType, VerifyType verifyType);
}

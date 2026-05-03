package com.clougence.rdp.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.RdpNamingService;
import com.clougence.rdp.util.RandomStrUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/7/10 21:18
 */
@Slf4j
@Service
public class RdpNamingServiceImpl implements RdpNamingService {

    @Resource
    private RdpUserMapper rdpUserMapper;

    @Override
    public String genAccessKey() {
        String namePattern = "ak%s";

        while (true) {
            String ak = String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
            RdpUserDO userDO = rdpUserMapper.queryByAccessKey(ak);
            if (userDO == null) {
                return ak;
            }
        }
    }

    @Override
    public String genUid() {
        while (true) {
            String uid = RandomStrUtils.fixedLenRandomNumberStr(16);
            RdpUserDO user = rdpUserMapper.queryByUid(uid);
            if (user == null) {
                return uid;
            }
        }
    }

    @Override
    public String genSecretKey() {
        String namePattern = "sk%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
    }

    @Override
    public String genInnerUserPwd() {
        String namePattern = "inner%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
    }

    @Override
    public String genProductClusterName() {
        String namePattern = "pc_name_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }

    @Override
    public String genProductClusterCode() {
        String namePattern = "pc_code_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }

    @Override
    public String genConsoleJobToken() {
        String namePattern = "cjtoken_" + System.currentTimeMillis() + "_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }
}

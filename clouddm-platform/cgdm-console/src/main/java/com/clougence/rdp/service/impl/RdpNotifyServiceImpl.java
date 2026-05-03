package com.clougence.rdp.service.impl;

import org.springframework.stereotype.Service;

import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.enumeration.UserOperationType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2023/11/24 10:24:56
 */
@Service
@Slf4j
public class RdpNotifyServiceImpl implements RdpNotifyService {

    @Override
    public void notifyUser(String curUID, String userUID, UserOperationType type) {

    }
}

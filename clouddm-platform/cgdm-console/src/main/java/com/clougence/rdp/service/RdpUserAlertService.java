package com.clougence.rdp.service;

import com.clougence.rdp.component.alert.RdpImAlertService;
import com.clougence.rdp.component.alert.RdpMailAlertService;
import com.clougence.rdp.component.alert.RdpSmsAlertService;
import com.clougence.rdp.service.enumeration.AlertImType;

/**
 * @author bucketli 2023/12/26 14:35:33
 */
public interface RdpUserAlertService {

    RdpMailAlertService chooseMailAlertService();

    boolean fetchUserImAlertAtAll(String uid);

    RdpImAlertService chooseImAlertService(String uid);

    RdpImAlertService chooseImAlertService(AlertImType alertImType);

    RdpSmsAlertService chooseSmsAlertService(String uid);

    //    RdpVoiceAlertService chooseVoiceAlertService(String uid);
}

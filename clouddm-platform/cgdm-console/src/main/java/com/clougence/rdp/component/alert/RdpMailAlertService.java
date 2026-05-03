package com.clougence.rdp.component.alert;

import java.util.List;

import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.model.MailDTO;

/**
 * @author bucketli 2020-02-01 11:50
 * @since 1.1.3
 */
public interface RdpMailAlertService {

    /**
     * Send mail to someone. Need to ensure send mail with lock. Every user share
     * the mailSender object
     */
    SendMsgResult sendMail(MailDTO mailDTO, RdpUserDO sendUser, List<String> receiverUids);
}

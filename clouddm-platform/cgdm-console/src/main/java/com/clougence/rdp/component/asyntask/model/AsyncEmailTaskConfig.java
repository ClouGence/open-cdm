package com.clougence.rdp.component.asyntask.model;

import java.util.List;

import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.model.MailDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsyncEmailTaskConfig {

    private MailDTO      mailDTO;

    private RdpUserDO    userDO;

    private List<String> receiverUids;
}

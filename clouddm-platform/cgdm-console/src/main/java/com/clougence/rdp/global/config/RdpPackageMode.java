package com.clougence.rdp.global.config;

import com.clougence.rdp.util.RdpHostUtil;

public enum RdpPackageMode {

    TGZ,
    DOCKER,
    K8S;

    public boolean isContainer() { return this == DOCKER || this == K8S; }

    public String getLocalIpOrHostName() {
        return this == K8S ? RdpHostUtil.getHostIpOrHostName(RdpHostUtil.LOCAL_HOST_NAME) : RdpHostUtil.getHostIpOrHostName(RdpHostUtil.LOCAL_IP);
    }
}

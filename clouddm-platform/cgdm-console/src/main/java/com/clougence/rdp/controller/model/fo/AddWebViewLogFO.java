package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/2/1 19:15
 */
@Data
public class AddWebViewLogFO {

    /**
     * referrer
     */
    private String src;

    /**
     * kw
     */
    private String kw;

    /**
     * target uri
     */
    @NotBlank(message = "{notblank.viewlog.uri}")
    private String uri;

    /**
     * baidu vb_id
     */
    private String vbId;

    /**
     * browser client_id
     */
    private String clientId;
}

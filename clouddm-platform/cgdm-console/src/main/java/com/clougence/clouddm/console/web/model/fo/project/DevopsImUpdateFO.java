package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsImUpdateFO {

    private long    imId;
    private String  newDisplay;
    private String  newWebhookUrl;
    private String  newSecret;
    private boolean force;
}

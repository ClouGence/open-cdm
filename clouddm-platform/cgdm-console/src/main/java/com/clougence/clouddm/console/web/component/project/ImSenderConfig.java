package com.clougence.clouddm.console.web.component.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImSenderConfig {

    private ImType imType;
    private String webhookUrl;
    private String secret;
}

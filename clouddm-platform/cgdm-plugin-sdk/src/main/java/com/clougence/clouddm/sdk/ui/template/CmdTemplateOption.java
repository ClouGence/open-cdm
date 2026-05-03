package com.clougence.clouddm.sdk.ui.template;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmdTemplateOption {

    //normal
    private String              targetName;
    private String              targetNewName;
    private String              targetExactName;
    private String              catalog;
    private String              schema;

    //
    private boolean             delimited;
    private boolean             cascade;
    private boolean             restrict;
    private boolean             purge;
    private boolean             usingExists;
    private int                 defaultLimit;
    private String              clusterName; // CK
    private boolean             truncateUseDelete;
    private boolean             replace;

    //trigger
    private String              triggerTable;

    private Map<String, Object> data;

}

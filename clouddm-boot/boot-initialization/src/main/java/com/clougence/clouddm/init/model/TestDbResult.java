package com.clougence.clouddm.init.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDbResult {

    private boolean success;
    private String  message;
    private String  messageType;
    private boolean canProceed;
    private boolean isEmpty;
    private boolean isInstalled;
    private boolean databaseExists;
    private boolean charsetValid;
    private String  databaseCharset;
    private boolean createDatabase;
    private boolean showRebuildChoice;
    private String  rebuildPrompt;
    private boolean requireConfirmInput;
    private String  confirmInputLabel;
    private String  confirmInputExpectedValue;
}

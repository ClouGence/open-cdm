package com.clougence.clouddm.console.web.component.project;

import java.util.Locale;
import java.util.function.Function;

import com.clougence.clouddm.sdk.messenger.MsgContent;
import com.clougence.clouddm.sdk.messenger.MsgSendResult;

public interface ImSenderService {

    String getProjectLanguage(String ownerUid, long projectId);

    MsgSendResult sendMessage(String ownerUid, ImSenderConfig imConfig, MsgContent testMessage);

    // for ci/cd

    void sendMessage(String ownerUid, long projectId, ImMessageType messageType, MsgContent testMessage);

    void sendMessage(String ownerUid, long projectId, ImMessageType imMessageType, Function<Locale, String> msgFunction);

    void sendMessage(String ownerUid, long projectId, ImMessageType imMessageType, String msgText);

}

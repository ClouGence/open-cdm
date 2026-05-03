package com.clougence.clouddm.console.web.service.faker;

import com.clougence.clouddm.console.web.model.fo.faker.FakerConfigFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerDefFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerInitFO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerDefVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerLogVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerPreviewVO;
import com.clougence.clouddm.sdk.model.faker.FakerStatusDTO;
import com.clougence.clouddm.sdk.ui.faker.FakerUiData;

public interface FakerService {

    FakerDefVO loadFakerDef(String puid, String uid, FakerDefFO fo);

    FakerUiData loadColumnSeed(String puid, String uid, FakerInitFO fo);

    FakerPreviewVO dataPreview(String puid, String uid, FakerConfigFO fo);

    boolean hasInstanceById(String uid, String sessionId);

    String execute(String uid, FakerConfigFO fo);

    FakerLogVO tailLog(String uid, String sessionId, int startLine);

    FakerStatusDTO tailStatus(String uid, String sessionId);

    void pause(String uid, String sessionId);

    void resume(String uid, String sessionId);

    void close(String uid, String sessionId);

    FakerConfigFO fetchFoConfigByToolsSession(String uid, String sessionId);
}

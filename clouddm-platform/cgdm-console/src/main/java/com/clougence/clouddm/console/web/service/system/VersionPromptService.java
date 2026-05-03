package com.clougence.clouddm.console.web.service.system;

import com.clougence.clouddm.console.web.model.vo.version.VersionDetailVO;
import com.clougence.clouddm.console.web.model.vo.version.VersionPromptVO;

public interface VersionPromptService {

    VersionPromptVO check();

    VersionDetailVO detail();

    void ignore();
}

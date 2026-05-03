package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultBuild;

public interface BatchBuild extends ResultBuild {

    void onBegin();

    void onEnd();

    void onCancel();

}

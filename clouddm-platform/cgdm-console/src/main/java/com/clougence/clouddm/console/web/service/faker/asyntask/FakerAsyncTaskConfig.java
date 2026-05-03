package com.clougence.clouddm.console.web.service.faker.asyntask;

import com.clougence.clouddm.console.web.model.fo.faker.FakerConfigFO;

import lombok.Getter;
import lombok.Setter;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Getter
@Setter
public class FakerAsyncTaskConfig {

    private String        userId;
    private String        sessionId;
    private FakerConfigFO foConfig;
}

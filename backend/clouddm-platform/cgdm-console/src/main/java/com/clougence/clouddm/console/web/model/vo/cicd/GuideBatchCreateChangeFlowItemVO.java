/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.console.web.model.vo.cicd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideBatchCreateChangeFlowItemVO {

    private String clientId;
    private long   flowId;
}

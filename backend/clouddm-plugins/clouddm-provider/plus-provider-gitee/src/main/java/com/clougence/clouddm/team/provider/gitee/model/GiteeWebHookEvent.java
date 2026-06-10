/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.team.provider.gitee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeWebHookEvent {

    // global
    private String                      hook_id;
    private String                      hook_name;
    private String                      password;
    private String                      timestamp;
    private GiteeWebHookEventRepository repository;
    // private GiteeWebHookEventProject project
    private String                      sign;

    // for push/tag
    private String                      after;
    private String                      before;
    private String                      ref;     // target branch
    private Boolean                     created; // Is it a new branch?
    private Boolean                     deleted; // Whether or not the branch is deleted
    private GiteeWebHookEventUser       pusher;
    private GiteeWebHookEventUser       sender;  // Info to trigger hook

    // for issue or pr
    private String                      state;     // Status. eg:open
    private String                      action;     // Status. eg:tested
    private String                      iid;        // Corresponding identification (issue eg: IG6E9, pr is number)
    private String                      title;      // Title
    private GiteeWebHookEventUser       user;       // PR/Issue founder.
    private GiteeWebHookEventUser       updated_by; // Updateer information for PR/Issue.
    private GiteeWebHookEventUser       target_user;// PR/Issue's commissioned user.

    // for issue
    private String                      description;    // Contents

    // for pr
    private GiteeWebHookEventUser       author;          // PR's creator information.
    private String                      body;            // Content of PR
    private String                      merge_commit_sha;// PR Consolidated Committee id.
    private String                      merge_status;    // Content of PR
    private String                      source_branch;   // source branch
    private String                      target_branch;   // target branch
    private DevOpsEventPrSource         source_repo;     // source
    private DevOpsEventPrTarget         target_repo;     // target

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DevOpsEventPrSource {

        // private GiteeWebHookEventProject project
        private GiteeWebHookEventRepository repository;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DevOpsEventPrTarget {

        // private GiteeWebHookEventProject project
        private GiteeWebHookEventRepository repository;
    }

    // for note
    // private GiteeWebHookEventUser       author;          // Comment author information.
    private String noteable_type;   // # The type of target commented upon. eg: Issue
    private String noteable_id;     // # Commented target id.
    private String note;            // # Comment. eg: Good things should open up...

}

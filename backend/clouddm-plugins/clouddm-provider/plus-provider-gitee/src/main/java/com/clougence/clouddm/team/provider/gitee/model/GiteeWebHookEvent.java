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
    private Boolean                     created; // 推送的是否是新分支
    private Boolean                     deleted; // 推送的是否是删除分支
    private GiteeWebHookEventUser       pusher;
    private GiteeWebHookEventUser       sender;  // 触发 hook 的用户信息

    // for issue or pr
    private String                      state;     // 状态。eg：open
    private String                      action;     // 状态。eg：tested
    private String                      iid;        // 对应的标识（issue eg：IG6E9，pr 为数字）
    private String                      title;      // 标题
    private GiteeWebHookEventUser       user;       // PR/Issue 创建者。
    private GiteeWebHookEventUser       updated_by; // PR/Issue 的更新者信息。
    private GiteeWebHookEventUser       target_user;// PR/Issue 的被委托处理用户。

    // for issue
    private String                      description;    // 内容

    // for pr
    private GiteeWebHookEventUser       author;          // PR 的创建者信息。
    private String                      body;            // PR 的内容
    private String                      merge_commit_sha;// PR 合并产生的 commit id。
    private String                      merge_status;    // PR 的内容
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
    // private GiteeWebHookEventUser       author;          // 评论的作者信息。
    private String noteable_type;   // # 被评论的目标类型。eg：Issue
    private String noteable_id;     // # 被评论的目标 id。
    private String note;            // # 评论内容。eg：好的东西应该开源...

}

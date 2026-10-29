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
package com.clougence.clouddm.console.web.component.approval;

import static com.clougence.clouddm.console.web.component.approval.CustomApprovalConstants.MAX_CONTEXT_BYTES;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.model.*;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalParticipantType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalTemplateDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.JsonUtils;

public interface CustomApprovalConfigService {

    void lockConfiguration(String puid);

    ApprovalConfigurationVO getConfiguration(String puid);

    InternalApprovalEngineConfig getEngineConfig(String puid);

    ApprovalTeamConfig getTeamConfig(String puid);

    List<DmApprovalTemplateDO> listCustomTemplates(String puid);

    CustomApprovalFlowConfig parseFlowConfig(DmApprovalTemplateDO template);

    void saveEngine(String puid, ApprovalEngineSaveFO request);

    void saveTeams(String puid, ApprovalTeamConfig request);

    void deleteEngine(String puid, ApprovalEngineDeleteFO request);

    void validateFlowConfig(String puid, CustomApprovalFlowConfig flowConfig, ApprovalTeamConfig teams);

    List<DmAuthUserDO> resolveParticipants(String puid, ApprovalParticipantType type, String teamId, String userUid, ApprovalTeamConfig teams, String role);

    boolean isEligibleUser(String puid, DmAuthUserDO user, String label);

    DmApprovalTemplateDO lockAndValidateTemplate(String puid, String identity);

    static String serializeContext(Object value) {
        String json = JsonUtils.toJson(value);
        if (json.getBytes(StandardCharsets.UTF_8).length > MAX_CONTEXT_BYTES) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }
        return json;
    }
}

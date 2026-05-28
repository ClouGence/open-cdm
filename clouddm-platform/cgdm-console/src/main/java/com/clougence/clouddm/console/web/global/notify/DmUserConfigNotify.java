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
package com.clougence.clouddm.console.web.global.notify;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 17:11
 */
@Slf4j
@Service
public class DmUserConfigNotify implements RdpNotifyService {

    @Resource
    private WhiteListService whiteListService;

    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        for (UserConfigMO config : configList) {
            if (!whiteListService.checkUserConfigNumber(config.getConfig(), config.getNewValue())) {
                log.warn("User config value out of range, ownerUid={}, configKey={}, configValue={}", ownerUid, config.getConfig(), config.getNewValue());
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_VERIFICATION_ERROR.name(), config.getConfig()));
            }
        }
    }
}

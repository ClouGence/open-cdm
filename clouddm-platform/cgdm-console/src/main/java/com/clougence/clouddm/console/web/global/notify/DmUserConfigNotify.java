package com.clougence.clouddm.console.web.global.notify;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;

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

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
package com.clougence.clouddm.console.web.controller.kafka;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaAlterTopicConfigsFO;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaAlterTopicLayoutFO;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaCreateTopicFO;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaGroupIdFO;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaResetOffsetsFO;
import com.clougence.clouddm.console.web.model.fo.kafka.KafkaTopicNameFO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.kafka.KafkaOpsService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/kafka")
@Slf4j
public class KafkaOpsController {

    @Resource
    private KafkaOpsService kafkaOpsService;

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/createTopic", method = RequestMethod.POST)
    public ResWebData<?> createTopic(@Valid @RequestBody KafkaCreateTopicFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.createTopic(puid, fo.getDsId(), fo.getTopicName(), fo.getPartitions(), fo.getReplicationFactor(), fo.getConfigs());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/deleteTopic", method = RequestMethod.POST)
    public ResWebData<?> deleteTopic(@Valid @RequestBody KafkaTopicNameFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.deleteTopic(puid, fo.getDsId(), fo.getTopicName());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/describeTopicConfigs", method = RequestMethod.POST)
    public ResWebData<Map<String, String>> describeTopicConfigs(@Valid @RequestBody KafkaTopicNameFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        return ResWebDataUtils.buildSuccess(this.kafkaOpsService.describeTopicConfigs(puid, fo.getDsId(), fo.getTopicName()));
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/alterTopicConfigs", method = RequestMethod.POST)
    public ResWebData<?> alterTopicConfigs(@Valid @RequestBody KafkaAlterTopicConfigsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.alterTopicConfigs(puid, fo.getDsId(), fo.getTopicName(), fo.getConfigs());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/alterTopicLayout", method = RequestMethod.POST)
    public ResWebData<?> alterTopicLayout(@Valid @RequestBody KafkaAlterTopicLayoutFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.alterTopicLayout(puid, fo.getDsId(), fo.getTopicName(), fo.getPartitions(), fo.getReplicationFactor());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/deleteConsumerGroup", method = RequestMethod.POST)
    public ResWebData<?> deleteConsumerGroup(@Valid @RequestBody KafkaGroupIdFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.deleteConsumerGroup(puid, fo.getDsId(), fo.getGroupId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/resetConsumerGroupOffsets", method = RequestMethod.POST)
    public ResWebData<?> resetConsumerGroupOffsets(@Valid @RequestBody KafkaResetOffsetsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.kafkaOpsService.resetConsumerGroupOffsets(puid, fo.getDsId(), fo.getGroupId(), fo.getMode(), fo.getTopics(), fo.getTimestampMs(), fo.getOffset());
        return ResWebDataUtils.buildSuccess(true);
    }
}

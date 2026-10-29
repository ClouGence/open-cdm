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
package com.clougence.clouddm.console.web.service.ai;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineListFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineSaveFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineVersionFO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiModelVO;
import com.clougence.clouddm.platform.dal.mapper.ai.DmAiModelConfigMapper;
import com.clougence.clouddm.platform.dal.mapper.ai.DmAiProviderConfigMapper;
import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;
import com.clougence.clouddm.platform.dal.model.ai.DmAiModelConfigDO;
import com.clougence.clouddm.platform.dal.model.ai.DmAiProviderConfigDO;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;

import jakarta.annotation.Resource;
import okhttp3.HttpUrl;

@Service
@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class AiEngineService {
    @Resource
    private DmAiProviderConfigMapper providers;
    @Resource
    private DmAiModelConfigMapper    models;
    @Resource
    private DmAuthServiceForBiz      authorization;

    public DmPageVO<AiEngineVO> list(String puid, String uid, AiEngineListFO fo) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        PageObj page = fo.getPage();
        if (page.getPageNum() < 1 || page.getPageNum() > 10000 || page.getPageSize() < 1 || page.getPageSize() > 100) {
            throw AiError.INVALID_REQUEST.exception();
        }
        String keyword = fo.getKeyword();
        if (keyword != null) {
            keyword = keyword.trim();
        }
        List<String> codes = providers.findCodes();
        if (codes.isEmpty()) {
            return new DmPageVO<>(page.getPageNum(), page.getPageSize(), 0, List.of());
        }
        IPage<DmAiModelConfigDO> engines = models.findPageByProviderCodes(new Page<>(page.getPageNum(), page.getPageSize()), keyword, codes);
        List<AiEngineVO> records = assemble(engines.getRecords()).stream().map(this::toVO).toList();
        return new DmPageVO<>(engines.getCurrent(), engines.getSize(), engines.getTotal(), records);
    }

    public List<AiModelVO> models(String puid, String uid) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        List<String> codes = providers.findCodes();
        if (codes.isEmpty()) {
            return List.of();
        }
        return models.findByProviderCodes(codes)
            .stream()
            .map(model -> new AiModelVO(model.getModelCode(), model.getVersion(), model.getLabel(), model.getUpstreamModelName()))
            .toList();
    }

    public AiEngineConfigVO requireAvailable(String puid, String uid, String id, int version) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            throw AiError.FORBIDDEN.exception();
        }
        AiEngineConfigVO engine = find(id);
        if (engine == null || engine.getVersion() != version) {
            throw AiError.ENGINE_UNAVAILABLE.exception();
        }
        return engine;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AiEngineVO save(String puid, String uid, AiEngineSaveFO fo) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        if ((fo.getId() == null) != (fo.getVersion() == null)) {
            throw AiError.INVALID_REQUEST.exception();
        }
        HttpUrl parsedUrl = HttpUrl.parse(fo.getBaseUrl().strip());
        if (parsedUrl == null) {
            throw AiError.INVALID_REQUEST.exception();
        }
        String baseUrl = parsedUrl.toString().replaceAll("/+$", "");
        if (parsedUrl.encodedPath().replaceAll("/+$", "").endsWith("/chat/completions")) {
            throw AiError.INVALID_REQUEST.exception();
        }
        AiEngineConfigVO engine;
        boolean create = fo.getId() == null;
        if (create) {
            if (fo.getApiKey() == null || fo.getApiKey().isBlank()) {
                throw AiError.INVALID_REQUEST.exception();
            }
            engine = new AiEngineConfigVO();
            engine.setId(UUID.randomUUID().toString());
            engine.setProviderCode(UUID.randomUUID().toString());
            engine.setCreatedBy(uid);
            engine.setVersion(1);
        } else {
            engine = requireVersion(fo.getId(), fo.getVersion());
        }
        engine.setName(fo.getName().strip());
        engine.setPreset(fo.getPreset());
        engine.setModel(fo.getModel().strip());
        engine.setBaseUrl(baseUrl);
        engine.setUpdatedBy(uid);
        engine.setVerificationStatus(AiVerificationStatus.NOT_TESTED);
        boolean replaceApiKey = fo.getApiKey() != null && !fo.getApiKey().isEmpty();
        if (replaceApiKey) {
            if (fo.getApiKey().isBlank() || fo.getApiKey().chars().anyMatch(c -> c < 32 || c > 126)) {
                throw AiError.INVALID_REQUEST.exception();
            }
            engine.setAccessToken(fo.getApiKey());
        }
        Date now = new Date();
        try {
            if (create) {
                DmAiProviderConfigDO provider = new DmAiProviderConfigDO();
                provider.setProviderCode(engine.getProviderCode());
                provider.setDisplayName(engine.getName());
                provider.setAdapterType(engine.getPreset());
                provider.setBaseUrl(engine.getBaseUrl());
                provider.setAccessToken(engine.getAccessToken());
                provider.setGmtCreate(now);
                provider.setGmtModified(now);

                DmAiModelConfigDO model = new DmAiModelConfigDO();
                model.setModelCode(engine.getId());
                model.setProviderCode(provider.getProviderCode());
                model.setLabel(engine.getName());
                model.setUpstreamModelName(engine.getModel());
                model.setVersion(engine.getVersion());
                model.setVerificationStatus(engine.getVerificationStatus());
                model.setCreatedBy(engine.getCreatedBy());
                model.setUpdatedBy(engine.getUpdatedBy());
                model.setGmtCreate(now);
                model.setGmtModified(now);

                providers.insert(provider);
                models.insert(model);
            } else {
                if (models.updateByCodeAndVersion(engine.getId(), engine.getName(), engine.getModel(), engine.getUpdatedBy(), engine.getVersion() + 1, engine
                    .getVerificationStatus(), null, null, fo.getVersion(), now) != 1) {
                    throw AiError.ENGINE_VERSION_CONFLICT.exception();
                }
                providers.updateByCode(engine.getProviderCode(), engine.getName(), engine.getPreset(), engine.getBaseUrl(), engine.getAccessToken(), replaceApiKey, now);
            }
        } catch (DuplicateKeyException e) {
            throw AiError.ENGINE_NAME_MODEL_CONFLICT.exception();
        }
        return toVO(find(engine.getId()));
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void delete(String puid, String uid, AiEngineVersionFO fo) {
        if (!authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)) {
            throw AiError.FORBIDDEN.exception();
        }
        AiEngineConfigVO engine = requireVersion(fo.getId(), fo.getVersion());
        if (models.deleteByCodeAndVersion(fo.getId(), fo.getVersion()) != 1) {
            throw AiError.ENGINE_VERSION_CONFLICT.exception();
        }
        providers.deleteByCode(engine.getProviderCode());
    }

    public AiEngineConfigVO find(String id) {
        DmAiModelConfigDO model = models.findByCode(id);
        if (model == null) {
            return null;
        }
        List<AiEngineConfigVO> values = assemble(List.of(model));
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    private List<AiEngineConfigVO> assemble(List<DmAiModelConfigDO> values) {
        if (values.isEmpty()) {
            return List.of();
        }
        List<String> codes = values.stream().map(DmAiModelConfigDO::getProviderCode).distinct().toList();
        Map<String, DmAiProviderConfigDO> configs = providers.findByCodes(codes).stream().collect(Collectors.toMap(DmAiProviderConfigDO::getProviderCode, Function.identity()));
        List<AiEngineConfigVO> engines = new ArrayList<>();
        for (DmAiModelConfigDO model : values) {
            DmAiProviderConfigDO provider = configs.get(model.getProviderCode());
            if (provider == null) {
                continue;
            }
            AiEngineConfigVO engine = new AiEngineConfigVO();
            engine.setId(model.getModelCode());
            engine.setProviderCode(model.getProviderCode());
            engine.setName(model.getLabel());
            engine.setModel(model.getUpstreamModelName());
            engine.setVersion(model.getVersion());
            engine.setVerificationStatus(model.getVerificationStatus());
            engine.setVerifiedVersion(model.getVerifiedVersion());
            engine.setVerifiedAt(model.getVerifiedAt());
            engine.setCreatedBy(model.getCreatedBy());
            engine.setUpdatedBy(model.getUpdatedBy());
            engine.setGmtCreate(model.getGmtCreate());
            engine.setGmtModified(model.getGmtModified());
            engine.setPreset(provider.getAdapterType());
            engine.setBaseUrl(provider.getBaseUrl());
            engine.setAccessToken(provider.getAccessToken());
            engines.add(engine);
        }
        return engines;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public AiEngineConfigVO verify(String id, int version, AiVerificationStatus status) {
        if (models.updateVerificationByCodeAndVersion(id, version, status, new Date()) != 1) {
            throw AiError.ENGINE_VERSION_CONFLICT.exception();
        }
        AiEngineConfigVO current = find(id);
        if (current == null || current.getVersion() != version) {
            throw AiError.ENGINE_VERSION_CONFLICT.exception();
        }
        return current;
    }

    private AiEngineConfigVO requireVersion(String id, int version) {
        AiEngineConfigVO engine = find(id);
        if (engine == null) {
            throw AiError.NOT_FOUND.exception();
        }
        if (engine.getVersion() != version) {
            throw AiError.ENGINE_VERSION_CONFLICT.exception();
        }
        return engine;
    }

    private AiEngineVO toVO(AiEngineConfigVO engine) {
        Instant verifiedAt = null;
        if (engine.getVerifiedAt() != null) {
            verifiedAt = engine.getVerifiedAt().toInstant();
        }
        return new AiEngineVO(engine.getId(),
            engine.getName(),
            engine.getPreset(),
            engine.getModel(),
            engine.getBaseUrl(),
            engine.getVersion(),
            engine.getVerificationStatus(),
            engine.getVerifiedVersion(),
            verifiedAt,
            engine.getAccessToken() != null,
            engine.getGmtCreate().toInstant(),
            engine.getGmtModified().toInstant());
    }
}

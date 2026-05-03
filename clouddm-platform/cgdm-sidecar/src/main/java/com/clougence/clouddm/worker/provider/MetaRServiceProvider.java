package com.clougence.clouddm.worker.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.execute.MetaRService;
import com.clougence.clouddm.api.sidecar.session.execute.TestConnectResultDO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsClassify;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.worker.component.resource.OnlineDsResourceManager;
import com.clougence.clouddm.worker.component.session.SessionAgent;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class MetaRServiceProvider implements MetaRService {

    @Resource
    private OnlineDsResourceManager onlineRM;
    @Resource
    private SessionManager          sessionManager;

    protected String genSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    protected SessionContextDTO context(DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        Map<String, Object> params = new HashMap<>();
        if (levelsParam != null) {
            if (levelsParam.get(UmiTypes.Catalog) != null) {
                params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
            }
            params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        }

        SessionSpi spi = PluginManager.findSessionSpi(dbConfig.getDataSourceType());
        return spi.createSessionContext(dbConfig, params);
    }

    protected SessionAgent metaSession(DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        SessionContextDTO contextDTO = context(dbConfig, levelsParam);
        contextDTO.setSessionId(genSessionId());
        return this.sessionManager.createSession(this.onlineRM, dbConfig, contextDTO);
    }

    @Override
    public TestConnectResultDO testConnect(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        try {
            try (SessionAgent rdbSession = metaSession(dbConfig, levelsParam)) {
                rdbSession.getMetaService().testConnect();
                return new TestConnectResultDO(true, "OK");
            }
        } catch (Exception e) {
            Throwable rootErr = ExceptionUtils.getRootCause(e);
            String msg = ExceptionUtils.getRootCauseMessage(rootErr);
            log.error("test datasource failed,host:" + dbConfig.toString() + ",msg:" + msg, rootErr);
            return new TestConnectResultDO(false, rootErr.getMessage());
        }
    }

    @Override
    public String getVersion(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam) {
        try {
            try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
                return rdbSession.getMetaService().getVersion();
            }
        } catch (Exception e) {
            String msg = "getVersion error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<DsElement> listLevels(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        try (Session session = metaSession(dbConfig, levelsParam)) {
            return session.getMetaService().listLevels(levels, levelsParam);
        } catch (Exception e) {
            String msg = "listLevels error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public DsElement detailLevel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        Map<UmiTypes, Object> detailParam = new HashMap<>(levelsParam);
        if (dbConfig.getDataSourceType().getDsClassify() == DsClassify.RDB) {
            if (levels.size() == 1 && levels.contains(UmiTypes.Schema)) {
                levelsParam.remove(UmiTypes.Schema);
            }
        }

        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().detailLevel(levels, detailParam);
        } catch (Exception e) {
            String msg = "detailLevel error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<DsElement> listLeaf(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) {
        try (Session session = metaSession(dbConfig, levelsParam)) {
            return session.getMetaService().listLeaf(levelsParam, leafType, pattern);
        } catch (Exception e) {
            String msg = "listLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Value detailLeaf(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().detailLeaf(levelsParam, leafType, leafName);
        } catch (Exception e) {
            String msg = "detailLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Value fetchSelectObject(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, String leafName) {
        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().fetchSelectObject(levelsParam, leafName);
        } catch (Exception e) {
            String msg = "detailLeaf error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    //
    // RDB
    //

    @Override
    public Map<String, List<RdbColumn>> loadColumns(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType,
                                                    List<String> leafNames) {
        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().batchColumns(levelsParam, leafType, leafNames);
        } catch (Exception e) {
            String msg = "loadColumns error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String loadTableEditor(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, String table) {
        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().loadTableEditor(levelsParam, table);
        } catch (Exception e) {
            String msg = "loadTableEditor error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public List<String> requestObjectScript(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try (Session rdbSession = metaSession(dbConfig, levelsParam)) {
            return rdbSession.getMetaService().requestObjectScript(levelsParam, leafType, leafName);
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}

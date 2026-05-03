package com.clougence.clouddm.console.web.controller.editor.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.model.fo.editor.query.QueryResultFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.*;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryApi;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.Sm2Utils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/query")
@Slf4j
public class TestController implements UnifiedPostConstruct {

    private final Map<String, List<WsResMsg>> map      = new ConcurrentHashMap<>();

    private final Map<String, Boolean>        checkMap = new ConcurrentHashMap<>();

    @Resource
    private RdpConsoleConfig                  rdbConfig;

    @Resource
    private ConsoleQueryApi                   queryServiceApi;

    @Value("${editor.query.test:false}")
    private boolean                           openTest;

    @Override
    public void init() throws Exception {
        if (!openTest) {
            return;
        }

        DmGlobalEventBus.addQueryResultEventListen(wsResMsg -> {
            if (!this.checkMap.containsKey(wsResMsg.getSessionId())) {
                return;
            }

            WsResultType resultType = wsResMsg.getResultType();
            synchronized (this) {
                switch (resultType) {
                    case Message: {
                        WsInfoResMsg wsInfoResMsg = (WsInfoResMsg) wsResMsg;
                        for (WsInfoEntity entity : wsInfoResMsg.getEntities()) {
                            if (entity.getMode() == MessageMode.Console) {
                                List<WsResMsg> list = map.getOrDefault(wsResMsg.getSessionId(), new ArrayList<>());
                                list.add(wsResMsg);
                                map.put(wsResMsg.getSessionId(), list);
                                break;
                            }
                        }
                        break;
                    }
                    case ResultSet:
                    case RuleCheck:
                    case Status: {
                        List<WsResMsg> list = map.getOrDefault(wsResMsg.getSessionId(), new ArrayList<>());
                        list.add(wsResMsg);
                        map.put(wsResMsg.getSessionId(), list);
                        break;
                    }
                    case Done: {
                        checkMap.put(wsResMsg.getSessionId(), true);
                        break;
                    }
                }
            }

        });
    }

    @Override
    public void stop() {

    }

    @RequestMapping(value = "/doQuery", method = RequestMethod.POST)
    public ResWebData<?> doQuery(@RequestBody WsQueryFO wsQueryFO, HttpServletRequest request) {
        if (!openTest) {
            throw new UnsupportedOperationException();
        }
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        wsQueryFO.setCurrentUserId(uid);
        wsQueryFO.setPrimaryUserId(puid);
        wsQueryFO.setChannelKey(UUID.randomUUID().toString());

        checkMap.put(wsQueryFO.getSessionId(), false);

        this.queryServiceApi.offerQueryRequest(wsQueryFO, DmGlobalEventBus::triggerQueryResultEvent);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestMapping(value = "/queryResults", method = RequestMethod.POST)
    public ResWebData<?> queryResults(@RequestBody QueryResultFO fo, HttpServletRequest request) {
        if (!openTest) {
            throw new UnsupportedOperationException();
        }
        QueryResultVO queryResultVO = new QueryResultVO();
        String sessionId = fo.getSessionId();
        if (checkMap.get(sessionId) == null || !checkMap.get(sessionId)) {
            throw new ErrorMessageException("query is running");
        }
        checkMap.remove(sessionId);
        List<WsResMsg> wsResMsgs = map.remove(sessionId);

        for (WsResMsg wsResMsg : wsResMsgs) {
            if (wsResMsg instanceof WsInfoResMsg) {
                WsInfoResMsg wsInfoResMsg = (WsInfoResMsg) wsResMsg;
                for (WsInfoEntity entity : wsInfoResMsg.getEntities()) {
                    if (entity.getMode() == MessageMode.Console) {
                        queryResultVO.getWsInfoResMsgList().add(wsInfoResMsg);
                    }
                }
            } else if (wsResMsg instanceof WsResultSetResMsg) {
                WsResultSetResMsg wsResultSetResMsg = (WsResultSetResMsg) wsResMsg;
                queryResultVO.getResultSetResMsgList().add(wsResultSetResMsg);
            } else if (wsResMsg instanceof WsStatusResMsg) {
                WsStatusResMsg wsStatusResMsg = (WsStatusResMsg) wsResMsg;
                queryResultVO.getStatusResMsgList().add(wsStatusResMsg);
            } else if (wsResMsg instanceof WsRuleResMsg) {
                WsRuleResMsg wsRuleResMsg = (WsRuleResMsg) wsResMsg;
                queryResultVO.getWsRuleResMsgList().add(wsRuleResMsg);
            }
        }
        return ResWebDataUtils.buildSuccess(queryResultVO);
    }

    @RequestMapping(value = "/checkQuery", method = RequestMethod.POST)
    public ResWebData<?> checkQuery(@RequestBody QueryResultFO fo, HttpServletRequest request) {
        if (!openTest) {
            throw new UnsupportedOperationException();
        }
        return ResWebDataUtils.buildSuccess(checkMap.get(fo.getSessionId()));
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public ResWebData<?> encrypt(String password) {
        if (!openTest) {
            throw new UnsupportedOperationException();
        }

        String encryptPassword = Sm2Utils.encrypt(rdbConfig.getPublicKey(), password);

        return ResWebDataUtils.buildSuccess(encryptPassword);
    }

}

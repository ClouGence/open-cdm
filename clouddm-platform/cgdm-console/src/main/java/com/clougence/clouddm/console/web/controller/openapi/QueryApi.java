package com.clougence.clouddm.console.web.controller.openapi;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsQueryFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.*;
import com.clougence.clouddm.console.web.model.vo.openapi.DmApiQueryResultVO;
import com.clougence.clouddm.console.web.service.editor.DsQueryEditorService;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryApi;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.rdp.component.mcp.McpApiProvider;
import com.clougence.rdp.component.mcp.model.McpTool;
import com.clougence.rdp.component.openapi.OpenApiSessionManager;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

import lombok.extern.slf4j.Slf4j;

@McpApiProvider
@RestController
@RequestMapping(value = DmControllerUrlPrefix.OPEN_API_PREFIX + "/query")
@Slf4j
public class QueryApi {

    @Resource
    private DsQueryEditorService queryEditorService;

    @Resource
    private ConsoleQueryApi      consoleQueryApi;

    @McpTool(value = DmMcpI18nKey.M_EXECUTE_QUERY)
    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/syncQuery", method = RequestMethod.POST)
    public ResWebData<DmApiQueryResultVO> syncQuery(@Valid @RequestBody DmApiDsQueryFO fo, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("syncQuery for open api request id :" + requestId);

        // prepare query
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        switch (fo.getQueryType()) {
            case RequestQuery:
            case RequestPlan:
                break;
            default:
                throw new IllegalArgumentException("only support RequestQuery or RequestPlan, but: " + fo.getQueryType());
        }

        WsQueryFO queryFO = DmConvertUtils.convertToWsQueryFO(fo, getHost(request));
        queryFO.setCurrentUserId(uid);
        queryFO.setPrimaryUserId(puid);

        String sessionId = this.queryEditorService.createSession(uid, queryFO.getLevels(), queryFO.isRdbAutoCommit(), queryFO.getRdbIsolation());
        queryFO.setSessionId(sessionId);

        // do query
        final CgFuture<DmApiQueryResultVO> future = new CgFutureObj<>();
        final DmApiQueryResultVO resultData = new DmApiQueryResultVO();
        this.consoleQueryApi.offerQueryRequest(queryFO, msg -> applyMessageResult(queryFO, msg, resultData, future));

        // wait result
        int DEFAULT_QUERY_TIMEOUT_SEC = 54;
        try {
            DmApiQueryResultVO r = future.get(DEFAULT_QUERY_TIMEOUT_SEC, TimeUnit.SECONDS);
            return ResWebDataUtils.buildSuccess(r);
        } catch (TimeoutException e) {
            return ResWebDataUtils.buildError("Query timeout(" + DEFAULT_QUERY_TIMEOUT_SEC + " sec) in CloudDM");
        } catch (InterruptedException e) {
            return ResWebDataUtils.buildError("Query interrupted in CloudDM");
        } catch (Exception e) {
            Throwable root = e;
            if (e instanceof ExecutionException) {
                root = e.getCause();
            }
            String msg = "Error convert data.cause:" + ExceptionUtils.getRootCauseMessage(root);
            log.error(msg, root);

            StringWriter sw = new StringWriter();
            root.printStackTrace(new PrintWriter(sw));
            return ResWebDataUtils.buildError("Query Error in CloudDM, Message:" + sw);
        }
    }

    private String getHost(HttpServletRequest request) {
        //aliyun  slb
        String host = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(host)) {
            return host;
        }

        return request.getRemoteHost();
    }

    // merge and apply message to result
    private void applyMessageResult(WsQueryFO queryFO, WsResMsg msg, DmApiQueryResultVO vo, CgFuture<DmApiQueryResultVO> future) {
        if (future.isDone()) {
            return;
        }

        switch (msg.getResultType()) {
            case QueryScript: {
                WsQueryInfoMsg m = (WsQueryInfoMsg) msg;
                vo.setScript(m.getScript());
                vo.setOriginal(m.getOriginal());
                vo.setRewriteTags(m.getRewriteTags());
                break;
            }
            case RuleCheck: {
                WsRuleResMsg m = (WsRuleResMsg) msg;
                vo.setRuleCheckLevel(m.getWarnLevel());
                vo.setRuleCheckList(m.getMessage());
                break;
            }
            case ResultSetMeta: {
                WsResultSetMetaMsg m = (WsResultSetMetaMsg) msg;
                vo.setResultId(m.getResultId());
                vo.setColumnList(m.getColumnList());
                vo.setColumnType(m.getColumnType());
                vo.setReceiveMode(m.getReceiveMode());
                vo.setCacheFile(m.getCacheFile());
                vo.setReceiveCost(m.getReceiveCost());
                break;
            }
            case ResultSet: {
                WsResultSetResMsg m = (WsResultSetResMsg) msg;
                vo.setReceiveCost(m.getReceiveCost());
                vo.setFetchCount(m.getFetchCount());
                if (vo.getRows() == null) {
                    vo.setRows(new ArrayList<>());
                }
                vo.getRows().addAll(m.getRowSet());
                break;
            }
            case ResultSetRows: {
                WsResultSetRowsMsg m = (WsResultSetRowsMsg) msg;
                vo.setReceiveCost(m.getReceiveCost());
                vo.setFetchCount(m.getFetchCount());
                break;
            }
            case Message: {
                WsInfoResMsg m = (WsInfoResMsg) msg;
                if (vo.getMsgList() == null) {
                    vo.setMsgList(new ArrayList<>());
                }
                for (WsInfoEntity entity : m.getEntities()) {
                    if (entity.getMode() == MessageMode.Console) {
                        vo.getMsgList().add(m);
                    }
                }
                break;
            }
            case CancelDone:
            case Done: {
                future.completed(vo);
                break;
            }
            default:
                break;
        }
    }
}

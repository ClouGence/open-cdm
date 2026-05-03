package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import org.bson.Document;

import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MongoUtils {

    protected static final JdbcColumn       RESULT_COLUMN = new JdbcColumn("RESULT", AdapterType.String, "", "", "");
    protected static final List<JdbcColumn> RESULT        = Collections.singletonList(RESULT_COLUMN);

    public static CgFuture<?> completed(CgFuture<Object> sync) {
        sync.completed(true);
        return sync;
    }

    public static CgFuture<?> failed(CgFuture<Object> sync, Exception e) {
        sync.failed(e);
        return sync;
    }

    public static <T> Map<String, T> singletonMap(String column, T keyCol) {
        Map<String, T> dataMap = new LinkedHashMap<>();
        dataMap.put(column, keyCol);
        return dataMap;
    }

    public static AdapterResultCursor singleResult(AdapterRequest request, JdbcColumn col, Object value) throws SQLException {
        AdapterResultCursor result = new AdapterResultCursor(request, Collections.singletonList(col));
        result.pushData(singletonMap(col.name, value));
        result.pushFinish();
        return result;
    }

    public static AdapterResultCursor twoResult(AdapterRequest request, JdbcColumn firstCol, Object firstValue, JdbcColumn secondCol, Object secondValue) throws SQLException {
        AdapterResultCursor result = new AdapterResultCursor(request, Arrays.asList(firstCol, secondCol));
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put(firstCol.name, firstValue);
        dataMap.put(secondCol.name, secondValue);
        result.pushData(dataMap);
        result.pushFinish();
        return result;
    }

    public static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn col, Collection<?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Collections.singletonList(col));
        int affectRows = 0;
        for (Object item : result) {
            receiveCur.pushData(CollectionUtils.asMap(col.name, item));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn keyCol, JdbcColumn valueCol, Map<?, ?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(keyCol, valueCol));
        int affectRows = 0;
        for (Map.Entry<?, ?> item : result.entrySet()) {
            receiveCur.pushData(CollectionUtils.asMap(  //
                    keyCol.name, item.getKey(),         //
                    valueCol.name, item.getValue()      //
            ));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn fixedCol, Object fixedColValue,//
                                                 JdbcColumn keyCol, JdbcColumn valueCol, Map<?, ?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(fixedCol, keyCol, valueCol));
        int affectRows = 0;
        for (Map.Entry<?, ?> item : result.entrySet()) {
            receiveCur.pushData(CollectionUtils.asMap(  //
                    fixedCol.name, fixedColValue,       //
                    keyCol.name, item.getKey(),         //
                    valueCol.name, item.getValue()      //
            ));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static AdapterResultCursor listFixedColAndResult(AdapterRequest request, JdbcColumn fixedCol, Object fixedColValue,//
                                                            JdbcColumn col, Collection<?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(fixedCol, col));
        int affectRows = 0;
        for (Object item : result) {
            receiveCur.pushData(CollectionUtils.asMap(fixedCol.name, fixedColValue, col.name, item));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    protected static CgFuture<?> handleResult(CgFuture<Object> sync, AdapterRequest request, AdapterReceive receive, MongoResultBuffer buffer,
                                              Iterator<Document> it) throws SQLException, IOException {
        Set<String> keySet = new LinkedHashSet<>();

        long maxRows = request.getMaxRows();
        int affectRows = 0;
        while (it.hasNext()) {
            Document document = it.next();
            buffer.add(document);
            keySet.addAll(document.keySet());

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        buffer.finish();

        List<JdbcColumn> columns = new ArrayList<>();
        for (String key : keySet) {
            columns.add(new JdbcColumn(key, AdapterType.String, "", "", ""));
        }

        MongoResultCursor cursor = new MongoResultCursor(request, columns,buffer);

        receive.responseResult(request, cursor);
        return completed(sync);
    }

}

package com.clougence.clouddm.sdk.execute.session;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;

public interface ResultBuilder {

    String newResultId(QueryRequest query);

    PhaseBuild newPhase(QueryRequest query);

    PhaseBuild newPhase(QueryRequest query, String resultId);

    ResultSetMetaBuild newResultMeta(QueryRequest query, String resultId);

    ResultCountBuild newResultCount(QueryRequest query);

    ResultOutputBuild newOutput(QueryRequest query);

    ResultMessageBuild newMessage(QueryRequest query);

    void finished();

    interface ResultBuild {

        void collectCost(long costTimeMs);

        void finishRecord(boolean success);

        void finishRecord(boolean success, String message, Throwable e);
    }

    interface PhaseBuild extends ResultBuild {

        void onPhase(ResultPhaseType type);
    }

    interface ResultSetMetaBuild extends ResultBuild {

        ResultSetRowsBuild receiveMeta(Map<String, ResultColMeta> rowMeta) throws SQLException, IOException;
    }

    interface ResultSetRowsBuild extends ResultBuild {

        void collectMetric(int fetchCount);

        void receiveRow(boolean silent, ResultSet rs) throws SQLException, IOException;

        void receiveRow(boolean silent, Map<String, Object> rs) throws SQLException, IOException;

        long dataSize();

        long expansionSize();

        boolean fetcherOverflow();

        void finishAndContinue();

        void finishAndSilent(boolean success);

        ResultSetRowCountUpdateBuild newRowCountUpdate();

        void flushData();
    }

    interface ResultSetRowCountUpdateBuild extends ResultBuild {

        void collectMetric(int fetchCount);
    }

    interface ResultCountBuild extends ResultBuild {

        void receiveUpdateCount(long updateCount);

        void receiveGeneratedKey(Map<String, String> generatedData);
    }

    interface ResultOutputBuild extends ResultBuild {

        void receiveOutParam(QueryArg paramDef, String value);
    }

    interface ResultMessageBuild extends ResultBuild {

        void receiveMessage(MessageLevel level, String message);

        void receiveMessage(MessageLevel level, String message, boolean notify);
    }
}

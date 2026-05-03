package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import org.bson.Document;

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;

public class ClientCallForCommand extends MongoUtils {

    public static CgFuture<?> runCommand(CgFuture<Object> sync, MongoClient client, ClientSession session, String command, AdapterRequest request, AdapterReceive receive,
                                         String database) throws SQLException, IOException {
        Document result = client.getDatabase(database).runCommand(session, Document.parse(command));

        return handleResult(sync, request, receive, new MongoResultBuffer(), Collections.singletonList(result).iterator());
    }

}

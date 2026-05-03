package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import org.bson.Document;

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ClientCallForDatabase extends MongoUtils {

    public static CgFuture<?> listCollections(CgFuture<Object> sync, MongoClient client, ClientSession session, String command, AdapterRequest request, AdapterReceive receive,
                                              String database) throws SQLException, IOException {
        MongoDatabase database1 = client.getDatabase(database);
        ClientSession clientSession = client.startSession();
        Document result = database1.runCommand(session, Document.parse(command));

        return handleResult(sync, request, receive, new MongoResultBuffer(), new CursorResult(result, database1, clientSession).iterator());
    }
}

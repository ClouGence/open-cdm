package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import org.bson.Document;

import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;

public class ClientCallForShow extends MongoUtils {

    public static CgFuture<?> showDatabasesFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, AdapterRequest request,
                                                AdapterReceive receive) throws SQLException, IOException {
        ListDatabasesIterable<Document> documents = client.listDatabases(session);

        return handleResult(sync, request, receive, new MongoResultBuffer(), documents.iterator());
    }

    public static CgFuture<?> showCollectionsFunc(CgFuture<Object> sync, MongoClient client, ClientSession session, AdapterRequest request, AdapterReceive receive,
                                                  String database) throws SQLException, IOException {
        ListCollectionsIterable<Document> documents = client.getDatabase(database).listCollections(session);

        return handleResult(sync, request, receive, new MongoResultBuffer(), documents.iterator());
    }
}

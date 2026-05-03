package com.clougence.drivers.adapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdapterConnManager {

    private static final Map<String, AdapterConnection> connectionMap = new ConcurrentHashMap<>();

    static void newConnection(AdapterConnection connection) {
        connectionMap.put(connection.getObjectId(), connection);
    }

    static void removeConnection(AdapterConnection connection) {
        connectionMap.remove(connection.getObjectId());
    }

    public static AdapterConnection getConnection(String objectId) {
        return connectionMap.get(objectId);
    }
}

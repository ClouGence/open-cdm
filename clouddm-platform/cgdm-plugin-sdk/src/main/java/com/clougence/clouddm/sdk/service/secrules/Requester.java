package com.clougence.clouddm.sdk.service.secrules;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public enum Requester {

    CONSOLE,
    TICKET,
    CHANGE;

    public boolean isOnline() { return this == CONSOLE; }

    public boolean isTask() { return this != CONSOLE; }
}

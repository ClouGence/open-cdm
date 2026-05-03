package com.clougence.clouddm.faker.engine;

import lombok.Getter;

@Getter
class ErrorMessage {

    private final String messageKey;
    private long         timestamp;

    public ErrorMessage(String messageKey){
        this.messageKey = messageKey;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        return this.messageKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.messageKey.equals(obj.toString());
    }

    @Override
    public String toString() {
        return this.messageKey;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }
}

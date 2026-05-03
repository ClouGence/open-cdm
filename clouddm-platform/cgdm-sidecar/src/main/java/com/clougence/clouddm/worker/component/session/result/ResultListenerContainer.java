package com.clougence.clouddm.worker.component.session.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultListenerContainer {

    private final List<String>                order;
    private final List<String>                disabled;
    private final Map<String, ResultListener> listeners;

    public ResultListenerContainer(){
        this.order = new ArrayList<>();
        this.disabled = new ArrayList<>();
        this.listeners = new HashMap<>();
    }

    public void disabledAll() {
        this.disabled.addAll(this.order);
    }

    public void enableAll() {
        this.disabled.clear();
    }

    public void disabledListener(String name) {
        this.disabled.add(name);
    }

    public void enableListener(String name) {
        this.disabled.remove(name);
    }

    public void addListener(String name, ResultListener listener) {
        this.order.add(name);
        this.listeners.put(name, listener);
    }

    public void doListeners(QueryRequest query, Result result) {
        for (String name : this.order) {
            if (!this.disabled.contains(name)) {
                ResultListener listener = this.listeners.get(name);
                if (listener != null) {
                    listener.onResult(query, result);
                }
            }
        }
    }

}

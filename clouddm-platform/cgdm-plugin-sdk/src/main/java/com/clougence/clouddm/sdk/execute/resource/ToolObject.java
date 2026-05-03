package com.clougence.clouddm.sdk.execute.resource;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.tools.Tool;
import com.clougence.drivers.CloseEventListener;

public class ToolObject implements Closeable {

    private final static Logger            logger    = Logger.getLogger(ToolObject.class.getName());
    private final ToolConfig               toolsConfig;
    private Tool                           target;
    private final List<CloseEventListener> listeners = new ArrayList<>();
    protected final AtomicBoolean          closed    = new AtomicBoolean(false);

    public ToolObject(ToolConfig toolsConfig, Tool target){
        this.toolsConfig = toolsConfig;
        this.target = target;
    }

    public ToolConfig getDsConfig() { return toolsConfig; }

    // get DataSource Object when available, (after close,return is null)
    public Tool getTarget() {
        if (this.closed.get()) {
            throw new RuntimeException("ToolObject instance '" + this.toolsConfig.getToolName() + "' has been closed");
        } else {
            return this.target;
        }
    }

    public boolean isClose() { return this.closed.get(); }

    // force DataSource to close
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            try {
                target.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "target.close " + e.getMessage(), e);
            }

            this.listeners.forEach(listener -> {
                try {
                    listener.onClose();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "listener.onClose " + e.getMessage(), e);
                }
            });
            this.target = null;
        }
    }

    public void addCloseListener(CloseEventListener listener) {
        this.listeners.add(listener);
    }
}

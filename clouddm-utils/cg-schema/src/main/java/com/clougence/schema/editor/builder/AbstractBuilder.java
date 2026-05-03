package com.clougence.schema.editor.builder;

import com.clougence.schema.editor.TableEditor.Builder;

/**
 * @author mode 2021/5/21 19:56
 */
public abstract class AbstractBuilder implements Builder {

    private boolean finish;

    public AbstractBuilder(){
        this.finish = false;
    }

    @Override
    public boolean isFinish() { return this.finish; }

    @Override
    public void finish() {
        if (finish) {
            throw new IllegalStateException("already finish.");
        }
        this.finish = true;
    }

    protected abstract void doChanges();
}

package com.clougence.schema.editor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
public abstract class EChange<T> implements Cloneable {

    @JsonIgnore
    private transient T initData;

    public void initDomain() {
        this.initData = this.clone();
    }

    public abstract T clone();

    public final boolean hasChanged() {
        return this.testChanged(this.initData);
    }

    abstract boolean testChanged(T initData);
}

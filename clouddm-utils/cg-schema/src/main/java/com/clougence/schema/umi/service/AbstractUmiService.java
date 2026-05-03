package com.clougence.schema.umi.service;

import com.clougence.utils.function.ESupplier;

/**
 * mysql DsSchemaRService
 *
 * @author mode 2021/1/8 19:56
 */
public abstract class AbstractUmiService<T, E extends Throwable> implements UmiService {

    protected final ESupplier<T, E> metadataSupplier;

    public AbstractUmiService(ESupplier<T, E> metadataSupplier){
        this.metadataSupplier = metadataSupplier;
    }
}

package com.clougence.clouddm.console.web.service.browse;

import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;

public interface MetaInformatinCacheService {

    void putListCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String context);

    void putDetailCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String objName, String context);

    String getListCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type);

    String getDetailCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String objName);
}

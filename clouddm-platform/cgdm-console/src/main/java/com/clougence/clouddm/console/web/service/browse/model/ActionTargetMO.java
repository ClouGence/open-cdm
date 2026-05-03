package com.clougence.clouddm.console.web.service.browse.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-05-16 14:20
 */
@Getter
@Setter
public class ActionTargetMO {

    private GenerateSqlDataAuthEnum actionType;

    private String                  targetType;

    private String                  targetName;

    private String                  targetNewName;

    private String                  targetExactName;

    private Map<String, Object>     options;
}

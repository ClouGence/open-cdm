package com.clougence.clouddm.console.web.component.project.model;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCheckMO {

    private String                  content;
    private SecQueryKind contentKind;
    private List<ChangeCheckItemMO> checkList;
    private WarnLevel               level;

    // source code
    private int                     startCodeLine;
    private int                     startCodeColumn;
    private int                     endCodeLine;
    private int                     endCodeColumn;
}

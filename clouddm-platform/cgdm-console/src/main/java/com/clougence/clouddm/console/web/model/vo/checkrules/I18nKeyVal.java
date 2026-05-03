package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class I18nKeyVal {

    private final String     i18n;
    private final String     name;
    private final boolean    choose;
    private boolean          hidden;
    private List<I18nKeyVal> children;

    public I18nKeyVal(String name, String i18n){
        this.name = name;
        this.i18n = i18n;
        this.choose = false;
        this.hidden = false;
    }

    public I18nKeyVal(String name, String i18n, boolean choose){
        this.name = name;
        this.i18n = i18n;
        this.choose = choose;
        this.hidden = false;
    }

    public I18nKeyVal(String name, String i18n, boolean choose, boolean hidden){
        this.name = name;
        this.i18n = i18n;
        this.choose = choose;
        this.hidden = hidden;
    }
}

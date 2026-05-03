package com.clougence.schema.umi.struts;

import java.util.HashSet;
import java.util.Set;

/**
 * Keys
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractAttributeNames implements UmiAttributeNames {

    private final String space;
    private final String name;

    private AbstractAttributeNames(String name){
        this("", name);
    }

    protected AbstractAttributeNames(String space, String name){
        this.space = space;
        this.name = name;
    }

    public String getCodeKey() { return this.space + "_" + this.name; }

    private static final Set<String> checkNames = new HashSet<>();

    protected static synchronized <T extends UmiAttributeNames> T check(T attr) {
        if (checkNames.contains(attr.getCodeKey())) {
            //throw new IllegalStateException("attr " + attr.getCodeKey() + " duplicate.");
        }
        checkNames.add(attr.getCodeKey());
        return attr;
    }

}

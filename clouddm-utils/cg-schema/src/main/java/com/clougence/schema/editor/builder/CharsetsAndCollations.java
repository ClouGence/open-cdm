package com.clougence.schema.editor.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author barry
 * @date 2022/7/27 上午11:11:16
 * @description
 */
@Getter
@Setter
public class CharsetsAndCollations {

    private Map<String, String> data = new HashMap<>();

    public void add(String collationInfo, String charsetInfo) {
        data.put(collationInfo, charsetInfo);
    }

    public Set<String> collations() {
        return data.keySet();
    }

    public Collection<String> charsets() {
        return data.values();
    }

    public boolean containsCollation(String collation) {
        return this.collations().contains(collation);
    }

    public boolean containsCharset(String charset) {
        return this.charsets().contains(charset);
    }

    public List<String> findCollationsFromCharset(String charset) {
        List<String> re = new ArrayList<>();
        for (Map.Entry<String, String> e : data.entrySet()) {
            if (e.getValue().equals(charset)) {
                re.add(e.getKey());
            }
        }

        return re;
    }
}

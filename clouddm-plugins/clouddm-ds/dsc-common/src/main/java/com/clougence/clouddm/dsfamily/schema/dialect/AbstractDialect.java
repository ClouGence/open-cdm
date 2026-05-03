package com.clougence.clouddm.dsfamily.schema.dialect;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.clougence.schema.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

/**
 * 公共 SqlDialect 实现
 *
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractDialect implements Dialect {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDialect.class);
    private Set<String>         firstCharKeywords;
    private char[]              containsCharKeywords;
    private Set<String>         keyWords;

    @Override
    public final Set<String> keywords() {
        if (this.keyWords == null) {
            synchronized (this) {
                if (this.keyWords != null) {
                    return this.keyWords;
                }
                Set<String> keyWords = new HashSet<>();
                String keyWordsResource = keyWordsResource();
                if (StringUtils.isNotBlank(keyWordsResource)) {
                    try {
                        InputStream inputStream = ResourcesUtils.getResourceAsStream(keyWordsResource);
                        if (inputStream != null) {
                            List<String> strings = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
                            for (String term : strings) {
                                term = term.trim().toUpperCase();
                                if (!StringUtils.isBlank(term) && term.charAt(0) != '#') {
                                    keyWords.add(term);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("load {}.keywords failed.{}", keyWordsResource, e.getMessage(), e);
                    }
                }
                this.keyWords = keyWords;
            }
        }
        return this.keyWords;
    }

    protected final Set<String> firstCharKeywords() {
        if (this.firstCharKeywords == null) {
            synchronized (this) {
                if (this.firstCharKeywords != null) {
                    return this.firstCharKeywords;
                }
                Set<String> firstCharKeywords = new HashSet<>();
                firstCharKeywords.addAll(Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "=", "+", "~", "`", " "));
                firstCharKeywords.addAll(Arrays.asList("{", "}", "[", "]", "\\", "|", ";", ":", "\"", "'", ",", "<", ".", ">", "/", "?"));
                firstCharKeywords.addAll(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
                this.firstCharKeywords = firstCharKeywords;
            }
        }
        return this.firstCharKeywords;
    }

    protected final char[] containsCharKeywords() {
        if (this.containsCharKeywords == null) {
            synchronized (this) {
                if (this.containsCharKeywords != null) {
                    return this.containsCharKeywords;
                }
                List<String> firstCharKeywords = new ArrayList<>();
                firstCharKeywords.addAll(Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "=", "+", "~", "`", " "));
                firstCharKeywords.addAll(Arrays.asList("{", "}", "[", "]", "\\", "|", ";", ":", "\"", "'", ",", "<", ".", ">", "/", "?"));

                char[] result = new char[firstCharKeywords.size()];
                for (int i = 0; i < firstCharKeywords.size(); i++) {
                    result[i] = firstCharKeywords.get(i).charAt(0);
                }
                this.containsCharKeywords = result;
            }
        }
        return this.containsCharKeywords;
    }

    protected abstract String keyWordsResource();

    public String fmtName(boolean useQualifier, String fmtString) {
        if (fmtString == null) {
            return null;
        }
        if (this.keywords().contains(fmtString.toUpperCase()) || fmtString.contains(" ")) {
            useQualifier = true;
        }
        if (!useQualifier && !fmtString.isEmpty()) {
            useQualifier = firstCharKeywords().contains(String.valueOf(fmtString.charAt(0)));
        }
        if (!useQualifier && !fmtString.isEmpty()) {
            useQualifier = StringUtils.containsAny(fmtString, containsCharKeywords());
        }

        String leftQualifier = useQualifier ? leftQualifier() : "";
        String rightQualifier = useQualifier ? rightQualifier() : "";
        return leftQualifier + fmtNameValue(useQualifier, fmtString) + rightQualifier;
    }

    public String fmtValue(String value) {
        return value;
    }

    protected String fmtNameValue(boolean useQualifier, String fmtString) {
        return fmtString;
    }

    @Override
    public String fmtTableName(boolean useDelimited, String catalog, String schema, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(catalog)) {
            sqlBuilder.append(fmtName(useDelimited, catalog));
            sqlBuilder.append(".");
        }
        if (StringUtils.isNotBlank(schema)) {
            sqlBuilder.append(fmtName(useDelimited, schema));
            sqlBuilder.append(".");
        }
        sqlBuilder.append(fmtName(useDelimited, table));

        return sqlBuilder.toString();
    }

    protected String defaultQualifier() {
        return "";
    }

    public String leftQualifier() {
        return this.defaultQualifier();
    }

    public String rightQualifier() {
        return this.defaultQualifier();
    }
}

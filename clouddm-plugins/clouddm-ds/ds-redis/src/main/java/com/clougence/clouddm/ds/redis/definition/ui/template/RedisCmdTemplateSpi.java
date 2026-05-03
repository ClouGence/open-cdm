package com.clougence.clouddm.ds.redis.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.redis.dialect.RedisDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class RedisCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    protected Dialect getDialect() { return RedisDialect.INSTANCE; }

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "GET " + KEY_PLACEHOLDER;
    }

    @Override
    public String getQuickQueryKey(CmdTemplateOption option) {
        return "GET " + KEY_PLACEHOLDER;
    }

    @Override
    public List<String> getRenameKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("RENAME ");
        sb.append(fmtName(option, option.getTargetName()));
        sb.append(" ");
        sb.append(fmtName(option, option.getTargetNewName()));
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEL ");
        sb.append(fmtName(option, option.getTargetName()));
        return Collections.singletonList(sb.toString());
    }
}

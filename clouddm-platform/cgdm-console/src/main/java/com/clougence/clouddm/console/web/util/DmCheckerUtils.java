package com.clougence.clouddm.console.web.util;

import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.StringUtils;

/**
 * @author mode create time is 2021/1/30
 **/
public class DmCheckerUtils {

    public static void checkDetectRuleScript(String scriptContent) {
        if (StringUtils.isBlank(scriptContent)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_SCRIPT_EMPTY_ERROR.name()));
        }

        try {
            StatementSet statementSet = DslHelper.parserDsl("DetectRule", scriptContent);
            long codeLines = statementSet.getStatements().stream().filter(s -> {
                return !s.getClass().getSimpleName().equals("DefineStatement");
            }).count();
            if (codeLines == 0) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_SCRIPT_EMPTY_ERROR.name()));
            }
        } catch (ErrorMessageException e) {
            throw e;
        } catch (AntlerSyntaxException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_DSL_SYNTAX_ERROR.name(), e.getLine(), e.getColumn(), e.getMessage()));
        } catch (Exception e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_DSL_UNKNOWN_ERROR.name(), e.getMessage()));
        }
    }
}

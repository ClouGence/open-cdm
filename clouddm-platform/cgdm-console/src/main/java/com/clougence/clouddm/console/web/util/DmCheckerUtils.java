/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.util;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
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

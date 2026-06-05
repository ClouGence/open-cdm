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
package com.clougence.clouddm.team.provider.dingtalk.constants.approval;

public class DingConstant {

    // global
    public static final String FORM_TITLE               = "标题";
    public static final String FORM_DESCRIPTION         = "需求描述";

    // for SQL
    public static final String FORM_SQL_TARGET          = "目标数据源";
    public static final String FORM_SQL_EXECUTE_SQL     = "执行 SQL";
    public static final String FORM_SQL_ROLLBACK_SQL    = "回滚 SQL";
    public static final String FORM_SQL_AFFECT_LINE     = "预计受影响行数";

    // for Auth
    public static final String FORM_AUTH_TABLE          = "申请的权限";
    public static final String FORM_AUTH_TABLE_RES_DESC = "数据源描述";
    public static final String FORM_AUTH_TABLE_PATH     = "资源路径";
    public static final String FORM_AUTH_TABLE_TIME     = "生效时间";
    public static final String FORM_AUTH_TABLE_AUTH     = "权限列表";

    // for Change
    public static final String FORM_CHANGE_PROJECT      = "项目";
    public static final String FORM_CHANGE_NAME         = "变更";
    public static final String FORM_CHANGE_BRANCH       = "分支";
    public static final String FORM_CHANGE_TARGET       = "目标数据源";
    public static final String FORM_CHANGE_EXECUTE_SQL  = "执行 SQL";
}

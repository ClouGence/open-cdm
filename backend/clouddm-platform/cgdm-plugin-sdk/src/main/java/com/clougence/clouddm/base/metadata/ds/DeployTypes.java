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
package com.clougence.clouddm.base.metadata.ds;

import java.util.Set;

public enum DeployTypes {
    SELF_MAINTENANCE,
    AWS_CLOUD_HOSTED,
    ALIBABA_CLOUD_HOSTED,
    MICROSOFT_AZURE_CLOUD_HOSTED,
    GOOGLE_CLOUD_HOSTED,
    TENCENT_CLOUD_HOSTED,
    HUAWEI_CLOUD_HOSTED;

    public static Set<DeployTypes> mysql() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, ALIBABA_CLOUD_HOSTED, MICROSOFT_AZURE_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> oracle() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> sqlServer() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, ALIBABA_CLOUD_HOSTED, MICROSOFT_AZURE_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> postgresql() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, ALIBABA_CLOUD_HOSTED, MICROSOFT_AZURE_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> mariaDb() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> gaussDb() {
        return Set.of(SELF_MAINTENANCE, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> polarDbMysql() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> polarDbX() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> polarDbPg() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> clickHouse() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> greenplum() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> redis() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, ALIBABA_CLOUD_HOSTED, MICROSOFT_AZURE_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> valkey() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> mongoDb() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> kafka() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, ALIBABA_CLOUD_HOSTED, GOOGLE_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> elasticsearch() {
        return Set.of(SELF_MAINTENANCE, ALIBABA_CLOUD_HOSTED, TENCENT_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> openSearch() {
        return Set.of(SELF_MAINTENANCE, AWS_CLOUD_HOSTED, HUAWEI_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> adbForMysql() {
        return Set.of(ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> redshift() {
        return Set.of(AWS_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> hologres() {
        return Set.of(ALIBABA_CLOUD_HOSTED);
    }

    public static Set<DeployTypes> maxCompute() {
        return Set.of(ALIBABA_CLOUD_HOSTED);
    }
}

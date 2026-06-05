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
package com.clougence.clouddm.base.metadata.rdp.enumeration;

public class PaimonPropsKey {

    // common
    public static final String CATALOG_URI       = "uri";

    public static final String CATALOG_METASTORE = "metastore";

    public static final String CATALOG_WAREHOUSE = "warehouse";

    // s3
    public static final String S3_ACCESS_KEY     = "s3.access-key";

    public static final String S3_SECRET_KEY     = "s3.secret-key";

    public static final String S3_ENDPOINT       = "s3.endpoint";

    // dlf
    public static final String DLF_ACCESS_ID     = "dlf.access-key-id";

    public static final String DLF_SECRET_KEY    = "dlf.access-key-secret";

    public static final String TOKEN_PROVIDER    = "token.provider";

    public static final String DLF_REGION        = "dlf.region";

    // cosn
    public static final String COSN_ACCESS_ID    = "fs.cosn.userinfo.secretId";

    public static final String COSN_SECRET_KEY   = "fs.cosn.userinfo.secretKey";

    public static final String COSN_REGION       = "fs.cosn.bucket.region";

    // oss
    public static final String OSS_ACCESS_ID     = "fs.oss.accessKeyId";

    public static final String OSS_SECRET_ID     = "fs.oss.accessKeySecret";

    public static final String OSS_ENDPOINT      = "fs.oss.endpoint";
}

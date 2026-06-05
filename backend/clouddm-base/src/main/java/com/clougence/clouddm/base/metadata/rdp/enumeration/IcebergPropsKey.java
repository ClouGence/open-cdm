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

public class IcebergPropsKey {

    public static final String CATALOG_NAME              = "catalog_name";

    public static final String CATALOG_URI               = "uri";

    public static final String CATALOG_TYPE              = "type";

    public static final String CATALOG_REF               = "ref";

    public static final String WAREHOUSE_LOCATION        = "warehouse";

    public static final String IO_IMPL                   = "io-impl";

    // s3
    public static final String S3_ENDPOINT               = "s3.endpoint";

    public static final String S3_ACCESS_KEY             = "s3.access-key-id";

    public static final String S3_SECRET_KEY             = "s3.secret-access-key";

    public static final String S3_PATH_STYLE_ACCESS      = "s3.path-style-access";

    public static final String GLUE_ACCESS_KEY           = "client.credentials-provider.glue.access-key-id";

    public static final String GLUE_SECRET_KEY           = "client.credentials-provider.glue.secret-access-key";

    public static final String GLUE_CREDENTIALS_PROVIDER = "client.credentials-provider";

    public static final String AWS_CLIENT_REGION         = "client.region";

}

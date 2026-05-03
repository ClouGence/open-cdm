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

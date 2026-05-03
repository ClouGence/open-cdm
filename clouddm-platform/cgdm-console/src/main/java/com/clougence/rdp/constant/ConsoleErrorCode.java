package com.clougence.rdp.constant;

import com.clougence.rdp.util.RdpI18nUtils;

/**
 * @author bucketli 2021/1/6 10:18
 */
@Deprecated // use throw new ErrorMessageException(RdpI18nUtils.getMessage(xxxx));
public enum ConsoleErrorCode {

    UNKNOWN(ErrorType.SYSTEM, "1001"),

    EMPTY_VERIFY_CODE(ErrorType.SYSTEM, "1002"),

    GET_A_VERIFY_CODE_FIRST(ErrorType.SYSTEM, "1003"),

    VERIFY_CODE_IS_ERROR(ErrorType.SYSTEM, "1004"),

    VERIFY_CODE_IS_EXPIRED(ErrorType.SYSTEM, "1005"),

    VERIFY_CODE_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1006"),

    ALREADY_REGISTER(ErrorType.SYSTEM, "1007"),

    PUNISH_NOT_FINISH_YET(ErrorType.SYSTEM, "1008"),

    NEED_REGISTER_FIRST(ErrorType.SYSTEM, "1009"),

    ALREADY_HAVE_SAME_CODE_OF_PRODUCT_CLUSTER(ErrorType.SYSTEM, "1010"),

    ALREADY_HAVE_SAME_NAME_OF_PRODUCT_CLUSTER(ErrorType.SYSTEM, "1011"),

    VERIFY_EMAIL_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1011"),

    VERIFY_IM_FREQUENCY_TOO_FAST(ErrorType.SYSTEM, "1012"),

    NO_AUTHORITY_TO_OPERATE_ON_THIS_RESOURCE(ErrorType.USER, "2002"),

    VERIFY_CODE_ERROR(ErrorType.USER, "2007"),

    USER_ALIYUN_AK_SK_INVALID(ErrorType.USER, "2011"),

    PASSWD_CAN_NOT_BE_BLANK(ErrorType.USER, "2012"),

    RDS_INSTANCE_NOT_FOUND(ErrorType.DATASOURCE, "3010"),

    STILL_HAVE_BIZ_USE_IT_WHEN_DELETE_DATASOURCE(ErrorType.DATASOURCE, "3011"),
    /*
     * RSocket errors
     */
    RPC_INVOKER_CLUSTER_ERROR(ErrorType.RSOCKET, "5003"),
    RPC_INVOKER_SPECIFIED_ERROR(ErrorType.RSOCKET, "5004"),

    /*
     * Login errors
     */
    VERIFY_PHONE_DISAGREE_FIRST(ErrorType.USER, "8009"),
    SSO_UNSUPPORT_TYPE(ErrorType.USER, "8010"),

    /*
     * License Auth Error Code
     */
    LICENSE_VERSION_FIELD_ERROR(ErrorType.USER, "8011"),
    AUTH_CODE_VERIFY_FAILED(ErrorType.USER, "8012"),
    LICENSE_FEATURE_LIMIT(ErrorType.USER, "8013"),

    /**
     * SAAS Error Code
     */
    SAAS_LOCKED_ACCOUNT_MSG(ErrorType.USER, "9000"),
    SAAS_UN_SUBSCRIBE_ACCOUNT_MSG(ErrorType.USER, "9001"),
    SAAS_UN_BIND_BANK_ACCOUNT_MSG(ErrorType.USER, "9002"),
    SAAS_SUBSCRIBE_ACCOUNT_FAILED_MSG(ErrorType.USER, "9003"),
    SAAS_USER_ERROR_STATUS_CHINA(ErrorType.USER, "9004"),
    SAAS_USER_ERROR_STATUS_OVERSEA(ErrorType.USER, "9005"),;

    private final String    code;
    private final ErrorType type;

    ConsoleErrorCode(ErrorType type, String code){
        this.code = code;
        this.type = type;
    }

    public String getCode() { return code; }

    public String getType() { return type.name(); }

    public String getMessage(Object... params) {
        return RdpI18nUtils.getMessage(this, params);
    }
}

enum ErrorType {
    SYSTEM,
    USER,
    DATASOURCE,
    WORKER,
    DATA_OP,
    RSOCKET,
    TICKET,
    SQL_EDITOR,
    DS_ENV
}

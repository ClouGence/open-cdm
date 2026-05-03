package com.clougence.clouddm.sdk.model.exception;

import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.utils.ArrayUtils;

import lombok.Getter;

@Getter
public class ThirdPartyApiException extends RuntimeException {

    private final String                 productType;
    private final ThirdPartyApiErrorType errorType;
    private final String                 messageKey;
    private final Object[]               messageArgs;

    public ThirdPartyApiException(Throwable e, String errorMessage, String productType, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs){
        super(errorMessage, e);
        this.productType = productType;
        this.errorType = errorType;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public static ThirdPartyApiExceptionBuilder asRDP() {
        return new ThirdPartyApiExceptionBuilderImpl(RdpFeatureIDs.PRODUCT_CLOUD_RDP);
    }

    public static ThirdPartyApiExceptionBuilder asDM() {
        return new ThirdPartyApiExceptionBuilderImpl(RdpFeatureIDs.PRODUCT_CLOUD_DM);
    }

    public static ThirdPartyApiExceptionBuilder asCC() {
        return new ThirdPartyApiExceptionBuilderImpl(RdpFeatureIDs.PRODUCT_CLOUD_CANAL);
    }

    public interface ThirdPartyApiExceptionBuilder {

        ThirdPartyApiException with(String messageKey, Object... messageArgs);

        ThirdPartyApiException with(ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs);

        ThirdPartyApiException with(Throwable e);

        ThirdPartyApiException with(Throwable e, String messageKey, Object... messageArgs);

        ThirdPartyApiException with(Throwable e, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs);
    }

    private static final class ThirdPartyApiExceptionBuilderImpl implements ThirdPartyApiExceptionBuilder {

        private final String productType;

        public ThirdPartyApiExceptionBuilderImpl(String productType){
            this.productType = productType;
        }

        public ThirdPartyApiException with(String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(null, messageKey, productType, ThirdPartyApiErrorType.OTHER, messageKey, messageArgs);
        }

        public ThirdPartyApiException with(ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(null, messageKey, productType, errorType, messageKey, messageArgs);
        }

        public ThirdPartyApiException with(Throwable e) {
            return new ThirdPartyApiException(e, e.getMessage(), productType, ThirdPartyApiErrorType.OTHER, e.getMessage(), ArrayUtils.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public ThirdPartyApiException with(Throwable e, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(e, e.getMessage(), productType, ThirdPartyApiErrorType.OTHER, messageKey, messageArgs);
        }

        @Override
        public ThirdPartyApiException with(Throwable e, ThirdPartyApiErrorType errorType, String messageKey, Object... messageArgs) {
            return new ThirdPartyApiException(e, e.getMessage(), productType, errorType, messageKey, messageArgs);
        }
    }
}

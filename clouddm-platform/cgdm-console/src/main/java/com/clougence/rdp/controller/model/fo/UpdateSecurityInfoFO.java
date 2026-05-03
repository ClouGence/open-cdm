package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

import lombok.Data;

/**
 * @author bucketli 2021/1/28 12:22
 */
@Data
public class UpdateSecurityInfoFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long          dataSourceId;

    @NotBlank(message = "{notblank.username}")
    private String        userName;

    private String        password;

    private String        accessKey;

    private String        secretKey;

    private String        clientTrustStorePassword;

    private SecurityType  securityType;

    /** like krb5 file,ssl trust store file */
    private MultipartFile securityFile;

    private String        securityFilePassword;

    /** like kerberos file , jaas file */
    private MultipartFile secretFile;

    private String        secretFilePassword;

    // like client ssl ca file
    private MultipartFile clientSecurityFile;

    private String        clientSecurityFilePassword;
}

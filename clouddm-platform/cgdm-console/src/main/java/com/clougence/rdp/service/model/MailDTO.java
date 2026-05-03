package com.clougence.rdp.service.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author bucketli 2020-02-01 12:00
 * @since 1.1.3
 */
@Data
@Builder
public class MailDTO {

    @Tolerate
    public MailDTO(){
    }

    private List<String>    mailTo;

    private String          subject;

    private String          content;

    /**
     * carbon copy
     */
    private List<String>    cc;

    /**
     * blind carbon copy
     */
    private List<String>    bcc;

    /**
     * attachments
     */
    private MultipartFile[] multipartFiles;

    private boolean         isHtml;
}

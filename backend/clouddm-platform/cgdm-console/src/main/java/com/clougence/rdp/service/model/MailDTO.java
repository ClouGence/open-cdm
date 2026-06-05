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

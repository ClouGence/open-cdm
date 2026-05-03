package com.clougence.rdp.controller.model.fo;

import lombok.Data;

/**
 * @author bucketli 2020/4/13 14:47
 */
@Data
public class PageData {

    public static final int MAX_PAGE_SIZE = 20;
    /**
     * Used for cloudcanal. Invoke rds openAPI do not dependend on this field
     */
    private long            startId;

    private int             pageSize;

    private int             pageNumber;
}

package com.clougence.rdp.controller.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chunlin create time is 2024/7/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceVO {

    private Long   id;
    private String resourceFlag;
    private String flagDesc;
}

package com.clougence.clouddm.console.web.model.vo.envparam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-14 14:02
 */
@Getter
@Setter
@Builder
public class DmEnvParamTicketDesVO {

    private boolean openTicket;
    private String  type;
    private String  typeI18n;
    private String  templateId;
    private String  templateName;
    private boolean isDelete;
}

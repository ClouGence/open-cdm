package com.clougence.clouddm.console.web.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Getter
@Setter
public class AlertConfigVO {

    private long    id;

    private String  uid;

    private boolean phone;

    private boolean email;

    private boolean dingding;

    private boolean sms;

    private boolean duplicated;

    private String  ruleName;

    private String  expression;

    private boolean sendAdmin;

    private boolean sendSystem;

    private Long    workerId;

}

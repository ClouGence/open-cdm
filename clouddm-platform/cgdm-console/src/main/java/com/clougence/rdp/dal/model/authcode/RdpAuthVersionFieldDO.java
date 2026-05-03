package com.clougence.rdp.dal.model.authcode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "rdp_auth_version_field")
public class RdpAuthVersionFieldDO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Date    gmtCreate;

    private Date    gmtModified;

    private String  licenseVersion;

    private String  fields;

}

package com.clougence.rdp.controller.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.clougence.rdp.dal.model.RdpDsEnvDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/18
 **/
@Getter
@Setter
public class DsEnvVO {

    private Long   id;

    private String ownerUid;

    private String envName;

    private String description;

    private Long   queryLimit;

    public static List<DsEnvVO> generateVO(List<RdpDsEnvDO> dsEnvDOList) {
        List<DsEnvVO> dsEnvVOList = new ArrayList<>();
        for (RdpDsEnvDO dsEnvDO : dsEnvDOList) {
            dsEnvVOList.add(generateVO(dsEnvDO));
        }
        return dsEnvVOList;
    }

    public static DsEnvVO generateVO(RdpDsEnvDO dsEnvDO) {
        DsEnvVO dsEnvVO = new DsEnvVO();
        dsEnvVO.setId(dsEnvDO.getId());
        dsEnvVO.setEnvName(dsEnvDO.getEnvName());
        dsEnvVO.setDescription(dsEnvDO.getDescription());
        dsEnvVO.setOwnerUid(dsEnvDO.getOwnerUid());
        return dsEnvVO;
    }
}

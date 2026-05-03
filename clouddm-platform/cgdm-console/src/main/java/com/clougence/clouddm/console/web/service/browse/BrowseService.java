package com.clougence.clouddm.console.web.service.browse;

import java.util.List;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseObjectMO;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * @author mode create time is 2021/1/5
 **/
public interface BrowseService {

    /** for service API '/browse/listLevels' */
    List<BrowseLevelsVO> listLevels(String puid, String uid, DsLevels levels, boolean refreshCache);

    /** for service API '/browse/listLevels' */
    List<BrowseLevelsVO> listDs(String puid, String uid, String envId);

    /** for service API '/browse/listLevels' */
    List<BrowseLevelsVO> listDsIncludeAllEnv(String puid, String uid);

    /** for service API '/browse/listLeaf' */
    List<BrowseLevelsVO> listLeaf(String puid, String uid, DsLevels levels, UmiTypes leafType, String pattern, boolean refreshCache);

    /** for service API '/browse/detailLevels' */
    BrowseLevelsVO detailLevels(String puid, String uid, DsLevels levels);

    /** for service API '/browse/detailLevels' */
    BrowseLevelsVO detailDs(String uid, DsLevels levels);

    /** for service API '/browse/rdbTableDetail' */
    BrowseObjectMO rdbObjectDetail(String puid, String uid, DsLevels levels, UmiTypes leafType, String leafName, boolean refreshCache);

    List<BrowseColumnMO> rdbColumns(String puid, String uid, DsLevels levels, UmiTypes leafType, String leafName);
}

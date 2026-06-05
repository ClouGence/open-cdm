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

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
package com.clougence.rdp.controller;

import static com.clougence.clouddm.base.metadata.ds.DataSourceType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.constants.RegionArea;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.model.vo.RegionAreaVO;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;

/**
 * @author bucketli 2020/12/30 12:17
 */
@Deprecated
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/constant")
public class RdpConstantController {

    private static final List<DataSourceType> SUPPORTED_DS_TYPES = Arrays
        .asList(Db2, Db2Fori, GaussDB, GaussDBForOpenGauss, KingbaseES, MariaDB, MySQL, ObForOracle, OceanBase, Oracle, PostgreSQL, SQLServer, TiDB, ClickHouse, Doris, Greenplum, SelectDB, StarRocks, PolarDBPg, PolarDbMySQL, PolarDbX, AdbForMySQL, Hologres, MaxCompute, MongoDB, Redis);

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/listregionareas", method = RequestMethod.POST)
    public ResWebData<?> listRegionAreas() {
        List<RegionAreaVO> re = new ArrayList<>();
        for (RegionArea ra : RegionArea.values()) {
            RegionAreaVO areaVO = new RegionAreaVO(ra);
            re.add(areaVO);
        }

        return ResWebDataUtils.buildSuccess(re);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listfilterdstypes", method = RequestMethod.POST)
    public ResWebData<?> listFilterDsTypes() {
        List<String> dsTypes = Arrays.stream(DataSourceType.values()).map(Enum::name).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(dsTypes);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/listdstypesbydeploytype", method = RequestMethod.POST)
    public ResWebData<?> listDsTypesByDeployType() {
        List<List<String>> result = DataSourceType.groupByDisplay(SUPPORTED_DS_TYPES).stream().map(this::toDsTypeNames).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(result);
    }

    protected List<String> toDsTypeNames(List<DataSourceType> dsTypes) {
        return dsTypes.stream().map(DataSourceType::getTypeName).collect(Collectors.toList());
    }
}

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
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.RegionAreaVO;
import com.clougence.rdp.dal.enumeration.RegionArea;

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

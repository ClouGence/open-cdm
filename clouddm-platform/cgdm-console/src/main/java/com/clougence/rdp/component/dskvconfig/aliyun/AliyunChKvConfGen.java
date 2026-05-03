//package com.clougence.rdp.component.dskvconfig.aliyun;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.cloud.aliyun.AliyunDsBaseData;
//import com.clougence.rdp.cloud.aliyun.AliyunRegion;
//import com.clougence.rdp.cloud.aliyun.ck.CkOpenApi;
//import com.clougence.rdp.cloud.aliyun.ck.model.CkCategory;
//import com.clougence.rdp.cloud.aliyun.ck.model.CkCluster;
//import com.clougence.rdp.cloud.aliyun.ck.model.CkConfigEntry;
//import com.clougence.clouddm.api.common.crypt.AesSymmetricCryptService;
//import com.clougence.rdp.component.dskvconfig.RdpDsConfigService;
//import com.clougence.rdp.component.dskvconfig.RdpDsKvConfGen;
//import com.clougence.rdp.component.dskvconfig.model.CkExtraConfig;
//import com.clougence.rdp.dal.model.RdpDataSourceDO;
//import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
//import com.clougence.clouddm.base.metadata.ds.DataSourceType;
//import com.clougence.rdp.service.RdpRegionService;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author bucketli 2023/4/10 16:52:28
// */
//@Service
//@Slf4j
//public class AliyunChKvConfGen implements RdpDsKvConfGen {
//
//    @Resource
//    private CkOpenApi          rdpCkOpenApi;
//
//    @Resource
//    private RdpDsConfigService rdpDsConfigService;
//
//    @Resource
//    private RdpRegionService   rdpRegionService;
//
//    @Override
//    public void genDsKvConf(RdpDataSourceDO dsDO) {
//        AliyunRegion region = rdpRegionService.mapToAliyun(dsDO.getRegion());
//
//        AliyunDsBaseData baseData = AliyunDsBaseData.builder()
//            .instanceIds(Collections.singletonList(dsDO.getInstanceId()))
//            .aliyunRegion(region)
//            .dataSourceType(dsDO.getDataSourceType())
//            .aliyunAk(dsDO.getAccessKey())
//            .aliyunSk(AesSymmetricCryptService.getInstance().decryptUseDefaultKeyAndSalt(dsDO.getSecretKey()))
//            .build();
//
//        List<CkCluster> clusters = rdpCkOpenApi.describeDBClusters(baseData);
//        if (clusters == null || clusters.size() != 1) {
//            throw new IllegalArgumentException("clickhouse (" + dsDO.getInstanceId() + ") is not exist or have multi instances.");
//        }
//
//        baseData.setInstanceId(dsDO.getInstanceId());
//        Map<String, CkConfigEntry> clusterConfs = rdpCkOpenApi.describeDbClusterConfig(baseData);
//
//        CkCluster cluster = clusters.get(0);
//        List<RdpDsKvBaseConfigDO> configs = rdpDsConfigService.fetchDefaultConfig(dsDO.getId(), DataSourceType.ClickHouse);
//        for (RdpDsKvBaseConfigDO config : configs) {
//            if (config.getConfigName().equals(CkExtraConfig.Fields.multiReplica)) {
//                if (cluster.getCategory() == CkCategory.HighAvailability) {
//                    config.setConfigValue("true");
//                } else {
//                    config.setConfigValue("false");
//                }
//            } else if (config.getConfigName().equals(CkExtraConfig.Fields.clusterName)) {
//                CkConfigEntry v = clusterConfs.get("cluster_name");
//                if (v != null) {
//                    config.setConfigValue(v.getCurrentValue());
//                }
//            }
//        }
//
//        rdpDsConfigService.persistInnerDsConfig(dsDO, configs);
//    }
//}

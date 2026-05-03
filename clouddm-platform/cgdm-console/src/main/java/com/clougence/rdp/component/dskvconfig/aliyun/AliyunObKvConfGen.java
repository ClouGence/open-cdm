//package com.clougence.rdp.component.dskvconfig.aliyun;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.component.dskvconfig.RdpDsConfigService;
//import com.clougence.rdp.component.dskvconfig.RdpDsKvConfGen;
//import com.clougence.rdp.component.dskvconfig.model.OceanBaseExtraConfig;
//import com.clougence.rdp.dal.model.RdpDataSourceDO;
//import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
//import com.clougence.clouddm.base.metadata.ds.DataSourceType;
//import com.clougence.clouddm.base.metadata.rdp.enumeration.ObIncreMode;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author bucketli 2023/4/10 16:52:28
// */
//@Service
//@Slf4j
//public class AliyunObKvConfGen implements RdpDsKvConfGen {
//
//    @Resource
//    private RdpDsConfigService rdpDsConfigService;
//
//    @Override
//    public void genDsKvConf(RdpDataSourceDO dsDO) {
//        List<RdpDsKvBaseConfigDO> configs = rdpDsConfigService.fetchDefaultConfig(dsDO.getId(), DataSourceType.OceanBase);
//        for (RdpDsKvBaseConfigDO config : configs) {
//            if (config.getConfigName().equals(OceanBaseExtraConfig.Fields.obIncreMode)) {
//                config.setConfigValue(ObIncreMode.Binlog.name());
//            }
//        }
//
//        rdpDsConfigService.persistInnerDsConfig(dsDO, configs);
//    }
//}

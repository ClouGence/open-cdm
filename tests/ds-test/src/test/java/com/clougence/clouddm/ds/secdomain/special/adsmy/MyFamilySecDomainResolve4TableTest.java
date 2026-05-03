package com.clougence.clouddm.ds.secdomain.special.adsmy;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySecDomainResolveSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbMySplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainResolve4TableTest;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MyFamilySecDomainResolve4TableTest extends MySecDomainResolve4TableTest {

    public MyFamilySecDomainResolve4TableTest(){
        this.analysisSpi = new AdbMyResAnalysisSpi(null);
        this.resolveSpi = new AdbMySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new AdbMySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }

    @Test
    public void adbCreateTable() {
        String sql = "CREATE TABLE `tms_hub_pd_back_ds_task` (\n" + "  `id` bigint NOT NULL COMMENT '主键Id',\n" + "  `base_info_id` bigint COMMENT '基础信息id',\n"
                     + "  `job_id` bigint COMMENT '作业单id',\n" + "  `waybill_no` varchar(32) DEFAULT '' COMMENT '运单号',\n"
                     + "  `task_status` varchar(20) DEFAULT '' COMMENT 'init待处理,complete完成,exceptionClose异常关闭',\n" + "  `close_remark` varchar(255) DEFAULT '' COMMENT '异常关闭原因',\n"
                     + "  `task_create_type` varchar(20) DEFAULT '' COMMENT '任务生成方式;dispatch调度,plan计划生成,temp临时生成',\n"
                     + "  `operate_hub_code` varchar(64) DEFAULT '' COMMENT '操作网点',\n" + "  `operate_hub_name` varchar(128) DEFAULT '' COMMENT '操作网点名称',\n"
                     + "  `driver_code` varchar(64) DEFAULT '' COMMENT '司机编码',\n" + "  `driver_name` varchar(128) DEFAULT '' COMMENT '司机名称',\n"
                     + "  `driver_type` varchar(20) DEFAULT '' COMMENT '司机类型',\n" + "  `auto_get_da` tinyint DEFAULT '0' COMMENT '自动获取Da',\n"
                     + "  `next_work_day` datetime COMMENT '下一工作日',\n" + "  `operate_date` datetime COMMENT '操作时间',\n"
                     + "  `operate_user_code` varchar(64) DEFAULT '' COMMENT '操作人',\n" + "  `operate_user_name` varchar(128) DEFAULT '' COMMENT '操作人名称',\n"
                     + "  `next_info` varchar(50) DEFAULT '' COMMENT '指导提示',\n" + "  `is_warn` tinyint DEFAULT '0' COMMENT '是否警告,0为非警告,1为警告',\n"
                     + "  `warn_type` varchar(10) COMMENT '警告类型',\n" + "  `warn_remark` varchar(1024) COMMENT '警告备注',\n" + "  `problem_id` bigint COMMENT '当前问题件Id',\n"
                     + "  `problem_type` varchar(128) COMMENT '问题件类型',\n" + "  `problem_reason` varchar(255) COMMENT '问题件原因',\n"
                     + "  `operation_resource` int DEFAULT '1' COMMENT '操作来源 1表示WEB端 2表示PDA 3表示DriverApp 4表示接口',\n"
                     + "  `operation_method` int DEFAULT '1' COMMENT '操作方式 1表示单票写入 2表示Excel导入',\n" + "  `business_uuid` bigint COMMENT '业务id',\n"
                     + "  `delivery_record_id` bigint COMMENT '派件扫描Id',\n" + "  `org_id` bigint DEFAULT '0' COMMENT '公司编码',\n"
                     + "  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除',\n" + "  `record_version` bigint DEFAULT '0' COMMENT '版本编码',\n"
                     + "  `create_date` datetime COMMENT '创建时间',\n" + "  `create_user_code` varchar(255) DEFAULT '' COMMENT '新增用户编码',\n"
                     + "  `create_user_name` varchar(255) DEFAULT '' COMMENT '新增用户名',\n" + "  `last_upd_date` datetime COMMENT '更新时间',\n"
                     + "  `last_upd_user_code` varchar(255) DEFAULT '' COMMENT '最后修改用户编码',\n" + "  `last_upd_user_name` varchar(255) DEFAULT '' COMMENT '最后修改用户名',\n"
                     + "  `schedule_pie_date` datetime COMMENT '预约派件日期',\n" + "  `country` varchar(30) DEFAULT '' COMMENT '国家',\n"
                     + "  `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                     + "  `data_source_system` varchar(10) COMMENT '数据来源系统,ops为新系统,hub为老系统',\n" + "  PRIMARY KEY (`id`)\n"
                     + ") DISTRIBUTE BY HASH(`id`) INDEX_ALL='Y' STORAGE_POLICY='HOT' ENGINE='XUANWU' TABLE_PROPERTIES='{\"format\":\"columnstore\"}' COMMENT='归班反馈任务'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 42;
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 43;

    }
}

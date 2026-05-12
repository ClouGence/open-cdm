///*
// * Copyright 2026 杭州开云集致科技有限公司
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.clougence.clouddm.console.web.dal.model.dto;
//
//import org.apache.commons.lang3.time.DateFormatUtils;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.annotation.write.style.ColumnWidth;
//import com.alibaba.excel.annotation.write.style.ContentStyle;
//import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
//import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
//import com.clougence.clouddm.console.web.dal.model.RdpOpAuditDO;
//import com.clougence.rdp.util.DmI18nUtils;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
///**
// * @author chunlin create time is 2024/11/4
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class OpAuditExportDTO {
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 0, value = "EXPORT_OPAUDIT_USER_NAME")
//    private String        userName;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 1, value = "EXPORT_OPAUDIT_UID")
//    private String        uid;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 2, value = "EXPORT_OPAUDIT_OPERATE_DATE")
//    private String        operateDate;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 3, value = "EXPORT_OPAUDIT_RESOURCE_TYPE")
//    private String        resourceTypeDesc;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 4, value = "EXPORT_OPAUDIT_AUDIT_TYPE")
//    private String        auditTypeDesc;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 5, value = "EXPORT_OPAUDIT_RESOURCE_ID")
//    private String        resourceId;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 6, value = "EXPORT_OPAUDIT_RESOURCE_VALUE")
//    private String        resourceValue;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 7, value = "EXPORT_OPAUDIT_LOG_PATH_WORKER_IP")
//    private String        logPathWorkerIp;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 8, value = "EXPORT_OPAUDIT_SECURITY_LEVEL")
//    private String        securityLevel;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 9, value = "EXPORT_OPAUDIT_UUID_KEY")
//    private String        uuidKey;
//
//    @ColumnWidth(20)
//    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    @ExcelProperty(index = 10, value = "EXPORT_OPAUDIT_LOG_INFO")
//    private String        logInfo;
//
//    public OpAuditExportDTO convertFromDO(RdpOpAuditDO auditDO) {
//        this.uid = auditDO.getUid();
//        this.userName = auditDO.getUserName();
//        this.operateDate = DateFormatUtils.format(auditDO.getOperateDate(), "yyyy-MM-dd HH:mm:ss");
//        this.resourceId = auditDO.getResourceValue();
//        this.resourceValue = auditDO.getResourceName();
//        this.securityLevel = auditDO.getSecurityLevel().name();
//        this.uuidKey = auditDO.getUuidKey();
//        this.logInfo = auditDO.getLogInfo();
//        if (auditDO.getAuditType() != null) {
//            this.auditTypeDesc = DmI18nUtils.getMessage(auditDO.getAuditType().name());
//        }
//
//        if (auditDO.getResourceType() != null) {
//            this.resourceTypeDesc = DmI18nUtils.getMessage(auditDO.getResourceType().name());
//        }
//
//        if (auditDO.getIp() != null) {
//            this.logPathWorkerIp = auditDO.getIp();
//        }
//        return this;
//    }
//}

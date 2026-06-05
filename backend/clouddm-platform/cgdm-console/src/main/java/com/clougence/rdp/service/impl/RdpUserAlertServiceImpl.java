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
//package com.clougence.clouddm.console.web.component.config.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import com.clougence.clouddm.console.web.component.alert.RdpMailAlertService;
//import com.clougence.clouddm.console.web.component.config.UserConfigService;
//import com.clougence.rdp.service.RdpUserAlertService;
//
//import jakarta.annotation.Resource;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author bucketli 2023/12/26 14:36:03
// */
//@Service
//@Slf4j
//public class RdpUserAlertServiceImpl implements RdpUserAlertService {
//
//    @Autowired
//    @Qualifier("RdpAlibabaMailAlertService")
//    @Setter
//    protected RdpMailAlertService  rdpMailAlertService;
//    @Resource
//    @Setter
//    protected UserConfigService rdpUserConfigService;
//
//    @Override
//    public RdpMailAlertService chooseMailAlertService() {
//        return rdpMailAlertService;
//    }
//}

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
//package com.clougence.clouddm.service;
//
//import org.junit.Test;
//import com.clougence.clouddm.base.metadata.ds.rdb.RdbConfig;
//import com.clougence.clouddm.base.metadata.ds.rdb.greenplum.GreenplumConfig;
//import com.clougence.clouddm.comm.model.WorkerIdentityAware;
//import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Getter;
//import lombok.Setter;
//
///**
// * @author wanshao create time is 2021/1/8
// **/
//public class RdbConfigTest {
//
//    @Test
//    public void doExample() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        GreenplumConfig config = new GreenplumConfig();
//        String valueAsString = objectMapper.writeValueAsString(config);
//
//        JavaType paramJavaType = objectMapper.getTypeFactory().constructType(RdbConfig.class);
//        Object readValue = objectMapper.readValue(valueAsString, paramJavaType);
//
//        System.out.println(readValue);
//
//        TestObject forwardDTO = new TestObject();
//        forwardDTO.setApiMethodName("");
//        forwardDTO.setJsonParams(new Object[] { config });
//        forwardDTO.setTargetWsn("specifiedSidecarWsn");
//
//        valueAsString = objectMapper.writeValueAsString(forwardDTO);
//        paramJavaType = objectMapper.getTypeFactory().constructType(TestObject.class);
//        readValue = objectMapper.readValue(valueAsString, paramJavaType);
//        System.out.println(readValue);
//    }
//
//    @Getter
//    @Setter
//    public class TestObject implements WorkerIdentityAware {
//
//        private String         uid;
//
//        private String         apiMethodName;
//
//        private Object[]       jsonParams;
//
//        private String         targetWsn;
//
//        private WorkerIdentity workerIdentity;
//
//        @Override
//        public void injectWorkerIdentity(WorkerIdentity workerIdentity) {
//            this.workerIdentity = workerIdentity;
//        }
//
//        @Override
//        public WorkerIdentity getWorkerIdentity() { return workerIdentity; }
//    }
//
//}

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

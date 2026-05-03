//package com.clougence.test.rsocket;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import com.clougence.clouddm.comm.model.RSocketAuthException;
//import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.rsocket.SocketAcceptor;
//import io.rsocket.plugins.SocketAcceptorInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
///**
// * @author wanshao create time is 2021/3/2
// **/
//@Slf4j
//public class TestSocketAcceptor implements SocketAcceptorInterceptor {
//
//    public TestSocketAcceptor(){
//    }
//
//    @Override
//    public SocketAcceptor apply(SocketAcceptor socketAcceptor) {
//        return (setup, sendingSocket) -> {
//            try {
//                // Here will occur issue
//                ConnAuthDTO authInfo = new ObjectMapper().readValue(setup.getDataUtf8(), ConnAuthDTO.class);
//
//                return socketAcceptor.accept(setup, sendingSocket);
//            } catch (Exception e) {
//                String errMsg = "try to accept a connection error.msg:" + ExceptionUtils.getRootCauseMessage(e);
//                log.warn(errMsg, e);
//                return Mono.error(new RSocketAuthException(errMsg));
//            }
//        };
//    }
//
//}

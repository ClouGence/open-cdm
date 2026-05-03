package com.clougence.clouddm.dingding;

import com.clougence.clouddm.team.provider.dingtalk.approval.DingApprovalProviderSpi;
import org.junit.Test;

import com.clougence.clouddm.team.provider.dingtalk.client.DingApi;
import com.clougence.clouddm.team.provider.dingtalk.client.DingClient;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;

public class DingTalkApiTest {

    private String ak      = "dingfur8gzrfvyrvc5eo";
    private String sk      = "PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl";
    private Long   agentId = 3285786501L;

    @Test
    public void test() {
        //
        DingApi api = new DingClient(null, ak, sk).getBindApi();
        DingApprovalProviderSpi sdk = new DingApprovalProviderSpi(null, null, DingTalkApiTest.class.getClassLoader()) {

            @Override
            protected DingApi approvalApi(String primaryUid) {
                return api;
            }
        };

        try {
            System.out.println(api.getClient().getApiToken());
            System.out.println(sdk.getUserDetailByUid("123", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws Exception {
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            OpenDingTalkClient build = OpenDingTalkStreamClientBuilder.custom()
                .credential(new AuthClientCredential("dingfur8gzrfvyr2vc5eo", "PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl"))
                //注册事件监听
                .registerAllEventListener(event -> {
                    try {
                        System.out.println(event);
                        //success
                        return EventAckStatus.SUCCESS;
                    } catch (Exception e) {
                        //fail
                        return EventAckStatus.LATER;
                    }
                })
                .build();
            build.start();
            build.stop();
        } finally {

        }

    }

    //    @Test
    //    void getTemplate() throws Exception {
    //        DingApproApi api = new DingApproApi();
    //        ApprovalInfoMO approAuthInfoMO = ApprovalInfoMO.builder()
    //            //
    //            //                .approType(ApprovalType.DINGDING)
    ////            .agentId(3285786501L)
    //            .accessKey("dingfur8gzrfvyrvc5eo")
    //            .secretKey("PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl")
    //            .build();
    //        DingTalkService dingTalkService = new DingTalkService();
    //        ApprovalInstanceInfoDTO info = dingTalkService.getLastInfo(approAuthInfoMO, "_8wYCtlxQmy_D-rk3-uAbg08131730880716");
    //        System.out.println(info);
    //    }
    //
    //    @Test
    //    void createInstance() throws Exception {
    //        DingApproApi api = new DingApproApi();
    //        ApprovalCreateInstanceDTO approvalInstanceDTO = new ApprovalCreateInstanceDTO();
    //        ApprovalInfoMO approAuthInfoMO = ApprovalInfoMO.builder()
    //            //
    //            //                .approType(ApprovalType.DINGDING)
    ////            .agentId(3285786501L)
    //            .accessKey("dingfur8gzrfvyrvc5eo")
    //            .secretKey("PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl")
    //            .phone("18939630347")
    //            .build();
    //        approvalInstanceDTO.setTemplateIdentity("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        approvalInstanceDTO.setTarget("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        approvalInstanceDTO.setRollbackSql("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        approvalInstanceDTO.setAffectCount(2L);
    //        approvalInstanceDTO.setDescription("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        approvalInstanceDTO.setTicketTitle("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        approvalInstanceDTO.setExecuteSql("PROC-9540BF08-9848-4F6E-8C28-8AD2F0286D84");
    //        //                Object instance = api.createInstance(approvalInstanceDTO,approAuthInfoMO);
    //        System.out.println(new Object());
    //    }
    //
    //    @Test
    //    void getUserId() throws Exception {
    //        DingApproApi api = new DingApproApi();
    //        ApprovalInfoMO approAuthInfoMO = ApprovalInfoMO.builder()
    //            //
    //            //                .approType(ApprovalType.DINGDING)
    ////            .agentId(3285786501L)
    //            .accessKey("dingfur8gzrfvyrvc5eo")
    //            .secretKey("PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl")
    //            .phone("19974077527")
    //            .build();
    //        //        Object instance = api.getDingUidByPhone(approAuthInfoMO);
    //        //        System.out.println(instance);
    //    }
    //
    //    @Test
    //    void getTaskDetail() throws Exception {
    //        DingApproApi api = new DingApproApi();
    //        ApprovalInfoMO approAuthInfoMO = ApprovalInfoMO.builder()
    //            //
    //            //                .approType(ApprovalType.DINGDING)
    ////            .agentId(3285786501L)
    //            .accessKey("dingfur8gzrfvyrvc5eo")
    //            .secretKey("PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl")
    //            .phone("19974077527")
    //            .build();
    //        //        api.test(approAuthInfoMO);
    //        //        System.out.println(instance);
    //    }
    //
    //    @Test
    //    void getTaskDetail1() throws Exception {
    //        DingApproApi api = new DingApproApi();
    //        ApprovalInfoMO approAuthInfoMO = ApprovalInfoMO.builder()
    //            //
    //            //                .approType(ApprovalType.DINGDING)
    //            .agentId("3285786501")
    //            .accessKey("dingfur8gzrfvyrvc5eo")
    //            .secretKey("PE5WKu9WwygPCVUBC6Sd0WoMTyizsy6cuS4TVc7NienkiyAb6qVNEIy-eeh9-nzl")
    //            .phone("19974077527")
    //            .build();
    ////        api.getApiToken(approAuthInfoMO);
    //        for (int i = 0; i < 100; i++) {
    ////            new Thread(()->{
    //
    //                System.out.println(new DingTalkService().getTemplates(approAuthInfoMO));
    ////            }).start();
    //
    //        }
    //        Thread.sleep(100000);
    ////        api.cancelApproval(apiToken,"02556408693319900416","Trqoj0KFQSyIV4ORfVCMNA08131731480547");
    //
    //    }

}

//package com.clougence.rdp.feishu;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Properties;
//
//import org.junit.jupiter.api.Test;
//
//import com.lark.oapi.core.request.EventReq;
//import com.lark.oapi.core.utils.Jsons;
//import com.lark.oapi.event.CustomEventHandler;
//import com.lark.oapi.event.EventDispatcher;
//import com.lark.oapi.service.im.ImService;
//import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1;
//import com.lark.oapi.ws.Client;
//
//public class FeiShuTest {
//
//    @Test
//    public void client() throws InterruptedException {
//        EventDispatcher build = EventDispatcher.newBuilder("", "").onP2MessageReceiveV1(new ImService.P2MessageReceiveV1Handler() {
//
//            @Override
//            public void handle(P2MessageReceiveV1 event) throws Exception {
//                System.out.printf("[ onP2MessageReceiveV1 access ], data: %s\n", Jsons.DEFAULT.toJson(event.getEvent()));
//            }
//        }).onCustomizedEvent("approval_instance", new CustomEventHandler() {
//
//            @Override
//            public void handle(EventReq event) throws Exception {
//                System.out.printf("[ onCustomizedEvent access ], type: message, data: %s\n", new String(event.getBody(), StandardCharsets.UTF_8));
//            }
//        }).onCustomizedEvent("approval_task", new CustomEventHandler() {
//
//            @Override
//            public void handle(EventReq event) throws Exception {
//                System.out.printf("[ onCustomizedEvent access ], type: message, data: %s\n", new String(event.getBody(), StandardCharsets.UTF_8));
//            }
//        }).build();
//
//        Client build1 = new Client.Builder("cli_a7c19626dce61900e", "zI0LJaQ2yYGg6XjmsSFlLgGqgTNESg38").eventHandler(build).build();
//        build1.start();
//
//    }
//
//    @Test
//    public void getApiToken() throws Exception {
//        Properties properties = new Properties();
//        properties.setProperty("appId", "cli_a7c3966dce61900e");
//        properties.setProperty("secret", "zI0LJaQ2yYGg6XjmsSFlLgGqgTNESg38");
//
//    }
//
//    private Properties getProperties() {
//
//        Properties properties = new Properties();
//        properties.setProperty("feishuApprovalAppID", "cli_a71c3966dce61900e");
//        properties.setProperty("feishuApprovalAppSecret", "zI0LJaQ12yYGg6XjmsSFlLgGqgTNESg38");
//        return properties;
//    }
//
//    @Test
//    public void getUserByPhone() throws Exception {
//        //        for (int i = 0; i < 1000; i++) {
//
//        try {
//            new FeishuApi().getUserIdByPhone(getProperties());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        //        }
//        Thread.sleep(100000);
//    }
//
//    @Test
//    public void getActivity() throws Exception {
//        new FeishuApi().getApprovalActivities(getProperties(), "32");
//    }
//
//    @Test
//    public void createInstance() throws Exception {
//        //        new FeishuApi().createInstance(getProperties(),null);
//    }
//
//    @Test
//    public void cancelInstance() throws Exception {
//        //        new FeishuApi().cancelInstance(getProperties());
//    }
//
//    @Test
//    public void getLastInfo() throws Exception {
//        new FeishuApi().getLastInfo(getProperties(), "23121231231");
//    }
//
//    @Test
//    public void getTemplate() throws Exception {
//        new FeishuApi().getTemplate(getProperties(), "0CCC4723-491BC-4578-BA38-795DE08B3CF9");
//    }
//
//    @Test
//
//    public void getUser() throws Exception {
//        //        new FeishuApi().getUserInfoById(getProperties());
//    }
//
//}

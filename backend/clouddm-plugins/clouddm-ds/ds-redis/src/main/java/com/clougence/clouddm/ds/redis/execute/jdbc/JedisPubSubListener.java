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
package com.clougence.clouddm.ds.redis.execute.jdbc;//package com.clougence.drivers.jedis.jdbc;

//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import com.clougence.utils.ExceptionUtils;
//
//import lombok.extern.slf4j.Slf4j;
//import redis.clients.jedis.JedisPubSub;
//
///**
// * @Author: Ekko
// * @Date: 2023-05-30 15:15
// */
//@Slf4j
//public class JedisPubSubListener extends JedisPubSub {
//
//    private final AtomicBoolean tag    = new AtomicBoolean();
//
//    private final List<String>  result = new ArrayList<>();
//
//    private CountDownLatch      count  = new CountDownLatch(6);
//
//    public List<String> getResult() { return result; }
//
//    public void clearCache() {
//        result.clear();
//    }
//
//    private void countDown(String model, boolean patternTag) {
//        new Thread(() -> {
//            for (;;) {
//                count.countDown();
//                log.info(model + " countDown: " + count.getCount());
//                try {
//                    Thread.sleep(1000);
//                    if (count.getCount() <= 0) {
//                        if (patternTag) {
//                            punsubscribe(model);
//                        } else {
//                            unsubscribe(model);
//                        }
//                        count = new CountDownLatch(6);
//                        tag.set(false);
//                        log.info("countDown over, unsubscribe model : " + model);
//                        break;
//                    }
//                } catch (InterruptedException e) {
//                    log.error(ExceptionUtils.getRootCauseMessage(e));
//                }
//            }
//        }).start();
//    }
//
//    public void unsubscribed(boolean patternTag, String... model) {
//        if (isSubscribed() && !patternTag) {
//            if (model.length != 0) {
//                unsubscribe(model);
//                result.add(model + " be unsubscribed");
//            } else {
//                unsubscribe();
//                result.add("all channels has been unsubscribed");
//            }
//        } else if (isSubscribed() && patternTag) {
//            if (model.length != 0) {
//                punsubscribe(model);
//                result.add(model + " be unsubscribed");
//            } else {
//                punsubscribe();
//                result.add("all + " + model + " +channels has been unsubscribed");
//            }
//        } else {
//            result.add("no channel is on subscribe");
//        }
//
//    }
//
//    public void onMessage(String channel, String message) {
//        log.info("channel: " + channel + " handle message: " + message);
//        result.add(channel + " receive message: " + message);
//    }
//
//    public void onPMessage(String pattern, String channel, String message) {
//        log.info("channel: " + channel + pattern + "handle message: " + message);
//        result.add("pattern is : " + pattern + ", " + channel + " receive message: " + message);
//    }
//
//    public void onSubscribe(String channel, int subscribedChannels) {
//        log.info("channel " + channel + " has been subscribed number : " + subscribedChannels);
//        if (tag.compareAndSet(false, true)) {
//            countDown(channel, false);
//        }
//    }
//
//    public void onUnsubscribe(String channel, int subscribedChannels) {
//        log.info("channel " + channel + " has been subscribed number : " + subscribedChannels);
//        result.add("channel " + channel + " be unsubscribed ");
//    }
//
//    public void onPUnsubscribe(String pattern, int subscribedChannels) {
//        log.info("pattern " + pattern + " has been subscribed number : " + subscribedChannels);
//        result.add("pattern " + pattern + " be unsubscribed ");
//    }
//
//    public void onPSubscribe(String pattern, int subscribedChannels) {
//        log.info("pattern " + pattern + " has been subscribed number : " + subscribedChannels);
//        if (tag.compareAndSet(false, true)) {
//            countDown(pattern, true);
//        }
//    }
//
//    public void onPong(String pattern) {
//        log.info("pattern : " + pattern);
//    }
//}

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
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//class JedisExecuteUtils {
//    /* -------------------------------------------------------------------------------------------- pubsub commands */
//    public static void cmdSSubScribeRedisCmd(Jedis jedis, SSubscribeRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        String[] strings = redisCmd.getChannels().stream().map(StrToken::getValue).toArray(String[]::new);
//        JedisPubSubListener instance = new JedisPubSubListener();
//        switch (redisCmd.getType().toUpperCase()) {
//            case "SUBSCRIBE": {
//                jedis.subscribe(instance, strings);
//                ResultBuildTools.buildValue(rb, "Value", JacksonUtil.toJson(instance.getResult()));
//                instance.clearCache();
//                break;
//            }
//            case "PSUBSCRIBE": {
//                jedis.psubscribe(instance, strings);
//                ResultBuildTools.buildValue(rb, "Value", JacksonUtil.toJson(instance.getResult()));
//                instance.clearCache();
//                break;
//            }
//            case "SSUBSCRIBE": {
//                // from redis 7.0
//            }
//            default: {
//                throw new UnsupportedOperationException("Unsupported RedisCmdType" + redisCmd.getType());
//            }
//        }
//    }
//
//    public static void cmdPublishRedisCmd(Jedis jedis, PublishRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        long publish = jedis.publish(redisCmd.getChannel().getValue(), redisCmd.getMessage().getValue());
//
//        ResultBuildTools.buildValue(rb, "Value", publish);
//    }
//
//    public static void cmdUnSubscribeCmd(Jedis jedis, UnSubScribeRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        String[] strings = redisCmd.getKeyNames().stream().map(StrToken::getValue).toArray(String[]::new);
//        JedisPubSubListener instance = new JedisPubSubListener();
//        switch (redisCmd.getType().toUpperCase()) {
//            case "UNSUBSCRIBE": {
//                instance.unsubscribed(false, strings);
//                ResultBuildTools.buildValue(rb, "Value", JacksonUtil.toJson(instance.getResult()));
//                instance.clearCache();
//                break;
//            }
//            case "PUNSUBSCRIBE": {
//                instance.unsubscribed(true, strings);
//                ResultBuildTools.buildValue(rb, "Value", JacksonUtil.toJson(instance.getResult()));
//                instance.clearCache();
//                break;
//            }
//            case "SUNSUBSCRIBE": {
//                // from redis 7.0
//            }
//            default: {
//                throw new UnsupportedOperationException("Unsupported RedisCmdType" + redisCmd.getType());
//            }
//        }
//    }
//
//    public static void cmdPuSubNumPatCmd(Jedis jedis, PubSubNumPatRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        Long numPat = jedis.pubsubNumPat();
//
//        ResultBuildTools.buildValue(rb, "Value", numPat);
//    }
//
//    public static void cmdPuSubNumSubCmd(Jedis jedis, NumSubRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        String[] strings = redisCmd.getKeyNames().stream().map(StrToken::getValue).toArray(String[]::new);
//        switch (redisCmd.getType().toUpperCase()) {
//            case "NUMSUB": {
//                Map<String, Long> stringLongMap = jedis.pubsubNumSub(strings);
//                String jsonResult = JacksonUtil.toJson(stringLongMap);
//                ResultBuildTools.buildValue(rb, "Value", jsonResult);
//                break;
//            }
//            case "SHARDNUMSUB": {
//                // from redis 7.0
//            }
//            default: {
//                throw new UnsupportedOperationException("Unsupported RedisCmdType" + redisCmd.getType());
//            }
//        }
//    }
//
//    public static void cmdPuSubChannelsCmd(Jedis jedis, ChannelsRedisCmd redisCmd, ResultSetBuild rb) throws SQLException, IOException {
//        switch (redisCmd.getType().toUpperCase()) {
//            case "CHANNELS": {
//                List<String> result;
//                if (redisCmd.getPattern() == null) {
//                    result = jedis.pubsubChannels();
//                } else {
//                    result = jedis.pubsubChannels(redisCmd.getPattern().getValue());
//                }
//                String jsonResult = JacksonUtil.toJson(result);
//                ResultBuildTools.buildValue(rb, "Value", jsonResult);
//                break;
//            }
//            case "SHARDCHANNELS": {
//                // from redis 7.0
//            }
//            default: {
//                throw new UnsupportedOperationException("Unsupported RedisCmdType" + redisCmd.getType());
//            }
//        }
//    }
//}

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
package com.clougence.clouddm.ds.valkey.execute.jdbc;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import glide.api.GlideClient;
import glide.api.models.GlideString;
import glide.api.models.commands.ExpireOptions;
import glide.api.models.commands.GetExOptions;
import glide.api.models.commands.InfoOptions;
import glide.api.models.commands.HGetExOptions;
import glide.api.models.commands.HashFieldExpirationConditionOptions;
import glide.api.models.commands.HSetExOptions;
import glide.api.models.commands.LInsertOptions.InsertPosition;
import glide.api.models.commands.ListDirection;
import glide.api.models.commands.RangeOptions.LexRange;
import glide.api.models.commands.RangeOptions.RangeByIndex;
import glide.api.models.commands.RangeOptions.RangeByLex;
import glide.api.models.commands.RangeOptions.RangeByScore;
import glide.api.models.commands.RangeOptions.ScoreRange;
import glide.api.models.commands.SetOptions;
import glide.api.models.commands.SetOptions.Expiry;
import glide.api.models.commands.WeightAggregateOptions.KeyArray;
import glide.api.models.commands.bitmap.BitwiseOperation;
import glide.api.models.commands.scan.HScanOptions;
import glide.api.models.commands.scan.SScanOptions;
import glide.api.models.commands.scan.ScanOptions;
import glide.api.models.commands.scan.ZScanOptions;

/**
 * Valkey GLIDE 客户端的同步命令封装：GLIDE 全部命令为异步（CompletableFuture），
 * 此处统一转同步并包装异常，命令方法签名对齐 Jedis 语义以便上层复用命令分发逻辑。
 */
public class GlideCmd {

    private final GlideClient client;

    public GlideCmd(GlideClient client){
        this.client = client;
    }

    public GlideClient getClient() {
        return this.client;
    }

    public void close() {
        try {
            this.client.close();
        } catch (Exception e) {
            // GLIDE 连接关闭失败不影响上层释放
        }
    }

    protected static <T> T await(CompletableFuture<T> future) throws SQLException {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("command interrupted.", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new SQLException(cause.getMessage(), cause);
        }
    }

    // ==================================================================== server
    public String ping() throws SQLException {
        return await(this.client.ping());
    }

    public String ping(String message) throws SQLException {
        return await(this.client.ping(message));
    }

    public String info() throws SQLException {
        return await(this.client.info());
    }

    public String info(String[] sections) throws SQLException {
        InfoOptions.Section[] infoSections = new InfoOptions.Section[sections.length];
        for (int i = 0; i < sections.length; i++) {
            infoSections[i] = InfoOptions.Section.valueOf(sections[i].toUpperCase());
        }
        return await(this.client.info(infoSections));
    }

    public String select(long index) throws SQLException {
        return await(this.client.select(index));
    }

    public long dbsize() throws SQLException {
        return await(this.client.dbsize());
    }

    public Map<String, String> configGet(String[] parameters) throws SQLException {
        return await(this.client.configGet(parameters));
    }

    public String configSet(Map<String, String> parameters) throws SQLException {
        return await(this.client.configSet(parameters));
    }

    public String flushdb() throws SQLException {
        return await(this.client.flushdb());
    }

    public String flushall() throws SQLException {
        return await(this.client.flushall());
    }

    public long waitReplicas(int numReplicas, long timeout) throws SQLException {
        return await(this.client.wait(numReplicas, timeout));
    }

    public Long[] waitAOF(long numLocal, long numReplicas, long timeout) throws SQLException {
        return await(this.client.waitaof(numLocal, numReplicas, timeout));
    }

    // ==================================================================== keys
    public String type(String key) throws SQLException {
        return await(this.client.type(key));
    }

    public String[] keys(String pattern) throws SQLException {
        return await(this.client.keys(pattern));
    }

    public long del(String[] keys) throws SQLException {
        return await(this.client.del(keys));
    }

    public long exists(String[] keys) throws SQLException {
        return await(this.client.exists(keys));
    }

    public boolean expire(String key, long seconds) throws SQLException {
        return await(this.client.expire(key, seconds));
    }

    public boolean expire(String key, long seconds, ExpireOptions options) throws SQLException {
        return await(this.client.expire(key, seconds, options));
    }

    public boolean expireAt(String key, long unixSeconds) throws SQLException {
        return await(this.client.expireAt(key, unixSeconds));
    }

    public boolean expireAt(String key, long unixSeconds, ExpireOptions options) throws SQLException {
        return await(this.client.expireAt(key, unixSeconds, options));
    }

    public boolean pexpire(String key, long milliseconds) throws SQLException {
        return await(this.client.pexpire(key, milliseconds));
    }

    public boolean pexpire(String key, long milliseconds, ExpireOptions options) throws SQLException {
        return await(this.client.pexpire(key, milliseconds, options));
    }

    public boolean pexpireAt(String key, long unixMilliseconds) throws SQLException {
        return await(this.client.pexpireAt(key, unixMilliseconds));
    }

    public boolean pexpireAt(String key, long unixMilliseconds, ExpireOptions options) throws SQLException {
        return await(this.client.pexpireAt(key, unixMilliseconds, options));
    }

    public long expireTime(String key) throws SQLException {
        return await(this.client.expiretime(key));
    }

    public long pexpireTime(String key) throws SQLException {
        return await(this.client.pexpiretime(key));
    }

    public long ttl(String key) throws SQLException {
        return await(this.client.ttl(key));
    }

    public long pttl(String key) throws SQLException {
        return await(this.client.pttl(key));
    }

    public boolean persist(String key) throws SQLException {
        return await(this.client.persist(key));
    }

    public String rename(String key, String newKey) throws SQLException {
        return await(this.client.rename(key, newKey));
    }

    public boolean renamenx(String key, String newKey) throws SQLException {
        return await(this.client.renamenx(key, newKey));
    }

    public String randomKey() throws SQLException {
        return await(this.client.randomKey());
    }

    public Object[] scan(String cursor) throws SQLException {
        return await(this.client.scan(cursor));
    }

    public Object[] scan(String cursor, String pattern, Long count) throws SQLException {
        ScanOptions.ScanOptionsBuilder<?, ?> builder = ScanOptions.builder();
        if (pattern != null) {
            builder.matchPattern(pattern);
        }
        if (count != null) {
            builder.count(count);
        }
        return await(this.client.scan(cursor, builder.build()));
    }

    public Object[] sscan(String key, String cursor) throws SQLException {
        return await(this.client.sscan(key, cursor));
    }

    public Object[] sscan(String key, String cursor, String pattern, Long count) throws SQLException {
        SScanOptions.SScanOptionsBuilder<?, ?> builder = SScanOptions.builder();
        if (pattern != null) {
            builder.matchPattern(pattern);
        }
        if (count != null) {
            builder.count(count);
        }
        return await(this.client.sscan(key, cursor, builder.build()));
    }

    public Object[] hscan(String key, String cursor) throws SQLException {
        return await(this.client.hscan(key, cursor));
    }

    public Object[] hscan(String key, String cursor, String pattern, Long count) throws SQLException {
        HScanOptions.HScanOptionsBuilder<?, ?> builder = HScanOptions.builder();
        if (pattern != null) {
            builder.matchPattern(pattern);
        }
        if (count != null) {
            builder.count(count);
        }
        return await(this.client.hscan(key, cursor, builder.build()));
    }

    public Object[] zscan(String key, String cursor) throws SQLException {
        return await(this.client.zscan(key, cursor));
    }

    public Object[] zscan(String key, String cursor, String pattern, Long count) throws SQLException {
        ZScanOptions.ZScanOptionsBuilder<?, ?> builder = ZScanOptions.builder();
        if (pattern != null) {
            builder.matchPattern(pattern);
        }
        if (count != null) {
            builder.count(count);
        }
        return await(this.client.zscan(key, cursor, builder.build()));
    }

    public byte[] dump(String key) throws SQLException {
        return await(this.client.dump(GlideString.gs(key)));
    }

    public String restore(String key, long ttl, byte[] serializedValue) throws SQLException {
        return await(this.client.restore(GlideString.gs(key), ttl, serializedValue));
    }

    public String[] sort(String key) throws SQLException {
        return await(this.client.sort(key));
    }

    public long touch(String[] keys) throws SQLException {
        return await(this.client.touch(keys));
    }

    public long unlink(String[] keys) throws SQLException {
        return await(this.client.unlink(keys));
    }

    public boolean copy(String source, String destination) throws SQLException {
        return await(this.client.copy(source, destination));
    }

    public boolean copy(String source, String destination, long dbIndex, boolean replace) throws SQLException {
        return await(this.client.copy(source, destination, dbIndex, replace));
    }

    public boolean move(String key, long dbIndex) throws SQLException {
        return await(this.client.move(key, dbIndex));
    }

    public String objectEncoding(String key) throws SQLException {
        return await(this.client.objectEncoding(key));
    }

    public long objectFreq(String key) throws SQLException {
        return await(this.client.objectFreq(key));
    }

    public long objectIdletime(String key) throws SQLException {
        return await(this.client.objectIdletime(key));
    }

    public long objectRefcount(String key) throws SQLException {
        return await(this.client.objectRefcount(key));
    }

    // ==================================================================== string
    public String get(String key) throws SQLException {
        return await(this.client.get(key));
    }

    public String set(String key, String value) throws SQLException {
        return await(this.client.set(key, value));
    }

    public boolean setnx(String key, String value) throws SQLException {
        return await(this.client.set(key, value, SetOptions.builder().conditionalSetOnlyIfNotExist().build())) != null;
    }

    public String setex(String key, long seconds, String value) throws SQLException {
        return await(this.client.set(key, value, SetOptions.builder().expiry(Expiry.Seconds(seconds)).build()));
    }

    public String psetex(String key, long milliseconds, String value) throws SQLException {
        return await(this.client.set(key, value, SetOptions.builder().expiry(Expiry.Milliseconds(milliseconds)).build()));
    }

    public String getSet(String key, String value) throws SQLException {
        return await(this.client.set(key, value, SetOptions.builder().returnOldValue(true).build()));
    }

    public String getDel(String key) throws SQLException {
        return await(this.client.getdel(key));
    }

    public String getEx(String key) throws SQLException {
        return await(this.client.getex(key));
    }

    public String getEx(String key, GetExOptions options) throws SQLException {
        return await(this.client.getex(key, options));
    }

    public String[] mget(String[] keys) throws SQLException {
        return await(this.client.mget(keys));
    }

    public String mset(Map<String, String> keyValueMap) throws SQLException {
        return await(this.client.mset(keyValueMap));
    }

    public boolean msetnx(Map<String, String> keyValueMap) throws SQLException {
        return await(this.client.msetnx(keyValueMap));
    }

    public long incr(String key) throws SQLException {
        return await(this.client.incr(key));
    }

    public long decr(String key) throws SQLException {
        return await(this.client.decr(key));
    }

    public long incrBy(String key, long increment) throws SQLException {
        return await(this.client.incrBy(key, increment));
    }

    public long decrBy(String key, long decrement) throws SQLException {
        return await(this.client.decrBy(key, decrement));
    }

    public double incrByFloat(String key, double increment) throws SQLException {
        return await(this.client.incrByFloat(key, increment));
    }

    public long append(String key, String value) throws SQLException {
        return await(this.client.append(key, value));
    }

    public long strlen(String key) throws SQLException {
        return await(this.client.strlen(key));
    }

    public long setrange(String key, long offset, String value) throws SQLException {
        return await(this.client.setrange(key, (int) offset, value));
    }

    public String getrange(String key, long start, long end) throws SQLException {
        return await(this.client.getrange(key, (int) start, (int) end));
    }

    public String lcs(String key1, String key2) throws SQLException {
        return await(this.client.lcs(key1, key2));
    }

    // ==================================================================== hash
    public long hset(String key, Map<String, String> fieldValues) throws SQLException {
        return await(this.client.hset(key, fieldValues));
    }

    public boolean hsetnx(String key, String field, String value) throws SQLException {
        return await(this.client.hsetnx(key, field, value));
    }

    public String hget(String key, String field) throws SQLException {
        return await(this.client.hget(key, field));
    }

    public Map<String, String> hgetall(String key) throws SQLException {
        return await(this.client.hgetall(key));
    }

    public long hdel(String key, String[] fields) throws SQLException {
        return await(this.client.hdel(key, fields));
    }

    public boolean hexists(String key, String field) throws SQLException {
        return await(this.client.hexists(key, field));
    }

    public long hlen(String key) throws SQLException {
        return await(this.client.hlen(key));
    }

    public String[] hkeys(String key) throws SQLException {
        return await(this.client.hkeys(key));
    }

    public String[] hvals(String key) throws SQLException {
        return await(this.client.hvals(key));
    }

    public String[] hmget(String key, String[] fields) throws SQLException {
        return await(this.client.hmget(key, fields));
    }

    public long hmset(String key, Map<String, String> fieldValues) throws SQLException {
        return await(this.client.hset(key, fieldValues));
    }

    public long hincrBy(String key, String field, long increment) throws SQLException {
        return await(this.client.hincrBy(key, field, increment));
    }

    public double hincrByFloat(String key, String field, double increment) throws SQLException {
        return await(this.client.hincrByFloat(key, field, increment));
    }

    public long hstrlen(String key, String field) throws SQLException {
        return await(this.client.hstrlen(key, field));
    }

    public String hrandfield(String key) throws SQLException {
        return await(this.client.hrandfield(key));
    }

    public String[] hgetex(String key, HGetExOptions options, String[] fields) throws SQLException {
        return await(this.client.hgetex(key, fields, options));
    }

    public long hsetex(String key, Map<String, String> fieldValues, HSetExOptions options) throws SQLException {
        return await(this.client.hsetex(key, fieldValues, options));
    }
    public Long[] hexpire(String key, long seconds, ExpireOptions option, String[] fields) throws SQLException {
        HashFieldExpirationConditionOptions options = option == null ? HashFieldExpirationConditionOptions.builder().build()
                : HashFieldExpirationConditionOptions.builder().condition(option).build();
        return await(this.client.hexpire(key, seconds, fields, options));
    }

    public Long[] hexpireAt(String key, long unixSeconds, ExpireOptions option, String[] fields) throws SQLException {
        HashFieldExpirationConditionOptions options = option == null ? HashFieldExpirationConditionOptions.builder().build()
                : HashFieldExpirationConditionOptions.builder().condition(option).build();
        return await(this.client.hexpireat(key, unixSeconds, fields, options));
    }

    public Long[] hpexpire(String key, long milliseconds, ExpireOptions option, String[] fields) throws SQLException {
        HashFieldExpirationConditionOptions options = option == null ? HashFieldExpirationConditionOptions.builder().build()
                : HashFieldExpirationConditionOptions.builder().condition(option).build();
        return await(this.client.hpexpire(key, milliseconds, fields, options));
    }

    public Long[] hpexpireAt(String key, long unixMilliseconds, ExpireOptions option, String[] fields) throws SQLException {
        HashFieldExpirationConditionOptions options = option == null ? HashFieldExpirationConditionOptions.builder().build()
                : HashFieldExpirationConditionOptions.builder().condition(option).build();
        return await(this.client.hpexpireat(key, unixMilliseconds, fields, options));
    }

    public Long[] hpersist(String key, String[] fields) throws SQLException {
        return await(this.client.hpersist(key, fields));
    }

    public Long[] httl(String key, String[] fields) throws SQLException {
        return await(this.client.httl(key, fields));
    }

    public Long[] hpttl(String key, String[] fields) throws SQLException {
        return await(this.client.hpttl(key, fields));
    }

    public Long[] hexpireTime(String key, String[] fields) throws SQLException {
        return await(this.client.hexpiretime(key, fields));
    }

    public Long[] hpexpireTime(String key, String[] fields) throws SQLException {
        return await(this.client.hpexpiretime(key, fields));
    }


    // ==================================================================== list
    public long lpush(String key, String[] elements) throws SQLException {
        return await(this.client.lpush(key, elements));
    }

    public long rpush(String key, String[] elements) throws SQLException {
        return await(this.client.rpush(key, elements));
    }

    public long lpushx(String key, String[] elements) throws SQLException {
        return await(this.client.lpushx(key, elements));
    }

    public long rpushx(String key, String[] elements) throws SQLException {
        return await(this.client.rpushx(key, elements));
    }

    public String lpop(String key) throws SQLException {
        return await(this.client.lpop(key));
    }

    public String[] lpopCount(String key, long count) throws SQLException {
        return await(this.client.lpopCount(key, count));
    }

    public String rpop(String key) throws SQLException {
        return await(this.client.rpop(key));
    }

    public String[] rpopCount(String key, long count) throws SQLException {
        return await(this.client.rpopCount(key, count));
    }

    public long llen(String key) throws SQLException {
        return await(this.client.llen(key));
    }

    public String[] lrange(String key, long start, long end) throws SQLException {
        return await(this.client.lrange(key, start, end));
    }

    public String lindex(String key, long index) throws SQLException {
        return await(this.client.lindex(key, index));
    }

    public String lset(String key, long index, String value) throws SQLException {
        return await(this.client.lset(key, index, value));
    }

    public String ltrim(String key, long start, long end) throws SQLException {
        return await(this.client.ltrim(key, start, end));
    }

    public long lrem(String key, long count, String value) throws SQLException {
        return await(this.client.lrem(key, count, value));
    }

    public long lpos(String key, String element) throws SQLException {
        return await(this.client.lpos(key, element));
    }

    public String[] blpop(String[] keys, double timeout) throws SQLException {
        return await(this.client.blpop(keys, timeout));
    }

    public Map<String, String[]> blmpop(String[] keys, String direction, double timeout) throws SQLException {
        return await(this.client.blmpop(keys, ListDirection.valueOf(direction.toUpperCase()), timeout));
    }

    public Map<String, String[]> blmpop(String[] keys, String direction, long count, double timeout) throws SQLException {
        return await(this.client.blmpop(keys, ListDirection.valueOf(direction.toUpperCase()), count, timeout));
    }

    public Map<String, String[]> lmpop(String[] keys, String direction) throws SQLException {
        return await(this.client.lmpop(keys, ListDirection.valueOf(direction.toUpperCase())));
    }

    public Map<String, String[]> lmpop(String[] keys, String direction, long count) throws SQLException {
        return await(this.client.lmpop(keys, ListDirection.valueOf(direction.toUpperCase()), count));
    }

    public String[] brpop(String[] keys, double timeout) throws SQLException {
        return await(this.client.brpop(keys, timeout));
    }

    public String lmove(String source, String destination, String whereFrom, String whereTo) throws SQLException {
        return await(this.client.lmove(source, destination, ListDirection.valueOf(whereFrom.toUpperCase()), ListDirection.valueOf(whereTo.toUpperCase())));
    }

    public String blmove(String source, String destination, String whereFrom, String whereTo, double timeout) throws SQLException {
        return await(this.client.blmove(source, destination, ListDirection.valueOf(whereFrom.toUpperCase()), ListDirection.valueOf(whereTo.toUpperCase()), timeout));
    }

    public long linsert(String key, String position, String pivot, String element) throws SQLException {
        return await(this.client.linsert(key, InsertPosition.valueOf(position.toUpperCase()), pivot, element));
    }

    // ==================================================================== set
    public long sadd(String key, String[] members) throws SQLException {
        return await(this.client.sadd(key, members));
    }

    public long srem(String key, String[] members) throws SQLException {
        return await(this.client.srem(key, members));
    }

    public Set<String> smembers(String key) throws SQLException {
        return await(this.client.smembers(key));
    }

    public boolean sismember(String key, String member) throws SQLException {
        return await(this.client.sismember(key, member));
    }

    public long scard(String key) throws SQLException {
        return await(this.client.scard(key));
    }

    public String spop(String key) throws SQLException {
        return await(this.client.spop(key));
    }

    public String[] spop(String key, long count) throws SQLException {
        // GLIDE 暂不支持带 count 的 SPOP
        throw new UnsupportedOperationException("SPOP with count Unsupported by GLIDE.");
    }

    public String srandmember(String key) throws SQLException {
        return await(this.client.srandmember(key));
    }

    public String[] srandmember(String key, long count) throws SQLException {
        return await(this.client.srandmember(key, count));
    }

    public Boolean[] smismember(String key, String[] members) throws SQLException {
        return await(this.client.smismember(key, members));
    }

    public Set<String> sinter(String[] keys) throws SQLException {
        return await(this.client.sinter(keys));
    }

    public Set<String> sunion(String[] keys) throws SQLException {
        return await(this.client.sunion(keys));
    }

    public Set<String> sdiff(String[] keys) throws SQLException {
        return await(this.client.sdiff(keys));
    }

    public long sinterstore(String destination, String[] keys) throws SQLException {
        return await(this.client.sinterstore(destination, keys));
    }

    public long sunionstore(String destination, String[] keys) throws SQLException {
        return await(this.client.sunionstore(destination, keys));
    }

    public long sdiffstore(String destination, String[] keys) throws SQLException {
        return await(this.client.sdiffstore(destination, keys));
    }

    public boolean smove(String source, String destination, String member) throws SQLException {
        return await(this.client.smove(source, destination, member));
    }

    // ==================================================================== zset
    public long zadd(String key, Map<String, Double> membersWithScores) throws SQLException {
        return await(this.client.zadd(key, membersWithScores));
    }

    public double zaddIncr(String key, String member, double score) throws SQLException {
        return await(this.client.zaddIncr(key, member, score));
    }

    public long zrem(String key, String[] members) throws SQLException {
        return await(this.client.zrem(key, members));
    }

    public double zscore(String key, String member) throws SQLException {
        return await(this.client.zscore(key, member));
    }

    public Double[] zmscore(String key, String[] members) throws SQLException {
        return await(this.client.zmscore(key, members));
    }

    public long zcard(String key) throws SQLException {
        return await(this.client.zcard(key));
    }

    public double zincrby(String key, double increment, String member) throws SQLException {
        return await(this.client.zincrby(key, increment, member));
    }

    public long zrank(String key, String member) throws SQLException {
        return await(this.client.zrank(key, member));
    }

    public long zrevrank(String key, String member) throws SQLException {
        return await(this.client.zrevrank(key, member));
    }

    public Object[] zrankWithScore(String key, String member) throws SQLException {
        return await(this.client.zrankWithScore(key, member));
    }

    public Object[] zrevrankWithScore(String key, String member) throws SQLException {
        return await(this.client.zrevrankWithScore(key, member));
    }

    public long zremrangeByRank(String key, long start, long end) throws SQLException {
        return await(this.client.zremrangebyrank(key, start, end));
    }

    public long zremrangeByScore(String key, ScoreRange min, ScoreRange max) throws SQLException {
        return await(this.client.zremrangebyscore(key, min, max));
    }

    public long zremrangeByLex(String key, LexRange min, LexRange max) throws SQLException {
        return await(this.client.zremrangebylex(key, min, max));
    }

    public long zcount(String key, ScoreRange min, ScoreRange max) throws SQLException {
        return await(this.client.zcount(key, min, max));
    }

    public long zlexcount(String key, LexRange min, LexRange max) throws SQLException {
        return await(this.client.zlexcount(key, min, max));
    }

    public String[] zrange(String key, long start, long end, boolean reverse) throws SQLException {
        return await(this.client.zrange(key, new RangeByIndex(start, end), reverse));
    }

    public Map<String, Double> zrangeWithScores(String key, long start, long end, boolean reverse) throws SQLException {
        return await(this.client.zrangeWithScores(key, new RangeByIndex(start, end), reverse));
    }

    public String[] zrangeByScore(String key, ScoreRange min, ScoreRange max, boolean reverse) throws SQLException {
        return await(this.client.zrange(key, new RangeByScore(min, max), reverse));
    }

    public Map<String, Double> zrangeByScoreWithScores(String key, ScoreRange min, ScoreRange max, boolean reverse) throws SQLException {
        return await(this.client.zrangeWithScores(key, new RangeByScore(min, max), reverse));
    }

    public String[] zrangeByLex(String key, LexRange min, LexRange max, boolean reverse) throws SQLException {
        return await(this.client.zrange(key, new RangeByLex(min, max), reverse));
    }

    public String zrandmember(String key) throws SQLException {
        return await(this.client.zrandmember(key));
    }

    public Map<String, Double> zpopmax(String key) throws SQLException {
        return await(this.client.zpopmax(key));
    }

    public Map<String, Double> zpopmin(String key) throws SQLException {
        return await(this.client.zpopmin(key));
    }

    public Object[] bzpopmax(String[] keys, double timeout) throws SQLException {
        return await(this.client.bzpopmax(keys, timeout));
    }

    public Object[] bzpopmin(String[] keys, double timeout) throws SQLException {
        return await(this.client.bzpopmin(keys, timeout));
    }

    public String[] zunion(String[] keys) throws SQLException {
        return await(this.client.zunion(new KeyArray(keys)));
    }

    public String[] zinter(String[] keys) throws SQLException {
        return await(this.client.zinter(new KeyArray(keys)));
    }

    public String[] zdiff(String[] keys) throws SQLException {
        return await(this.client.zdiff(keys));
    }

    public long zunionstore(String destination, String[] keys) throws SQLException {
        return await(this.client.zunionstore(destination, new KeyArray(keys)));
    }

    public long zinterstore(String destination, String[] keys) throws SQLException {
        return await(this.client.zinterstore(destination, new KeyArray(keys)));
    }

    public long zdiffstore(String destination, String[] keys) throws SQLException {
        return await(this.client.zdiffstore(destination, keys));
    }

    // ==================================================================== bitmap
    public long setbit(String key, long offset, long value) throws SQLException {
        return await(this.client.setbit(key, offset, value));
    }

    public long getbit(String key, long offset) throws SQLException {
        return await(this.client.getbit(key, offset));
    }

    public long bitcount(String key) throws SQLException {
        return await(this.client.bitcount(key));
    }

    public long bitcount(String key, long start, long end) throws SQLException {
        return await(this.client.bitcount(key, start, end));
    }

    public long bitpos(String key, long bit) throws SQLException {
        return await(this.client.bitpos(key, bit));
    }

    public long bitop(String operation, String destination, String[] keys) throws SQLException {
        return await(this.client.bitop(BitwiseOperation.valueOf(operation.toUpperCase()), destination, keys));
    }

    // ==================================================================== hyperloglog
    public boolean pfadd(String key, String[] elements) throws SQLException {
        return await(this.client.pfadd(key, elements));
    }

    public long pfcount(String[] keys) throws SQLException {
        return await(this.client.pfcount(keys));
    }

    public String pfmerge(String destination, String[] sources) throws SQLException {
        return await(this.client.pfmerge(destination, sources));
    }
}

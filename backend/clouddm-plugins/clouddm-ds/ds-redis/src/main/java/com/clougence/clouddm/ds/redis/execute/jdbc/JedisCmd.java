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
package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.*;

public class JedisCmd implements AutoCloseable {

    private final AutoCloseable                closeable;
    private AccessControlLogBinaryCommands     accessControlLogBinaryCommands;
    private AccessControlLogCommands           accessControlLogCommands;
    private BitBinaryCommands                  bitBinaryCommands;
    private BitCommands                        bitCommands;
    private BitPipelineBinaryCommands          bitPipelineBinaryCommands;
    private BitPipelineCommands                bitPipelineCommands;
    private ClientBinaryCommands               clientBinaryCommands;
    private ClientCommands                     clientCommands;
    private ClusterCommands                    clusterCommands;
    private CommandCommands                    commandCommands;
    private ConfigCommands                     configCommands;
    private ControlBinaryCommands              controlBinaryCommands;
    private ControlCommands                    controlCommands;
    private DatabaseCommands                   databaseCommands;
    private DatabasePipelineCommands           databasePipelineCommands;
    private FunctionBinaryCommands             functionBinaryCommands;
    private FunctionCommands                   functionCommands;
    private FunctionPipelineBinaryCommands     functionPipelineBinaryCommands;
    private FunctionPipelineCommands           functionPipelineCommands;
    private GenericControlCommands             genericControlCommands;
    private GeoBinaryCommands                  geoBinaryCommands;
    private GeoCommands                        geoCommands;
    private GeoPipelineBinaryCommands          geoPipelineBinaryCommands;
    private GeoPipelineCommands                geoPipelineCommands;
    private HashBinaryCommands                 hashBinaryCommands;
    private HashCommands                       hashCommands;
    private HashPipelineBinaryCommands         hashPipelineBinaryCommands;
    private HashPipelineCommands               hashPipelineCommands;
    private HyperLogLogBinaryCommands          hyperLogLogBinaryCommands;
    private HyperLogLogCommands                hyperLogLogCommands;
    private HyperLogLogPipelineBinaryCommands  hyperLogLogPipelineBinaryCommands;
    private HyperLogLogPipelineCommands        hyperLogLogPipelineCommands;
    private JedisBinaryCommands                jedisBinaryCommands;
    private JedisCommands                      jedisCommands;
    private KeyBinaryCommands                  keyBinaryCommands;
    private KeyCommands                        keyCommands;
    private KeyPipelineBinaryCommands          keyPipelineBinaryCommands;
    private KeyPipelineCommands                keyPipelineCommands;
    private ListBinaryCommands                 listBinaryCommands;
    private ListCommands                       listCommands;
    private ListPipelineBinaryCommands         listPipelineBinaryCommands;
    private ListPipelineCommands               listPipelineCommands;
    private ModuleCommands                     moduleCommands;
    private PipelineBinaryCommands             pipelineBinaryCommands;
    private PipelineCommands                   pipelineCommands;
    private ProtocolCommand                    protocolCommand;
    private RedisModuleCommands                redisModuleCommands;
    private RedisModulePipelineCommands        redisModulePipelineCommands;
    private SampleBinaryKeyedCommands          sampleBinaryKeyedCommands;
    private SampleBinaryKeyedPipelineCommands  sampleBinaryKeyedPipelineCommands;
    private SampleKeyedCommands                sampleKeyedCommands;
    private SampleKeyedPipelineCommands        sampleKeyedPipelineCommands;
    private ScriptingControlCommands           scriptingControlCommands;
    private ScriptingKeyBinaryCommands         scriptingKeyBinaryCommands;
    private ScriptingKeyCommands               scriptingKeyCommands;
    private ScriptingKeyPipelineBinaryCommands scriptingKeyPipelineBinaryCommands;
    private ScriptingKeyPipelineCommands       scriptingKeyPipelineCommands;
    private SentinelCommands                   sentinelCommands;
    private ServerCommands                     serverCommands;
    private SetBinaryCommands                  setBinaryCommands;
    private SetCommands                        setCommands;
    private SetPipelineBinaryCommands          setPipelineBinaryCommands;
    private SetPipelineCommands                setPipelineCommands;
    private SlowlogCommands                    slowlogCommands;
    private SortedSetBinaryCommands            sortedSetBinaryCommands;
    private SortedSetCommands                  sortedSetCommands;
    private SortedSetPipelineBinaryCommands    sortedSetPipelineBinaryCommands;
    private SortedSetPipelineCommands          sortedSetPipelineCommands;
    private StreamBinaryCommands               streamBinaryCommands;
    private StreamCommands                     streamCommands;
    private StreamPipelineBinaryCommands       streamPipelineBinaryCommands;
    private StreamPipelineCommands             streamPipelineCommands;
    private StringBinaryCommands               stringBinaryCommands;
    private StringCommands                     stringCommands;
    private StringPipelineBinaryCommands       stringPipelineBinaryCommands;
    private StringPipelineCommands             stringPipelineCommands;

    public JedisCmd(Jedis jedis, InvocationHandler invocation){
        this.initCommands(jedis, invocation);
        this.closeable = jedis;
    }

    public JedisCmd(JedisCluster jedis, InvocationHandler invocation){
        this.initCommands(jedis, invocation);
        this.closeable = jedis;
    }

    @Override
    public void close() throws IOException {
        try {
            this.closeable.close();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void test() {
        if (this.serverCommands != null) {
            String randomStr = UUID.randomUUID().toString();
            String ping = this.serverCommands.ping(randomStr);
            if (!randomStr.equals(ping)) {
                throw new IllegalStateException("Ping return invalid.");
            }
        }
    }

    void initCommands(Object object, InvocationHandler invocation) {
        this.accessControlLogBinaryCommands = this
            .tryProxy(object instanceof AccessControlLogBinaryCommands ? (AccessControlLogBinaryCommands) object : null, AccessControlLogBinaryCommands.class, invocation);
        this.accessControlLogCommands = this
            .tryProxy(object instanceof AccessControlLogCommands ? (AccessControlLogCommands) object : null, AccessControlLogCommands.class, invocation);
        this.bitBinaryCommands = this.tryProxy(object instanceof BitBinaryCommands ? (BitBinaryCommands) object : null, BitBinaryCommands.class, invocation);
        this.bitCommands = this.tryProxy(object instanceof BitCommands ? (BitCommands) object : null, BitCommands.class, invocation);
        this.bitPipelineBinaryCommands = this
            .tryProxy(object instanceof BitPipelineBinaryCommands ? (BitPipelineBinaryCommands) object : null, BitPipelineBinaryCommands.class, invocation);
        this.bitPipelineCommands = this.tryProxy(object instanceof BitPipelineCommands ? (BitPipelineCommands) object : null, BitPipelineCommands.class, invocation);
        this.clientBinaryCommands = this.tryProxy(object instanceof ClientBinaryCommands ? (ClientBinaryCommands) object : null, ClientBinaryCommands.class, invocation);
        this.clientCommands = this.tryProxy(object instanceof ClientCommands ? (ClientCommands) object : null, ClientCommands.class, invocation);
        this.clusterCommands = this.tryProxy(object instanceof ClusterCommands ? (ClusterCommands) object : null, ClusterCommands.class, invocation);
        this.commandCommands = this.tryProxy(object instanceof CommandCommands ? (CommandCommands) object : null, CommandCommands.class, invocation);
        this.configCommands = this.tryProxy(object instanceof ConfigCommands ? (ConfigCommands) object : null, ConfigCommands.class, invocation);
        this.controlBinaryCommands = this.tryProxy(object instanceof ControlBinaryCommands ? (ControlBinaryCommands) object : null, ControlBinaryCommands.class, invocation);
        this.controlCommands = this.tryProxy(object instanceof ControlCommands ? (ControlCommands) object : null, ControlCommands.class, invocation);
        this.databaseCommands = this.tryProxy(object instanceof DatabaseCommands ? (DatabaseCommands) object : null, DatabaseCommands.class, invocation);
        this.databasePipelineCommands = this
            .tryProxy(object instanceof DatabasePipelineCommands ? (DatabasePipelineCommands) object : null, DatabasePipelineCommands.class, invocation);
        this.functionBinaryCommands = this.tryProxy(object instanceof FunctionBinaryCommands ? (FunctionBinaryCommands) object : null, FunctionBinaryCommands.class, invocation);
        this.functionCommands = this.tryProxy(object instanceof FunctionCommands ? (FunctionCommands) object : null, FunctionCommands.class, invocation);
        this.functionPipelineBinaryCommands = this
            .tryProxy(object instanceof FunctionPipelineBinaryCommands ? (FunctionPipelineBinaryCommands) object : null, FunctionPipelineBinaryCommands.class, invocation);
        this.functionPipelineCommands = this
            .tryProxy(object instanceof FunctionPipelineCommands ? (FunctionPipelineCommands) object : null, FunctionPipelineCommands.class, invocation);
        this.genericControlCommands = this.tryProxy(object instanceof GenericControlCommands ? (GenericControlCommands) object : null, GenericControlCommands.class, invocation);
        this.geoBinaryCommands = this.tryProxy(object instanceof GeoBinaryCommands ? (GeoBinaryCommands) object : null, GeoBinaryCommands.class, invocation);
        this.geoCommands = this.tryProxy(object instanceof GeoCommands ? (GeoCommands) object : null, GeoCommands.class, invocation);
        this.geoPipelineBinaryCommands = this
            .tryProxy(object instanceof GeoPipelineBinaryCommands ? (GeoPipelineBinaryCommands) object : null, GeoPipelineBinaryCommands.class, invocation);
        this.geoPipelineCommands = this.tryProxy(object instanceof GeoPipelineCommands ? (GeoPipelineCommands) object : null, GeoPipelineCommands.class, invocation);
        this.hashBinaryCommands = this.tryProxy(object instanceof HashBinaryCommands ? (HashBinaryCommands) object : null, HashBinaryCommands.class, invocation);
        this.hashCommands = this.tryProxy(object instanceof HashCommands ? (HashCommands) object : null, HashCommands.class, invocation);
        this.hashPipelineBinaryCommands = this
            .tryProxy(object instanceof HashPipelineBinaryCommands ? (HashPipelineBinaryCommands) object : null, HashPipelineBinaryCommands.class, invocation);
        this.hashPipelineCommands = this.tryProxy(object instanceof HashPipelineCommands ? (HashPipelineCommands) object : null, HashPipelineCommands.class, invocation);
        this.hyperLogLogBinaryCommands = this
            .tryProxy(object instanceof HyperLogLogBinaryCommands ? (HyperLogLogBinaryCommands) object : null, HyperLogLogBinaryCommands.class, invocation);
        this.hyperLogLogCommands = this.tryProxy(object instanceof HyperLogLogCommands ? (HyperLogLogCommands) object : null, HyperLogLogCommands.class, invocation);
        this.hyperLogLogPipelineBinaryCommands = this
            .tryProxy(object instanceof HyperLogLogPipelineBinaryCommands ? (HyperLogLogPipelineBinaryCommands) object : null, HyperLogLogPipelineBinaryCommands.class, invocation);
        this.hyperLogLogPipelineCommands = this
            .tryProxy(object instanceof HyperLogLogPipelineCommands ? (HyperLogLogPipelineCommands) object : null, HyperLogLogPipelineCommands.class, invocation);
        this.jedisBinaryCommands = this.tryProxy(object instanceof JedisBinaryCommands ? (JedisBinaryCommands) object : null, JedisBinaryCommands.class, invocation);
        this.jedisCommands = this.tryProxy(object instanceof JedisCommands ? (JedisCommands) object : null, JedisCommands.class, invocation);
        this.keyBinaryCommands = this.tryProxy(object instanceof KeyBinaryCommands ? (KeyBinaryCommands) object : null, KeyBinaryCommands.class, invocation);
        this.keyCommands = this.tryProxy(object instanceof KeyCommands ? (KeyCommands) object : null, KeyCommands.class, invocation);
        this.keyPipelineBinaryCommands = this
            .tryProxy(object instanceof KeyPipelineBinaryCommands ? (KeyPipelineBinaryCommands) object : null, KeyPipelineBinaryCommands.class, invocation);
        this.keyPipelineCommands = this.tryProxy(object instanceof KeyPipelineCommands ? (KeyPipelineCommands) object : null, KeyPipelineCommands.class, invocation);
        this.listBinaryCommands = this.tryProxy(object instanceof ListBinaryCommands ? (ListBinaryCommands) object : null, ListBinaryCommands.class, invocation);
        this.listCommands = this.tryProxy(object instanceof ListCommands ? (ListCommands) object : null, ListCommands.class, invocation);
        this.listPipelineBinaryCommands = this
            .tryProxy(object instanceof ListPipelineBinaryCommands ? (ListPipelineBinaryCommands) object : null, ListPipelineBinaryCommands.class, invocation);
        this.listPipelineCommands = this.tryProxy(object instanceof ListPipelineCommands ? (ListPipelineCommands) object : null, ListPipelineCommands.class, invocation);
        this.moduleCommands = this.tryProxy(object instanceof ModuleCommands ? (ModuleCommands) object : null, ModuleCommands.class, invocation);
        this.pipelineBinaryCommands = this.tryProxy(object instanceof PipelineBinaryCommands ? (PipelineBinaryCommands) object : null, PipelineBinaryCommands.class, invocation);
        this.pipelineCommands = this.tryProxy(object instanceof PipelineCommands ? (PipelineCommands) object : null, PipelineCommands.class, invocation);
        this.protocolCommand = this.tryProxy(object instanceof ProtocolCommand ? (ProtocolCommand) object : null, ProtocolCommand.class, invocation);
        this.redisModuleCommands = this.tryProxy(object instanceof RedisModuleCommands ? (RedisModuleCommands) object : null, RedisModuleCommands.class, invocation);
        this.redisModulePipelineCommands = this
            .tryProxy(object instanceof RedisModulePipelineCommands ? (RedisModulePipelineCommands) object : null, RedisModulePipelineCommands.class, invocation);
        this.sampleBinaryKeyedCommands = this
            .tryProxy(object instanceof SampleBinaryKeyedCommands ? (SampleBinaryKeyedCommands) object : null, SampleBinaryKeyedCommands.class, invocation);
        this.sampleBinaryKeyedPipelineCommands = this
            .tryProxy(object instanceof SampleBinaryKeyedPipelineCommands ? (SampleBinaryKeyedPipelineCommands) object : null, SampleBinaryKeyedPipelineCommands.class, invocation);
        this.sampleKeyedCommands = this.tryProxy(object instanceof SampleKeyedCommands ? (SampleKeyedCommands) object : null, SampleKeyedCommands.class, invocation);
        this.sampleKeyedPipelineCommands = this
            .tryProxy(object instanceof SampleKeyedPipelineCommands ? (SampleKeyedPipelineCommands) object : null, SampleKeyedPipelineCommands.class, invocation);
        this.scriptingControlCommands = this
            .tryProxy(object instanceof ScriptingControlCommands ? (ScriptingControlCommands) object : null, ScriptingControlCommands.class, invocation);
        this.scriptingKeyBinaryCommands = this
            .tryProxy(object instanceof ScriptingKeyBinaryCommands ? (ScriptingKeyBinaryCommands) object : null, ScriptingKeyBinaryCommands.class, invocation);
        this.scriptingKeyCommands = this.tryProxy(object instanceof ScriptingKeyCommands ? (ScriptingKeyCommands) object : null, ScriptingKeyCommands.class, invocation);
        this.scriptingKeyPipelineBinaryCommands = this
            .tryProxy(object instanceof ScriptingKeyPipelineBinaryCommands ? (ScriptingKeyPipelineBinaryCommands) object : null, ScriptingKeyPipelineBinaryCommands.class, invocation);
        this.scriptingKeyPipelineCommands = this
            .tryProxy(object instanceof ScriptingKeyPipelineCommands ? (ScriptingKeyPipelineCommands) object : null, ScriptingKeyPipelineCommands.class, invocation);
        this.sentinelCommands = this.tryProxy(object instanceof SentinelCommands ? (SentinelCommands) object : null, SentinelCommands.class, invocation);
        this.serverCommands = this.tryProxy(object instanceof ServerCommands ? (ServerCommands) object : null, ServerCommands.class, invocation);
        this.setBinaryCommands = this.tryProxy(object instanceof SetBinaryCommands ? (SetBinaryCommands) object : null, SetBinaryCommands.class, invocation);
        this.setCommands = this.tryProxy(object instanceof SetCommands ? (SetCommands) object : null, SetCommands.class, invocation);
        this.setPipelineBinaryCommands = this
            .tryProxy(object instanceof SetPipelineBinaryCommands ? (SetPipelineBinaryCommands) object : null, SetPipelineBinaryCommands.class, invocation);
        this.setPipelineCommands = this.tryProxy(object instanceof SetPipelineCommands ? (SetPipelineCommands) object : null, SetPipelineCommands.class, invocation);
        this.slowlogCommands = this.tryProxy(object instanceof SlowlogCommands ? (SlowlogCommands) object : null, SlowlogCommands.class, invocation);
        this.sortedSetBinaryCommands = this
            .tryProxy(object instanceof SortedSetBinaryCommands ? (SortedSetBinaryCommands) object : null, SortedSetBinaryCommands.class, invocation);
        this.sortedSetCommands = this.tryProxy(object instanceof SortedSetCommands ? (SortedSetCommands) object : null, SortedSetCommands.class, invocation);
        this.sortedSetPipelineBinaryCommands = this
            .tryProxy(object instanceof SortedSetPipelineBinaryCommands ? (SortedSetPipelineBinaryCommands) object : null, SortedSetPipelineBinaryCommands.class, invocation);
        this.sortedSetPipelineCommands = this
            .tryProxy(object instanceof SortedSetPipelineCommands ? (SortedSetPipelineCommands) object : null, SortedSetPipelineCommands.class, invocation);
        this.streamBinaryCommands = this.tryProxy(object instanceof StreamBinaryCommands ? (StreamBinaryCommands) object : null, StreamBinaryCommands.class, invocation);
        this.streamCommands = this.tryProxy(object instanceof StreamCommands ? (StreamCommands) object : null, StreamCommands.class, invocation);
        this.streamPipelineBinaryCommands = this
            .tryProxy(object instanceof StreamPipelineBinaryCommands ? (StreamPipelineBinaryCommands) object : null, StreamPipelineBinaryCommands.class, invocation);
        this.streamPipelineCommands = this.tryProxy(object instanceof StreamPipelineCommands ? (StreamPipelineCommands) object : null, StreamPipelineCommands.class, invocation);
        this.stringBinaryCommands = this.tryProxy(object instanceof StringBinaryCommands ? (StringBinaryCommands) object : null, StringBinaryCommands.class, invocation);
        this.stringCommands = this.tryProxy(object instanceof StringCommands ? (StringCommands) object : null, StringCommands.class, invocation);
        this.stringPipelineBinaryCommands = this
            .tryProxy(object instanceof StringPipelineBinaryCommands ? (StringPipelineBinaryCommands) object : null, StringPipelineBinaryCommands.class, invocation);
        this.stringPipelineCommands = this.tryProxy(object instanceof StringPipelineCommands ? (StringPipelineCommands) object : null, StringPipelineCommands.class, invocation);
    }

    private <T> T tryProxy(T object, Class<T> iface, InvocationHandler invocation) {
        if (object == null || invocation == null) {
            return object;
        } else {
            return (T) Proxy.newProxyInstance(JedisCmd.class.getClassLoader(), new Class<?>[] { iface }, (proxy, method, args) -> {
                return invocation.invoke(object, method, args);
            });
        }
    }

    private <T> T verifyCommand(T command, Class<T> commandClass) {
        if (command == null) {
            throw new UnsupportedOperationException("The command " + commandClass.getName() + " is not supported.");
        } else {
            return command;
        }
    }

    public AccessControlLogBinaryCommands getAccessControlLogBinaryCommands() {
        return this.verifyCommand(this.accessControlLogBinaryCommands, AccessControlLogBinaryCommands.class);
    }

    public AccessControlLogCommands getAccessControlLogCommands() { return this.verifyCommand(this.accessControlLogCommands, AccessControlLogCommands.class); }

    public BitBinaryCommands getBitBinaryCommands() { return this.verifyCommand(this.bitBinaryCommands, BitBinaryCommands.class); }

    public BitCommands getBitCommands() { return this.verifyCommand(this.bitCommands, BitCommands.class); }

    public BitPipelineBinaryCommands getBitPipelineBinaryCommands() { return this.verifyCommand(this.bitPipelineBinaryCommands, BitPipelineBinaryCommands.class); }

    public BitPipelineCommands getBitPipelineCommands() { return this.verifyCommand(this.bitPipelineCommands, BitPipelineCommands.class); }

    public ClientBinaryCommands getClientBinaryCommands() { return this.verifyCommand(this.clientBinaryCommands, ClientBinaryCommands.class); }

    public ClientCommands getClientCommands() { return this.verifyCommand(this.clientCommands, ClientCommands.class); }

    public ClusterCommands getClusterCommands() { return this.verifyCommand(this.clusterCommands, ClusterCommands.class); }

    public CommandCommands getCommandCommands() { return this.verifyCommand(this.commandCommands, CommandCommands.class); }

    public ConfigCommands getConfigCommands() { return this.verifyCommand(this.configCommands, ConfigCommands.class); }

    public ControlBinaryCommands getControlBinaryCommands() { return this.verifyCommand(this.controlBinaryCommands, ControlBinaryCommands.class); }

    public ControlCommands getControlCommands() { return this.verifyCommand(this.controlCommands, ControlCommands.class); }

    public DatabaseCommands getDatabaseCommands() { return this.verifyCommand(this.databaseCommands, DatabaseCommands.class); }

    public DatabasePipelineCommands getDatabasePipelineCommands() { return this.verifyCommand(this.databasePipelineCommands, DatabasePipelineCommands.class); }

    public FunctionBinaryCommands getFunctionBinaryCommands() { return this.verifyCommand(this.functionBinaryCommands, FunctionBinaryCommands.class); }

    public FunctionCommands getFunctionCommands() { return this.verifyCommand(this.functionCommands, FunctionCommands.class); }

    public FunctionPipelineBinaryCommands getFunctionPipelineBinaryCommands() {
        return this.verifyCommand(this.functionPipelineBinaryCommands, FunctionPipelineBinaryCommands.class);
    }

    public FunctionPipelineCommands getFunctionPipelineCommands() { return this.verifyCommand(this.functionPipelineCommands, FunctionPipelineCommands.class); }

    public GenericControlCommands getGenericControlCommands() { return this.verifyCommand(this.genericControlCommands, GenericControlCommands.class); }

    public GeoBinaryCommands getGeoBinaryCommands() { return this.verifyCommand(this.geoBinaryCommands, GeoBinaryCommands.class); }

    public GeoCommands getGeoCommands() { return this.verifyCommand(this.geoCommands, GeoCommands.class); }

    public GeoPipelineBinaryCommands getGeoPipelineBinaryCommands() { return this.verifyCommand(this.geoPipelineBinaryCommands, GeoPipelineBinaryCommands.class); }

    public GeoPipelineCommands getGeoPipelineCommands() { return this.verifyCommand(this.geoPipelineCommands, GeoPipelineCommands.class); }

    public HashBinaryCommands getHashBinaryCommands() { return this.verifyCommand(this.hashBinaryCommands, HashBinaryCommands.class); }

    public HashCommands getHashCommands() { return this.verifyCommand(this.hashCommands, HashCommands.class); }

    public HashPipelineBinaryCommands getHashPipelineBinaryCommands() { return this.verifyCommand(this.hashPipelineBinaryCommands, HashPipelineBinaryCommands.class); }

    public HashPipelineCommands getHashPipelineCommands() { return this.verifyCommand(this.hashPipelineCommands, HashPipelineCommands.class); }

    public HyperLogLogBinaryCommands getHyperLogLogBinaryCommands() { return this.verifyCommand(this.hyperLogLogBinaryCommands, HyperLogLogBinaryCommands.class); }

    public HyperLogLogCommands getHyperLogLogCommands() { return this.verifyCommand(this.hyperLogLogCommands, HyperLogLogCommands.class); }

    public HyperLogLogPipelineBinaryCommands getHyperLogLogPipelineBinaryCommands() {
        return this.verifyCommand(this.hyperLogLogPipelineBinaryCommands, HyperLogLogPipelineBinaryCommands.class);
    }

    public HyperLogLogPipelineCommands getHyperLogLogPipelineCommands() { return this.verifyCommand(this.hyperLogLogPipelineCommands, HyperLogLogPipelineCommands.class); }

    public JedisBinaryCommands getJedisBinaryCommands() { return this.verifyCommand(this.jedisBinaryCommands, JedisBinaryCommands.class); }

    public JedisCommands getJedisCommands() { return this.verifyCommand(this.jedisCommands, JedisCommands.class); }

    public KeyBinaryCommands getKeyBinaryCommands() { return this.verifyCommand(this.keyBinaryCommands, KeyBinaryCommands.class); }

    public KeyCommands getKeyCommands() { return this.verifyCommand(this.keyCommands, KeyCommands.class); }

    public KeyPipelineBinaryCommands getKeyPipelineBinaryCommands() { return this.verifyCommand(this.keyPipelineBinaryCommands, KeyPipelineBinaryCommands.class); }

    public KeyPipelineCommands getKeyPipelineCommands() { return this.verifyCommand(this.keyPipelineCommands, KeyPipelineCommands.class); }

    public ListBinaryCommands getListBinaryCommands() { return this.verifyCommand(this.listBinaryCommands, ListBinaryCommands.class); }

    public ListCommands getListCommands() { return this.verifyCommand(this.listCommands, ListCommands.class); }

    public ListPipelineBinaryCommands getListPipelineBinaryCommands() { return this.verifyCommand(this.listPipelineBinaryCommands, ListPipelineBinaryCommands.class); }

    public ListPipelineCommands getListPipelineCommands() { return this.verifyCommand(this.listPipelineCommands, ListPipelineCommands.class); }

    public ModuleCommands getModuleCommands() { return this.verifyCommand(this.moduleCommands, ModuleCommands.class); }

    public PipelineBinaryCommands getPipelineBinaryCommands() { return this.verifyCommand(this.pipelineBinaryCommands, PipelineBinaryCommands.class); }

    public PipelineCommands getPipelineCommands() { return this.verifyCommand(this.pipelineCommands, PipelineCommands.class); }

    public ProtocolCommand getProtocolCommand() { return this.verifyCommand(this.protocolCommand, ProtocolCommand.class); }

    public RedisModuleCommands getRedisModuleCommands() { return this.verifyCommand(this.redisModuleCommands, RedisModuleCommands.class); }

    public RedisModulePipelineCommands getRedisModulePipelineCommands() { return this.verifyCommand(this.redisModulePipelineCommands, RedisModulePipelineCommands.class); }

    public SampleBinaryKeyedCommands getSampleBinaryKeyedCommands() { return this.verifyCommand(this.sampleBinaryKeyedCommands, SampleBinaryKeyedCommands.class); }

    public SampleBinaryKeyedPipelineCommands getSampleBinaryKeyedPipelineCommands() {
        return this.verifyCommand(this.sampleBinaryKeyedPipelineCommands, SampleBinaryKeyedPipelineCommands.class);
    }

    public SampleKeyedCommands getSampleKeyedCommands() { return this.verifyCommand(this.sampleKeyedCommands, SampleKeyedCommands.class); }

    public SampleKeyedPipelineCommands getSampleKeyedPipelineCommands() { return this.verifyCommand(this.sampleKeyedPipelineCommands, SampleKeyedPipelineCommands.class); }

    public ScriptingControlCommands getScriptingControlCommands() { return this.verifyCommand(this.scriptingControlCommands, ScriptingControlCommands.class); }

    public ScriptingKeyBinaryCommands getScriptingKeyBinaryCommands() { return this.verifyCommand(this.scriptingKeyBinaryCommands, ScriptingKeyBinaryCommands.class); }

    public ScriptingKeyCommands getScriptingKeyCommands() { return this.verifyCommand(this.scriptingKeyCommands, ScriptingKeyCommands.class); }

    public ScriptingKeyPipelineBinaryCommands getScriptingKeyPipelineBinaryCommands() {
        return this.verifyCommand(this.scriptingKeyPipelineBinaryCommands, ScriptingKeyPipelineBinaryCommands.class);
    }

    public ScriptingKeyPipelineCommands getScriptingKeyPipelineCommands() { return this.verifyCommand(this.scriptingKeyPipelineCommands, ScriptingKeyPipelineCommands.class); }

    public SentinelCommands getSentinelCommands() { return this.verifyCommand(this.sentinelCommands, SentinelCommands.class); }

    public ServerCommands getServerCommands() { return this.verifyCommand(this.serverCommands, ServerCommands.class); }

    public SetBinaryCommands getSetBinaryCommands() { return this.verifyCommand(this.setBinaryCommands, SetBinaryCommands.class); }

    public SetCommands getSetCommands() { return this.verifyCommand(this.setCommands, SetCommands.class); }

    public SetPipelineBinaryCommands getSetPipelineBinaryCommands() { return this.verifyCommand(this.setPipelineBinaryCommands, SetPipelineBinaryCommands.class); }

    public SetPipelineCommands getSetPipelineCommands() { return this.verifyCommand(this.setPipelineCommands, SetPipelineCommands.class); }

    public SlowlogCommands getSlowlogCommands() { return this.verifyCommand(this.slowlogCommands, SlowlogCommands.class); }

    public SortedSetBinaryCommands getSortedSetBinaryCommands() { return this.verifyCommand(this.sortedSetBinaryCommands, SortedSetBinaryCommands.class); }

    public SortedSetCommands getSortedSetCommands() { return this.verifyCommand(this.sortedSetCommands, SortedSetCommands.class); }

    public SortedSetPipelineBinaryCommands getSortedSetPipelineBinaryCommands() {
        return this.verifyCommand(this.sortedSetPipelineBinaryCommands, SortedSetPipelineBinaryCommands.class);
    }

    public SortedSetPipelineCommands getSortedSetPipelineCommands() { return this.verifyCommand(this.sortedSetPipelineCommands, SortedSetPipelineCommands.class); }

    public StreamBinaryCommands getStreamBinaryCommands() { return this.verifyCommand(this.streamBinaryCommands, StreamBinaryCommands.class); }

    public StreamCommands getStreamCommands() { return this.verifyCommand(this.streamCommands, StreamCommands.class); }

    public StreamPipelineBinaryCommands getStreamPipelineBinaryCommands() { return this.verifyCommand(this.streamPipelineBinaryCommands, StreamPipelineBinaryCommands.class); }

    public StreamPipelineCommands getStreamPipelineCommands() { return this.verifyCommand(this.streamPipelineCommands, StreamPipelineCommands.class); }

    public StringBinaryCommands getStringBinaryCommands() { return this.verifyCommand(this.stringBinaryCommands, StringBinaryCommands.class); }

    public StringCommands getStringCommands() { return this.verifyCommand(this.stringCommands, StringCommands.class); }

    public StringPipelineBinaryCommands getStringPipelineBinaryCommands() { return this.verifyCommand(this.stringPipelineBinaryCommands, StringPipelineBinaryCommands.class); }

    public StringPipelineCommands getStringPipelineCommands() { return this.verifyCommand(this.stringPipelineCommands, StringPipelineCommands.class); }
}

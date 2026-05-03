package com.clougence.rdp.controller;

import static com.clougence.clouddm.base.metadata.ds.DataSourceType.*;

import java.util.*;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.controller.model.fo.ConnectTypeByDsFO;
import com.clougence.rdp.controller.model.fo.DsSecurityOptionFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.ConnectTypeVO;
import com.clougence.rdp.controller.model.vo.DsSecurityDetailVO;
import com.clougence.rdp.controller.model.vo.DsSecurityOption;
import com.clougence.rdp.dal.enumeration.DeployEnvInfoFetchType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.util.RdpI18nUtils;

/**
 * @author bucketli 2020/12/30 12:17
 */
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/constant/ds")
public class RdpDsConstantController {

    public static final List<DataSourceType> LLM_EMBEDDING_DS = new ArrayList<>();
    public static final List<DataSourceType> LLM_CHAT_DS      = new ArrayList<>();

    static {
        LLM_EMBEDDING_DS.addAll(Arrays.asList(OpenAI, DashScope, HuggingFace, Cohere, LocalAI, Ollama, ZhipuAI, Anthropic, Bedrock));
        LLM_CHAT_DS.addAll(Arrays.asList(OpenAI, DashScope, HuggingFace, Cohere, DeepSeek, LocalAI, Ollama, ZhipuAI, Anthropic, Bedrock));
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/dsllmembeddingtypes", method = RequestMethod.POST)
    public ResWebData<?> dsLlmEmbeddingTypes() {
        return ResWebDataUtils.buildSuccess(LLM_EMBEDDING_DS);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/dsllmchattypes", method = RequestMethod.POST)
    public ResWebData<?> dsLlmChatTypes() {
        return ResWebDataUtils.buildSuccess(LLM_CHAT_DS);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/dsconnecttype", method = RequestMethod.POST)
    public ResWebData<?> dsConnectType() {
        List<ConnectTypeVO> vos = new ArrayList<>();
        for (ConnectType c : ConnectType.values()) {
            ConnectTypeVO v;
            if (c == ConnectType.ORACLE_SID || c == ConnectType.CLICKHOUSE_HTTP) {
                v = new ConnectTypeVO(c, true);
            } else {
                v = new ConnectTypeVO(c, false);
            }

            vos.add(v);
        }

        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/connecttypebyds", method = RequestMethod.POST)
    public ResWebData<?> connectTypeByDs(@Valid @RequestBody ConnectTypeByDsFO dsFO) {
        List<ConnectTypeVO> vos = new ArrayList<>();
        for (ConnectType c : ConnectType.values()) {
            if (dsFO.getDataSourceType() != c.getDsType()) {
                continue;
            }

            ConnectTypeVO v;
            if (c == ConnectType.ORACLE_SID || c == ConnectType.CLICKHOUSE_HTTP) {
                v = new ConnectTypeVO(c, true);
            } else {
                v = new ConnectTypeVO(c, false);
            }

            vos.add(v);
        }

        return ResWebDataUtils.buildSuccess(vos);
    }

    private static final String DEFAULT_DB_NAME_LABEL_PREFIX = "DEFAULT_DB_NAME_LABEL_";
    public static final String  DEFAULT_DB_NAME_LABEL_RDB    = DEFAULT_DB_NAME_LABEL_PREFIX + "RDB";

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/dssecurityoption", method = RequestMethod.POST)
    public ResWebData<?> dsSecurityOption(@Valid @RequestBody DsSecurityOptionFO data) {
        DsSecurityDetailVO detail = new DsSecurityDetailVO();

        // for historical metadata compatibility
        if (data.getDeployFetchType() == null) {
            if (data.getDeployEnvType() == DeployEnvType.ALIBABA_CLOUD_HOSTED) {
                data.setDeployFetchType(DeployEnvInfoFetchType.OPENAPI);
            } else {
                data.setDeployFetchType(DeployEnvInfoFetchType.MANUALLY_FILL);
            }
        }

        if (data.getDeployEnvType() == DeployEnvType.SELF_MAINTENANCE) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                switch (data.getDataSourceType()) {
                    case MariaDB:
                    case TiDB:
                    case StarRocks:
                    case Doris:
                    case SelectDB:
                    case GaussDBForOpenGauss:
                    case Greenplum:
                    case GreptimeDB:
                    case TDengine:
                    case KingbaseES: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case GaussDB: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needDbName(true) // dm need, cc will hide
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.ONLY_USER)
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .needUserName(true)
                                .needPassword(false)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.NONE)
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .needUserName(false)
                                .needPassword(false)
                                .build());
                        break;
                    }
                    case MySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFile(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsKeyStoreFile(true)
                                .needTlsKeyStoreFilePassword(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case PostgreSQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.CA_CERTIFICATE)
                                .needUserName(true)
                                .needPassword(true)
                                .needCaFile(true)
                                .needClientCaFile(true)
                                .needClientKeyFile(true)
                                .needSecretFilePassword(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case DuckDB: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case Dameng: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_KEYSTORE)
                                .needUserName(true)
                                .needPassword(true)
                                .needKeystoreFile(true)
                                .needKeystoreFilePassword(true)
                                .build());
                        break;
                    }
                    case OceanBase:
                    case ObForOracle:
                    case Lindorm:
                    case SshFile: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        break;
                    }
                    case RocketMQ: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case PolarDbX:
                    case Oracle:
                    case RabbitMQ:
                    case ClickHouse:
                    case MongoDB:
                    case DocumentDB: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case SQLServer:
                    case Hana: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case ElasticSearch: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.CA_CERTIFICATE).needUserName(true).needPassword(true).needCaFile(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case Redis: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.ONLY_PASSWD)
                                .needUserName(false)
                                .needPassword(true)
                                .needCaFile(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .needCaFile(true)
                                .defaultCheck(false)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).needCaFile(true).build());
                        break;
                    }
                    case AutoMQ:
                    case AmazonMSK:
                    case Kafka: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsTrustStoreFile(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.KERBEROS)
                                .needUserName(true)
                                .isUserNamePrinciple(true)
                                .needPassword(false)
                                .needKeyTabFile(true)
                                .needKrb5File(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD_WITH_SCRAM).needUserName(true).needPassword(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).defaultCheck(true).build());
                        break;
                    }
                    case Hive:
                    case Hudi: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.KERBEROS)
                                .needUserName(true)
                                .isUserNamePrinciple(true)
                                .needPassword(false)
                                .needKeyTabFile(true)
                                .needKrb5File(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).defaultCheck(true).build());
                        break;
                    }
                    case DeltaLake:
                    case Iceberg: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).defaultCheck(true).build());
                        break;
                    }
                    case Paimon: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).defaultCheck(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.KERBEROS)
                                .needUserName(true)
                                .isUserNamePrinciple(true)
                                .needPassword(false)
                                .needKeyTabFile(true)
                                .needKrb5File(true)
                                .build());
                        break;
                    }
                    case Tunnel: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsTrustStoreFile(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).build());
                        break;
                    }
                    case Db2:
                    case Db2Fori: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case Kudu:
                    case Pulsar: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).defaultCheck(true).build());
                        break;
                    }
                    case RagApi: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.API_KEY).defaultHost("localhost:18089").needApiKey(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).build());
                        break;
                    }
                    case LocalAI: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).defaultHost("localhost:8082/v1/").defaultCheck(true).build());
                        break;
                    }
                    case Ollama: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).defaultHost("localhost:11434").defaultCheck(true).build());
                        break;
                    }
                    case GoogleDrive: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).defaultHost("googleapis.com").defaultCheck(true).build());
                        break;
                    }
                    case Yuque: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultHost("www.yuque.com")
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unsupported datasource type:" + data.getDataSourceType());
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.ALIBABA_CLOUD_HOSTED) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.OPENAPI) {
                switch (data.getDataSourceType()) {
                    case MariaDB: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .hasAutoGenAccountPasswdOption(true)
                                .checkAutoGenAccountPasswdOption(false)
                                .hasAutoCreateAccountOption(true)
                                .checkAutoCreateAccountOption(true)
                                .needExtraAliyunAkSk(true)
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case MySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .hasAutoGenAccountPasswdOption(true)
                                .checkAutoGenAccountPasswdOption(false)
                                .hasAutoCreateAccountOption(true)
                                .checkAutoCreateAccountOption(true)
                                .needExtraAliyunAkSk(true)
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .hasAutoGenAccountPasswdOption(true)
                                .checkAutoGenAccountPasswdOption(false)
                                .hasAutoCreateAccountOption(true)
                                .checkAutoCreateAccountOption(true)
                                .needExtraAliyunAkSk(true)
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFile(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsKeyStoreFile(true)
                                .needTlsKeyStoreFilePassword(true)
                                .build());
                        break;
                    }
                    case AdbForMySQL:
                    case PolarDbMySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .hasAutoGenAccountPasswdOption(true)
                                .checkAutoGenAccountPasswdOption(false)
                                .hasAutoCreateAccountOption(false)
                                .checkAutoCreateAccountOption(false)
                                .needExtraAliyunAkSk(false)
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case ElasticSearch: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        break;
                    }
                    case Greenplum:
                    case SQLServer:
                    case OceanBase:
                    case ClickHouse:
                    case MongoDB:
                    case PolarDbX:
                    case Lindorm: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needExtraAliyunAkSk(true)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case PolarDBPg:
                    case PostgreSQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needExtraAliyunAkSk(true)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.CA_CERTIFICATE)
                                .needUserName(true)
                                .needPassword(true)
                                .needCaFile(true)
                                .needClientCaFile(true)
                                .needClientKeyFile(true)
                                .needSecretFilePassword(true)
                                .build());
                        break;
                    }
                    case Redis: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.ONLY_PASSWD).needUserName(false).needPassword(true).defaultCheck(false).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        break;
                    }
                    case RocketMQ: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needExtraAliyunAkSk(true).defaultCheck(false).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needExtraAliyunAkSk(true)
                                .isAccountAliyunAkSk(false)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case RabbitMQ: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .isAccountAliyunAkSk(true)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case AmazonMSK:
                    case Kafka: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needExtraAliyunAkSk(true).needUserName(false).needPassword(false).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needExtraAliyunAkSk(true)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsTrustStoreFile(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("unsupported datasource type:" + data.getDataSourceType());
                }
            } else if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                switch (data.getDataSourceType()) {
                    case DataLakeFormation:
                    case OssFile: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).defaultCheck(true).build());
                        break;
                    }
                    case DashScope: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.API_KEY)
                                .defaultHost("dashscope.aliyuncs.com/compatible-mode/v1/")
                                .needApiKey(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).build());
                        break;
                    }
                    case MySQL:
                    case PolarDbMySQL:
                    case PolarDbX:
                    case PostgreSQL:
                    case PolarDBPg:
                    case SQLServer:
                    case AdbForMySQL:
                    case Greenplum: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needAkSk(true)
                                .needInstanceId(true)
                                .needUserName(true)
                                .needPassword(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case MaxCompute:
                    case Hologres: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()//
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .securityType(SecurityType.AK_SK)
                                .needAkSk(true)
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unsupported datasource type:" + data.getDataSourceType());
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.AWS_CLOUD_HOSTED) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                switch (data.getDataSourceType()) {
                    case PostgreSQL:
                    case AuroraPostgreSQL:
                    case MySQL:
                    case MariaDB:
                    case AuroraMySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case Oracle:
                    case SQLServer:
                    case DocumentDB: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case DynamoDB: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).defaultCheck(true).build());
                        break;
                    }
                    case Redshift: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .needDbName(true)
                                .dbNameLabel(RdpI18nUtils.getMessage(DEFAULT_DB_NAME_LABEL_RDB))
                                .defaultCheck(true)
                                .build());
                        break;
                    }
                    case Bedrock:
                    case S3File: {
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.AK_SK).needAkSk(true).defaultCheck(true).build());
                        break;
                    }
                    case ElastiCache: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.ONLY_PASSWD)
                                .needUserName(false)
                                .needPassword(true)
                                .needCaFile(true)
                                .defaultCheck(true)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD)
                                .needUserName(true)
                                .needPassword(true)
                                .needCaFile(true)
                                .defaultCheck(false)
                                .build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).needCaFile(true).build());
                        break;
                    }
                    case AmazonMSK: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.USER_PASSWD_WITH_TLS)
                                .needUserName(true)
                                .needPassword(true)
                                .needTlsTrustStoreFilePassword(true)
                                .needTlsTrustStoreFile(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder()
                                .securityType(SecurityType.KERBEROS)
                                .needUserName(true)
                                .isUserNamePrinciple(true)
                                .needPassword(false)
                                .needKeyTabFile(true)
                                .needKrb5File(true)
                                .build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD_WITH_SCRAM).needUserName(true).needPassword(true).build());
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).defaultCheck(true).build());
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("unsupported datasource type:" + data.getDataSourceType());
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.MICROSOFT_AZURE_CLOUD_HOSTED) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                switch (data.getDataSourceType()) {
                    case PostgreSQL:
                    case MariaDB:
                    case MySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    case SQLServer: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("unsupported datasource type:" + data.getDataSourceType());
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.HUAWEI_CLOUD_HOSTED) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                if (data.getDataSourceType() == GaussDBForMySQL) {
                    detail.getSecurityOptions()
                        .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                    detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                    detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                } else {
                    throw new IllegalArgumentException("unsupported datasource type:" + data.getDataSourceType());
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.TENCENT_CLOUD_HOSTED) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                switch (data.getDataSourceType()) {
                    case TdsqlCMySQL:
                    case TdsqlMySQL: {
                        detail.getSecurityOptions()
                            .add(DsSecurityOption.builder().securityType(SecurityType.USER_PASSWD).needUserName(true).needPassword(true).defaultCheck(true).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.ONLY_USER).needUserName(true).needPassword(false).build());
                        detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).needUserName(false).needPassword(false).build());
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported datasource type:" + data.getDataSourceType());
                    }
                }
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else if (data.getDeployEnvType() == DeployEnvType.INDEPENDENT_CLOUD_PLATFORM) {
            if (data.getDeployFetchType() == DeployEnvInfoFetchType.MANUALLY_FILL) {
                String defaultHost = null;
                switch (data.getDataSourceType()) {
                    case HuggingFace: {
                        defaultHost = "api-inference.huggingface.co/";
                        break;
                    }
                    case Cohere: {
                        defaultHost = "api.cohere.ai/v1/";
                        break;
                    }
                    case OpenAI: {
                        defaultHost = "api.openai.com/v1/";
                        break;
                    }
                    case DeepSeek: {
                        defaultHost = "api.deepseek.com/v1/";
                        break;
                    }
                    case ZhipuAI: {
                        defaultHost = "open.bigmodel.cn/api/paas/v4";
                        break;
                    }
                    case Anthropic: {
                        defaultHost = "api.anthropic.com/v1/";
                        break;
                    }
                }
                detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.API_KEY).defaultHost(defaultHost).needApiKey(true).defaultCheck(true).build());
                detail.getSecurityOptions().add(DsSecurityOption.builder().securityType(SecurityType.NONE).build());
            } else {
                throw new IllegalArgumentException("Unsupported datasource fetch type:" + data.getDeployFetchType());
            }
        } else {
            throw new IllegalArgumentException("unsupported deploy env type:" + data.getDeployEnvType());
        }

        detail.getSecurityOptions().forEach(option -> {
            if (option.getSecurityType() != null && option.getSecurityTypeI18nName() == null) {
                option.setSecurityTypeI18nName(RdpI18nUtils.getMessage(option.getSecurityType().getI18nKey()));
            }
        });

        return ResWebDataUtils.buildSuccess(detail);
    }
}

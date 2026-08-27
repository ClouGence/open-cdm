import i18n from '../i18n';

export default {
  region: {
    customer: i18n.global.t('bu-xian')
  },
  resourceType: {
    WORKER: i18n.global.t('tong-bu-ji-qi'),
    DATASOURCE: i18n.global.t('shu-ju-yuan'),
    DATA_JOB: i18n.global.t('tong-bu-ren-wu')
  },
  deployDsMap: {
    SELF_MAINTENANCE: {
      PolarDbX: 'PolarDB-X',
      GaussDBForOpenGauss: 'OpenGauss'
    },
    ALIBABA_CLOUD_HOSTED: {
      MySQL: 'RDS for MySQL',
      PostgreSQL: 'RDS for PostgreSQL',
      Greenplum: 'ADB for PG',
      ElasticSearch: 'ElasticSearch',
      RocketMQ: 'RocketMQ',
      Kafka: 'Kafka',
      RabbitMQ: 'RabbitMQ',
      DRDS: 'DRDS',
      PolarDbX: 'PolarDB-X',
      AdbForMySQL: 'ADB for MySQL',
      PolarDbMySQL: 'PolarDbMySQL',
      ClickHouse: 'ClickHouse',
      MongoDB: 'MongoDB',
      Redis: 'Redis',
      Valkey: 'Redis',
      SQLServer: 'SQLServer'
    },
    AWS_CLOUD_HOSTED: {
      AuroraMySQL: 'Aurora (MySQL)',
      AuroraPostgreSQL: 'Aurora (PostgreSQL)',
      MySQL: 'MySQL',
      PostgreSQL: 'PostgreSQL',
      MariaDB: 'MariaDB',
      Oracle: 'Oracle',
      SQLServer: 'Microsoft SQLServer',
      ElastiCache: 'ElastiCache(redis)'
    },
    MICROSOFT_AZURE_CLOUD_HOSTED: {
      MySQL: 'Azure for MySQL',
      PostgreSQL: 'Azure for PostgreSQL',
      MariaDB: 'Azure for MariaDB',
      SQLServer: 'Azure SQL'
    }
  }
};

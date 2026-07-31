package com.clougence.clouddm.ds.behavior;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;

@Execution(ExecutionMode.CONCURRENT)
abstract class BasicSingleDataSourceBehaviorTextTest {

    private final String            datasource;
    private final TextResourceShard fixtureShard;

    protected BasicSingleDataSourceBehaviorTextTest(String resourceDirectory, int shardCount, int shardId){
        String prefix = "behavior/";
        if (!resourceDirectory.startsWith(prefix)) {
            throw new IllegalArgumentException("Behavior resource parent must start with " + prefix + ": " + resourceDirectory);
        }
        String relative = resourceDirectory.substring(prefix.length());
        int separator = relative.indexOf('/');
        this.datasource = separator < 0 ? relative : relative.substring(0, separator);
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @TestFactory
    Stream<DynamicTest> behaviorScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            BehaviorAnalysisSpi analysisSpi = SqlTestSupport.sqlEngine(datasource).behaviorAnalysisSpi(SqlParserParameters.empty());
            if (analysisSpi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for " + datasource);
            }
            return analysisSpi;
        });
        return fixtureShard.resourceFiles().stream()
            .flatMap(resourcePath -> BehaviorTextTest.loadCases(resourcePath).stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(),
                        () -> BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi.get(), null))));
    }
}

abstract class BasicAdbBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicAdbBehaviorTextTest(int count, int id){ super("behavior/adb", count, id); } }
final class AdbBehaviorShard1Test extends BasicAdbBehaviorTextTest { AdbBehaviorShard1Test(){ super(3, 0); } }
final class AdbBehaviorShard2Test extends BasicAdbBehaviorTextTest { AdbBehaviorShard2Test(){ super(3, 1); } }
final class AdbBehaviorShard3Test extends BasicAdbBehaviorTextTest { AdbBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicClickHouseBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicClickHouseBehaviorTextTest(int count, int id){ super("behavior/clickhouse", count, id); } }
final class ClickHouseBehaviorShard1Test extends BasicClickHouseBehaviorTextTest { ClickHouseBehaviorShard1Test(){ super(3, 0); } }
final class ClickHouseBehaviorShard2Test extends BasicClickHouseBehaviorTextTest { ClickHouseBehaviorShard2Test(){ super(3, 1); } }
final class ClickHouseBehaviorShard3Test extends BasicClickHouseBehaviorTextTest { ClickHouseBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicDb2BehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicDb2BehaviorTextTest(int count, int id){ super("behavior/db2", count, id); } }
final class Db2BehaviorShard1Test extends BasicDb2BehaviorTextTest { Db2BehaviorShard1Test(){ super(3, 0); } }
final class Db2BehaviorShard2Test extends BasicDb2BehaviorTextTest { Db2BehaviorShard2Test(){ super(3, 1); } }
final class Db2BehaviorShard3Test extends BasicDb2BehaviorTextTest { Db2BehaviorShard3Test(){ super(3, 2); } }

abstract class BasicDorisBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicDorisBehaviorTextTest(int count, int id){ super("behavior/doris", count, id); } }
final class DorisBehaviorShard1Test extends BasicDorisBehaviorTextTest { DorisBehaviorShard1Test(){ super(3, 0); } }
final class DorisBehaviorShard2Test extends BasicDorisBehaviorTextTest { DorisBehaviorShard2Test(){ super(3, 1); } }
final class DorisBehaviorShard3Test extends BasicDorisBehaviorTextTest { DorisBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicGaussBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicGaussBehaviorTextTest(int count, int id){ super("behavior/gauss", count, id); } }
final class GaussBehaviorShard1Test extends BasicGaussBehaviorTextTest { GaussBehaviorShard1Test(){ super(3, 0); } }
final class GaussBehaviorShard2Test extends BasicGaussBehaviorTextTest { GaussBehaviorShard2Test(){ super(3, 1); } }
final class GaussBehaviorShard3Test extends BasicGaussBehaviorTextTest { GaussBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicMaxComputeBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicMaxComputeBehaviorTextTest(int count, int id){ super("behavior/maxcompute", count, id); } }
final class MaxComputeBehaviorShard1Test extends BasicMaxComputeBehaviorTextTest { MaxComputeBehaviorShard1Test(){ super(3, 0); } }
final class MaxComputeBehaviorShard2Test extends BasicMaxComputeBehaviorTextTest { MaxComputeBehaviorShard2Test(){ super(3, 1); } }
final class MaxComputeBehaviorShard3Test extends BasicMaxComputeBehaviorTextTest { MaxComputeBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicMongoDbBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicMongoDbBehaviorTextTest(int count, int id){ super("behavior/mongodb", count, id); } }
final class MongoDbBehaviorShard1Test extends BasicMongoDbBehaviorTextTest { MongoDbBehaviorShard1Test(){ super(3, 0); } }
final class MongoDbBehaviorShard2Test extends BasicMongoDbBehaviorTextTest { MongoDbBehaviorShard2Test(){ super(3, 1); } }
final class MongoDbBehaviorShard3Test extends BasicMongoDbBehaviorTextTest { MongoDbBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicObMyBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicObMyBehaviorTextTest(int count, int id){ super("behavior/ob4my", count, id); } }
final class ObMyBehaviorShard1Test extends BasicObMyBehaviorTextTest { ObMyBehaviorShard1Test(){ super(3, 0); } }
final class ObMyBehaviorShard2Test extends BasicObMyBehaviorTextTest { ObMyBehaviorShard2Test(){ super(3, 1); } }
final class ObMyBehaviorShard3Test extends BasicObMyBehaviorTextTest { ObMyBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicObOracleBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicObOracleBehaviorTextTest(int count, int id){ super("behavior/ob4ora", count, id); } }
final class ObOracleBehaviorShard1Test extends BasicObOracleBehaviorTextTest { ObOracleBehaviorShard1Test(){ super(3, 0); } }
final class ObOracleBehaviorShard2Test extends BasicObOracleBehaviorTextTest { ObOracleBehaviorShard2Test(){ super(3, 1); } }
final class ObOracleBehaviorShard3Test extends BasicObOracleBehaviorTextTest { ObOracleBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicOracleBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicOracleBehaviorTextTest(int count, int id){ super("behavior/oracle", count, id); } }
final class OracleBehaviorShard1Test extends BasicOracleBehaviorTextTest { OracleBehaviorShard1Test(){ super(3, 0); } }
final class OracleBehaviorShard2Test extends BasicOracleBehaviorTextTest { OracleBehaviorShard2Test(){ super(3, 1); } }
final class OracleBehaviorShard3Test extends BasicOracleBehaviorTextTest { OracleBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicPolarDbXBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicPolarDbXBehaviorTextTest(int count, int id){ super("behavior/por4x", count, id); } }
final class PolarDbXBehaviorShard1Test extends BasicPolarDbXBehaviorTextTest { PolarDbXBehaviorShard1Test(){ super(3, 0); } }
final class PolarDbXBehaviorShard2Test extends BasicPolarDbXBehaviorTextTest { PolarDbXBehaviorShard2Test(){ super(3, 1); } }
final class PolarDbXBehaviorShard3Test extends BasicPolarDbXBehaviorTextTest { PolarDbXBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicPostgresBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicPostgresBehaviorTextTest(int count, int id){ super("behavior/postgres", count, id); } }
final class PostgresBehaviorShard1Test extends BasicPostgresBehaviorTextTest { PostgresBehaviorShard1Test(){ super(3, 0); } }
final class PostgresBehaviorShard2Test extends BasicPostgresBehaviorTextTest { PostgresBehaviorShard2Test(){ super(3, 1); } }
final class PostgresBehaviorShard3Test extends BasicPostgresBehaviorTextTest { PostgresBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicSql2003BehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicSql2003BehaviorTextTest(int count, int id){ super("behavior/sql2003", count, id); } }
final class Sql2003BehaviorShard1Test extends BasicSql2003BehaviorTextTest { Sql2003BehaviorShard1Test(){ super(3, 0); } }
final class Sql2003BehaviorShard2Test extends BasicSql2003BehaviorTextTest { Sql2003BehaviorShard2Test(){ super(3, 1); } }
final class Sql2003BehaviorShard3Test extends BasicSql2003BehaviorTextTest { Sql2003BehaviorShard3Test(){ super(3, 2); } }

abstract class BasicSql92BehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicSql92BehaviorTextTest(int count, int id){ super("behavior/sql92", count, id); } }
final class Sql92BehaviorShard1Test extends BasicSql92BehaviorTextTest { Sql92BehaviorShard1Test(){ super(3, 0); } }
final class Sql92BehaviorShard2Test extends BasicSql92BehaviorTextTest { Sql92BehaviorShard2Test(){ super(3, 1); } }
final class Sql92BehaviorShard3Test extends BasicSql92BehaviorTextTest { Sql92BehaviorShard3Test(){ super(3, 2); } }

abstract class BasicSql99BehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicSql99BehaviorTextTest(int count, int id){ super("behavior/sql99", count, id); } }
final class Sql99BehaviorShard1Test extends BasicSql99BehaviorTextTest { Sql99BehaviorShard1Test(){ super(3, 0); } }
final class Sql99BehaviorShard2Test extends BasicSql99BehaviorTextTest { Sql99BehaviorShard2Test(){ super(3, 1); } }
final class Sql99BehaviorShard3Test extends BasicSql99BehaviorTextTest { Sql99BehaviorShard3Test(){ super(3, 2); } }

abstract class BasicSqlServerBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicSqlServerBehaviorTextTest(int count, int id){ super("behavior/sqlserver", count, id); } }
final class SqlServerBehaviorShard1Test extends BasicSqlServerBehaviorTextTest { SqlServerBehaviorShard1Test(){ super(3, 0); } }
final class SqlServerBehaviorShard2Test extends BasicSqlServerBehaviorTextTest { SqlServerBehaviorShard2Test(){ super(3, 1); } }
final class SqlServerBehaviorShard3Test extends BasicSqlServerBehaviorTextTest { SqlServerBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicStarRocksBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicStarRocksBehaviorTextTest(int count, int id){ super("behavior/starrocks", count, id); } }
final class StarRocksBehaviorShard1Test extends BasicStarRocksBehaviorTextTest { StarRocksBehaviorShard1Test(){ super(3, 0); } }
final class StarRocksBehaviorShard2Test extends BasicStarRocksBehaviorTextTest { StarRocksBehaviorShard2Test(){ super(3, 1); } }
final class StarRocksBehaviorShard3Test extends BasicStarRocksBehaviorTextTest { StarRocksBehaviorShard3Test(){ super(3, 2); } }

abstract class BasicTiDbBehaviorTextTest extends BasicSingleDataSourceBehaviorTextTest { BasicTiDbBehaviorTextTest(int count, int id){ super("behavior/tidb", count, id); } }
final class TiDbBehaviorShard1Test extends BasicTiDbBehaviorTextTest { TiDbBehaviorShard1Test(){ super(3, 0); } }
final class TiDbBehaviorShard2Test extends BasicTiDbBehaviorTextTest { TiDbBehaviorShard2Test(){ super(3, 1); } }
final class TiDbBehaviorShard3Test extends BasicTiDbBehaviorTextTest { TiDbBehaviorShard3Test(){ super(3, 2); } }

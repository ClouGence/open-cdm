package com.clougence.clouddm.ds.split.doris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableLexer;
import com.clougence.clouddm.ds.doris.sql.parser.antlr.DrCreateTableParser;
import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicDorisSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicDorisSplitTextTest(int shardCount, int shardId){
        super("split/doris", shardCount, shardId);
    }
}

final class DorisSplitShard1Test extends BasicDorisSplitTextTest { DorisSplitShard1Test(){ super(3, 0); } }
final class DorisSplitShard2Test extends BasicDorisSplitTextTest { DorisSplitShard2Test(){ super(3, 1); } }
final class DorisSplitShard3Test extends BasicDorisSplitTextTest { DorisSplitShard3Test(){ super(3, 2); } }

final class DorisCreateTableGrammarTest {
    private static final String LONG_DELIMITER =
            "------------------------------------------------------------------------------------------";

    @Test
    void createTableWithAutoBucketsAndPropertiesMatchesCreateTableGrammar() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream(
                "/split/doris/ddl_table_auto_buckets_properties_0.txt")) {
            assertNotNull(stream);
            String fixture = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            String sql = fixture.substring(0, fixture.indexOf(LONG_DELIMITER));
            DrCreateTableLexer lexer = new DrCreateTableLexer(CharStreams.fromString(sql.toUpperCase(Locale.ROOT)));
            DrCreateTableParser parser = new DrCreateTableParser(new CommonTokenStream(lexer));

            DrCreateTableParser.SingleStatementContext statement = parser.singleStatement();
            long propertiesClauses = statement.createTable().tableProperty().stream()
                    .filter(property -> property.PROPERTIES() != null)
                    .count();

            assertEquals(0, parser.getNumberOfSyntaxErrors());
            assertEquals(1, propertiesClauses);
        }
    }
}

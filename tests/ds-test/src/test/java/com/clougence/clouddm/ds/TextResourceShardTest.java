package com.clougence.clouddm.ds;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextResourceShardTest {

    @Test
    void threeShardsAreDisjointAndCoverEveryFixture() {
        assertThreeShardCoverage("split");
        assertThreeShardCoverage("lineage");
        assertThreeShardCoverage("behavior");
        assertThreeShardCoverage("permission/mysql/5.6");
        assertThreeShardCoverage("permission/mysql/5.7");
        assertThreeShardCoverage("permission/mysql/8.0");
        assertThreeShardCoverage("permission/mysql/8.4");
        assertThreeShardCoverage("permission/mysql/9.7");
    }

    @Test
    void rejectsInvalidShardCoordinates() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TextResourceShard("split", 0, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TextResourceShard("split", 3, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TextResourceShard("split", 3, 3));
    }

    private static void assertThreeShardCoverage(String resourceDirectory) {
        List<String> all = TextCaseSupport.resourceFiles(resourceDirectory);
        Set<String> union = new HashSet<>();
        for (int shardId = 0; shardId < 3; shardId++) {
            List<String> shard = new TextResourceShard(resourceDirectory, 3, shardId).resourceFiles();
            Assertions.assertEquals(shard.size(), new HashSet<>(shard).size(), resourceDirectory + " shard " + shardId + " contains duplicates");
            for (String resource : shard) {
                Assertions.assertTrue(union.add(resource), resourceDirectory + " shards overlap at " + resource);
            }
        }
        Assertions.assertEquals(new HashSet<>(all), union, resourceDirectory + " shards do not cover the complete fixture set");
    }
}

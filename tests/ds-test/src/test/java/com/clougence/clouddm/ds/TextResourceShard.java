package com.clougence.clouddm.ds;

import java.util.List;
import java.util.function.Predicate;

/**
 * Selects fixture files from one resource parent using a stable relative-path hash.
 */
public final class TextResourceShard {

    private final String resourceDirectory;
    private final int    shardCount;
    private final int    shardId;

    public TextResourceShard(String resourceDirectory, int shardCount, int shardId){
        if (shardCount < 1 || shardId < 0 || shardId >= shardCount) {
            throw new IllegalArgumentException("Invalid shard " + shardId + "/" + shardCount);
        }
        this.resourceDirectory = resourceDirectory;
        this.shardCount = shardCount;
        this.shardId = shardId;
    }

    public List<String> resourceFiles() {
        return resourceFiles(path -> true);
    }

    public List<String> resourceFiles(Predicate<String> filter) {
        return resourceFiles(".txt", filter);
    }

    public List<String> resourceFiles(String suffix, Predicate<String> filter) {
        return TextCaseSupport.resourceFiles(resourceDirectory, suffix, filter.and(this::belongsToShard));
    }

    private boolean belongsToShard(String resourcePath) {
        String prefix = resourceDirectory + "/";
        if (!resourcePath.startsWith(prefix)) {
            throw new IllegalArgumentException("Resource is outside shard parent " + resourceDirectory + ": " + resourcePath);
        }
        String relativePath = resourcePath.substring(prefix.length());
        return Math.floorMod(relativePath.hashCode(), shardCount) == shardId;
    }
}

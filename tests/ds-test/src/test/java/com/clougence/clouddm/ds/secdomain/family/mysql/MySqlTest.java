package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.clougence.clouddm.ds.TestUtil;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MySqlTest extends MySecDomainTestSupport {

    private final String dir = "src/test/resources/sql-test/mysql";

    public MySqlTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void test1() throws IOException {
        File directory = new File(Paths.get(dir).toUri());
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".sql")) {
                continue;
            }
            String sql = getSql(file.getAbsolutePath());
            if (StringUtils.isEmpty(sql)) {
                continue;
            }
            try {
                analysisSpi.analysisResource(this.dataSourceType, codeInfo(sql), contextInfo(), ctx);
                resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql), contextInfo());
                for (SplitScript splitScript : splitAnalysisSpi.splitScript(sql, null, 0, 0)) {
                    boolean b = splitScript.getType() != SecQueryType.UNKNOWN;
                    if (!b) {
                        System.out.println(splitScript.getScript());
                    }
                    assert b;
                }
            } catch (Exception e) {
                System.out.println("failed to parse file:" + file.getAbsolutePath());
                throw e;
            }
        }
    }

    @Test
    public void test2() throws IOException {
        File directory = new File(Paths.get(dir).toUri());
        traverse(directory);
    }

    private void traverse(File directory) throws IOException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                traverse(file);
            } else {
                doTest(file);
            }
        }
    }

    private void doTest(File sqlFile) throws IOException {
        if (!sqlFile.getName().endsWith(".txt")) {
            return;
        }
        String text = getSql(sqlFile.getAbsolutePath());

        String[] blocks = text.split(TestUtil.LONG_SPLIT);
        for (String block : blocks) {
            try {
                String[] split = block.split(TestUtil.SHORT_SPLIT);
                String sql = split[0];
                String res = split[1].trim();
                String domains = split[2].trim();
                String sqlType = split[3].trim();
                {
                    Map<RuleDomain, List<ResObject>> ruleDomainListMap = analysisSpi.analysisResource(this.dataSourceType, codeInfo(sql), contextInfo(), ctx);
                    Set<String> set = new HashSet<>();
                    for (RuleDomain ruleDomain : ruleDomainListMap.keySet()) {
                        String authLabel = ruleDomain.getSqlType().getAuthKind().getAuthLabel();
                        List<ResObject> resObjects = ruleDomainListMap.get(ruleDomain);
                        Set<String> collect1 = resObjects.stream().map((obj) -> {
                            return authLabel + ":" + obj.toDsResPath().getResPath();
                        }).collect(Collectors.toSet());
                        set.addAll(collect1);
                    }
                    boolean equals = TestUtil.equalsSet(set, TestUtil.readToSet(res));

                    if (!equals) {
                        System.out.println(sql);
                        throw new RuntimeException("failed to parse file:" + sqlFile.getAbsolutePath());
                    }

                }
                {
                    List<RuleDomain> ruleDomains = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql), contextInfo());
                    Set<String> collect = ruleDomains.stream().map((domain) -> {
                        try {
                            return TestUtil.toJson(domain);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toSet());
                    boolean equals = TestUtil.equalsSet(collect, TestUtil.readToSet(domains));
                    if (!equals) {
                        collect.removeAll(TestUtil.readToSet(domains));
                        Set<String> strings = TestUtil.readToSet(domains);
                        System.out.println(sql);
                        throw new RuntimeException("failed to parse file:" + sqlFile.getAbsolutePath());
                    }
                }
                {
                    List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(sql, null, 0, 0);
                    assert splitScripts.size() == 1;
                    assert sqlType.equals(splitScripts.get(0).getType().name());
                }
            } catch (Exception e) {
                System.out.println("failed to parse file:" + sqlFile.getAbsolutePath());
                throw e;
            }
        }
    }

//    @Test
    public void generateFile() throws IOException {
        File file = new File("src/test/resources/sql-test/mysql/2025_12_10_ai.sql");
        createFile2(file);
    }

    private void createFile2(File sqlFile) throws IOException {
        String text = getSql(sqlFile.getAbsolutePath());

        String first = sqlFile.getAbsolutePath().replace(".sql", ".txt1");
        File file = new File(first);
        file.delete();
        file.createNewFile();
        Path path = Paths.get(first);

        List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(text, null, 0, 0);
        for (int i = 0; i < splitScripts.size(); i++) {
            SplitScript splitScript = splitScripts.get(i);
            Files.write(path, splitScript.getScript().getBytes(), StandardOpenOption.APPEND);
            Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            // res
            {
                Files.write(path, TestUtil.SHORT_SPLIT.getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
                Map<RuleDomain, List<ResObject>> ruleDomainListMap = analysisSpi.analysisResource(this.dataSourceType, codeInfo(splitScript.getScript()), contextInfo(), ctx);
                Set<String> set = new HashSet<>();
                for (RuleDomain ruleDomain : ruleDomainListMap.keySet()) {
                    String authLabel = ruleDomain.getSqlType().getAuthKind().getAuthLabel();
                    List<ResObject> resObjects = ruleDomainListMap.get(ruleDomain);
                    Set<String> collect1 = resObjects.stream().map((obj) -> {
                        return authLabel + ":" + obj.toDsResPath().getResPath();
                    }).collect(Collectors.toSet());
                    set.addAll(collect1);
                }
                Files.write(path, TestUtil.toJson(set).getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            }
            // domain
            {
                Files.write(path, TestUtil.SHORT_SPLIT.getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
                List<RuleDomain> ruleDomains = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(splitScript.getScript()), contextInfo());
                Set<String> collect = ruleDomains.stream().map((domain) -> {
                    try {
                        return TestUtil.toJson(domain);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet());

                Files.write(path, TestUtil.toJson(collect).getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            }
            //split
            {
                Files.write(path, TestUtil.SHORT_SPLIT.getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(path, splitScript.getType().name().getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            }

            if (i != splitScripts.size() - 1) {
                Files.write(path, TestUtil.LONG_SPLIT.getBytes(), StandardOpenOption.APPEND);
                Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            }
        }

    }

    private String getSql(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
}

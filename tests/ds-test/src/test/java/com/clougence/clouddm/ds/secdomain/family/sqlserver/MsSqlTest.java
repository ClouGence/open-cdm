package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public class MsSqlTest extends MsSecDomainTestSupport {

    private final String dir = "src/test/resources/sql-test/sqlserver";

    public MsSqlTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void test1() throws IOException {
        File directory = new File(Paths.get(dir).toUri());
        for (File file : directory.listFiles()) {
            String sql = getSql(file.getAbsolutePath());
            if (StringUtils.isEmpty(sql)) {
                continue;
            }
            try {
                analysisSpi.analysisResource(this.dataSourceType, codeInfo(sql), contextInfo(), ctx);
                resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql), contextInfo());
                splitAnalysisSpi.splitScript(sql, null, 0, 0);
            } catch (Exception e) {
                System.out.println("failed to parse file:" + file.getAbsolutePath());
                throw e;
            }
        }
    }

    private String getSql(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
}

package com.clougence.clouddm.ds.secdomain.special.adsmy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMyResAnalysisSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySecDomainResolveSpi;
import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public class AdsMySqlTest extends MySecDomainTestSupport {

    private final String dir = "src/test/resources/sql-test/adsmy";

    public AdsMySqlTest(){
        this.analysisSpi = new AdsMyResAnalysisSpi(null);
        this.resolveSpi = new AdsMySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new AdsMySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.AdbForMySQL;
    }

    @Test
    public void test1() throws IOException {
        File directory = new File(Paths.get(dir).toUri());
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
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

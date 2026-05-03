package com.clougence.clouddm.sec.rules.execute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.secrules.CheckerData;
import com.clougence.clouddm.sdk.service.secrules.CheckerRange;
import com.clougence.clouddm.sdk.service.secrules.MatchMode;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public class Utils {

    private static final Map<TargetType, Integer> scoreMap = new HashMap<>();

    static {
        // 1. the smaller the score, the greater the weight
        // 2. ddl has a higher weight than dml
        scoreMap.put(TargetType.Unknown, 0);
        scoreMap.put(TargetType.Environment, 1);
        scoreMap.put(TargetType.Machine, 2);
        scoreMap.put(TargetType.Instance, 3);
        scoreMap.put(TargetType.Catalog, 4);
        scoreMap.put(TargetType.Schema, 5);
        scoreMap.put(TargetType.Table, 6);
        scoreMap.put(TargetType.View, 6);
        scoreMap.put(TargetType.Materialized, 6);
        scoreMap.put(TargetType.Sequence, 6);
        scoreMap.put(TargetType.Function, 6);
        scoreMap.put(TargetType.Procedure, 6);
        scoreMap.put(TargetType.Trigger, 6);
        scoreMap.put(TargetType.Event, 6);
        scoreMap.put(TargetType.Synonym, 6);
        scoreMap.put(TargetType.Column, 7);
        scoreMap.put(TargetType.Index, 7);
        scoreMap.put(TargetType.Constraint, 7);

        //
        scoreMap.put(TargetType.Insert, 10);
        scoreMap.put(TargetType.Update, 10);
        scoreMap.put(TargetType.Delete, 10);
        scoreMap.put(TargetType.Call, 10);
        scoreMap.put(TargetType.Query, 10);
        scoreMap.put(TargetType.Key, 10);
    }

    // scope include target
    private static boolean scopeInclude(TargetType scope, TargetType target) {
        return scoreMap.getOrDefault(scope, 0) <= scoreMap.getOrDefault(target, 0);
    }

    public static boolean checkRangeIncludeDomain(CheckerRange range, CheckerData domain) {
        if (!scopeInclude(range.getScope(), domain.getDomain().getSqlTarget())) {
            return false;
        }

        //        Map<TargetType, String> resource = convertToResource(domain);
        List<Map<TargetType, String>> maps = convertToResourceToList(domain);
        for (Map<TargetType, String> resource : maps) {
            String levelPrefix = range.getLevelPrefix();

            StringBuilder targetPath = new StringBuilder();
            List<SecRangeCfg> rangeCfgList = new ArrayList<>();
            switch (range.getScope()) {
                case Environment: {
                    switch (range.getMatchMode()) {
                        case SUFFIX:
                        case PREFIX:
                        case INCLUDE:
                            targetPath.append(fmtNode(domain.getDomain().getEnvName()));
                            break;
                        case EXACT:
                            targetPath.append(fmtNode(String.valueOf(domain.getDomain().getEnvId())));
                            break;
                    }
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg("", node));
                    }
                    break;
                }
                case Instance: {
                    targetPath.append(fmtNode(resource.get(TargetType.Environment)));
                    switch (range.getMatchMode()) {
                        case SUFFIX:
                        case PREFIX:
                        case INCLUDE:
                            targetPath.append(fmtNode(domain.getDomain().getDsName()));
                            break;
                        case EXACT:
                            targetPath.append(fmtNode(resource.get(TargetType.Instance)));
                            break;
                    }
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg(levelPrefix, node));
                    }
                    break;
                }
                case Catalog: {
                    targetPath.append(fmtNode(resource.get(TargetType.Environment)));
                    targetPath.append(fmtNode(resource.get(TargetType.Instance)));
                    targetPath.append(fmtNode(resource.get(TargetType.Catalog)));
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg(levelPrefix, node));
                    }
                    break;
                }
                case Schema: {
                    targetPath.append(fmtNode(resource.get(TargetType.Environment)));
                    targetPath.append(fmtNode(resource.get(TargetType.Instance)));
                    if (domain.getDsLevelsDef().contains(UmiTypes.Catalog)) {
                        targetPath.append(fmtNode(resource.get(TargetType.Catalog)));
                    }
                    targetPath.append(fmtNode(resource.get(TargetType.Schema)));
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg(levelPrefix, node));
                    }
                    break;
                }
                case Materialized:
                case Table:
                case View: {
                    targetPath.append(fmtNode(resource.get(TargetType.Environment)));
                    targetPath.append(fmtNode(resource.get(TargetType.Instance)));
                    if (domain.getDsLevelsDef().contains(UmiTypes.Catalog)) {
                        targetPath.append(fmtNode(resource.get(TargetType.Catalog)));
                    }
                    targetPath.append(fmtNode(resource.get(TargetType.Schema)));
                    targetPath.append(fmtNode(resource.get(TargetType.Table)));
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg(levelPrefix, node));
                    }
                    break;
                }
                case Column: {
                    targetPath.append(fmtNode(resource.get(TargetType.Environment)));
                    targetPath.append(fmtNode(resource.get(TargetType.Instance)));
                    if (domain.getDsLevelsDef().contains(UmiTypes.Catalog)) {
                        targetPath.append(fmtNode(resource.get(TargetType.Catalog)));
                    }
                    targetPath.append(fmtNode(resource.get(TargetType.Schema)));
                    targetPath.append(fmtNode(resource.get(TargetType.Table)));
                    targetPath.append(fmtNode(resource.get(TargetType.Column)));
                    for (String node : range.getLevelNodes()) {
                        rangeCfgList.add(new SecRangeCfg(levelPrefix, node));
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException("range '" + range.getScope() + "' Unsupported.");
            }

            if (testRangeIncludeDomain(levelPrefix, rangeCfgList, targetPath.toString(), range.getMatchMode(), range.isChooseAll())) {
                return true;
            }
        }
        return false;
    }

    private static boolean testRangeIncludeDomain(String scopePrefix, List<SecRangeCfg> rangeCfgList, String targetPath, MatchMode matchMode, boolean chooseAll) {
        if (chooseAll) {
            return StringUtils.startsWith(targetPath, scopePrefix);
        } else {
            for (SecRangeCfg rangeCfg : rangeCfgList) {
                switch (matchMode) {
                    case EXACT: {
                        String cfgStr = rangeCfg.getLevelPrefix() + "/" + rangeCfg.getNode();
                        if (StringUtils.equals(targetPath, cfgStr)) {
                            return true;
                        }
                        break;
                    }
                    case PREFIX: {
                        String cfgStr = rangeCfg.getLevelPrefix() + "/" + rangeCfg.getNode();
                        if (StringUtils.startsWith(targetPath, cfgStr)) {
                            return true;
                        }
                        break;
                    }
                    case SUFFIX: {
                        if (StringUtils.startsWith(targetPath, rangeCfg.levelPrefix)) {
                            String testPath = targetPath.substring(rangeCfg.levelPrefix.length());
                            if (StringUtils.endsWith(testPath, rangeCfg.node)) {
                                return true;
                            }
                        }
                        break;
                    }
                    case INCLUDE: {
                        if (StringUtils.startsWith(targetPath, rangeCfg.levelPrefix)) {
                            String testPath = targetPath.substring(rangeCfg.levelPrefix.length());
                            if (StringUtils.contains(testPath, rangeCfg.node)) {
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            return false;
        }
    }

    //    private static Map<TargetType, String> convertToResource(CheckerData checker) {
    //        RuleDomain targetDomain = checker.getDomain();
    //        Map<TargetType, String> resource = new HashMap<>();
    //        resource.put(TargetType.Environment, String.valueOf(targetDomain.getEnvId()));
    //        resource.put(TargetType.Instance, String.valueOf(targetDomain.getDsId()));
    //        resource.put(TargetType.Catalog, getCatalog(checker, targetDomain.resolveName(TargetType.Catalog)));
    //        resource.put(TargetType.Schema, getSchema(checker, targetDomain.resolveName(TargetType.Schema)));
    //        resource.put(TargetType.Table, targetDomain.resolveName(TargetType.Table));
    //        resource.put(TargetType.Column, targetDomain.resolveName(TargetType.Column));
    //        //resource.put(TargetType.Index, targetDomain.resolveName(TargetType.Index));
    //        //resource.put(TargetType.Constraint, targetDomain.resolveName(TargetType.Constraint));
    //        return resource;
    //    }

    private static List<Map<TargetType, String>> convertToResourceToList(CheckerData checker) {
        RuleDomain targetDomain = checker.getDomain();
        List<Map<TargetType, String>> list = checker.getDomain().resolveResource();
        for (Map<TargetType, String> map : list) {
            map.putIfAbsent(TargetType.Environment, String.valueOf(targetDomain.getEnvId()));
            map.putIfAbsent(TargetType.Instance, String.valueOf(targetDomain.getDsId()));
            map.putIfAbsent(TargetType.Catalog, checker.getCurrentCatalog());
            map.putIfAbsent(TargetType.Schema, checker.getCurrentSchema());
        }

        return list;
    }

    private static String fmtNode(String str) {
        return "/" + (str == null ? "" : str);
    }

    private static String getCatalog(CheckerData checkerData, String catalogFromDomain) {
        if (StringUtils.isNotBlank(catalogFromDomain)) {
            return catalogFromDomain;
        } else {
            return checkerData.getCurrentCatalog();
        }
    }

    private static String getSchema(CheckerData checkerData, String schemaFromDomain) {
        if (StringUtils.isNotBlank(schemaFromDomain)) {
            return schemaFromDomain;
        } else {
            return checkerData.getCurrentSchema();
        }
    }

    @Getter
    private static class SecRangeCfg {

        private final String levelPrefix;
        private final String node;

        public SecRangeCfg(String levelPrefix, String node){
            this.levelPrefix = levelPrefix;
            this.node = node;
        }
    }
}

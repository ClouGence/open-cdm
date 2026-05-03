package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class SelectTableDomainBuilder extends AbstractDomainBuilder {

    protected List<String>                nameList       = new ArrayList<>();
    protected List<Domain>                ruleDomainList = new ArrayList<>();
    protected String                      alias;
    private Stack<List<WithSelectDomain>> selectStack;

    public SelectTableDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        this.selectStack = selectStack;
    }

    protected WithSelectDomain findDomainBy(String table) {
        List<WithSelectDomain> filtered = selectStack.stream()
            .flatMap((Function<List<WithSelectDomain>, Stream<WithSelectDomain>>) Collection::stream)
            .filter(t -> StringUtils.equals(t.getName(), table))
            .collect(Collectors.toList());

        Collections.reverse(filtered);

        if (filtered.isEmpty()) {
            return null;
        } else if (filtered.size() == 1) {
            return filtered.get(0);
        } else {
            WithSelectDomain defaultTab = filtered.get(0);
            List<WithSelectDomain> filteredOnlyName = filtered.stream().filter(t -> {
                return !StringUtils.equals(t.getName(), table);
            }).collect(Collectors.toList());
            if (filteredOnlyName.isEmpty()) {
                return defaultTab;
            } else {
                return filteredOnlyName.get(0);
            }
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.ALIAS) {
            this.alias = value.toString();
        }
    }

    @Override
    public List<Domain> build() {
        prepareHandle();
        if (ruleDomainList.isEmpty()) {
            RdbTableDomain rdbTableDomain = getRdbTableDomain();
            if (rdbTableDomain.getSchema() == null) {
                WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                if (domainBy != null) {
                    rdbTableDomain.setVirtual(true);
                    rdbTableDomain.addChild(domainBy.getSelectDomain());
                }
            }
            return Collections.singletonList(rdbTableDomain);
        } else {
            return ruleDomainList;
        }
    }

    protected void prepareHandle() {
        if (ruleDomainList.size() == 1 && ruleDomainList.get(0) instanceof RdbTableDomain && this.alias != null) {
            ((RdbTableDomain) ruleDomainList.get(0)).setAlias(this.alias);
            return;
        }

        RdbTableDomain rdbTableDomain = getRdbTableDomain();
        if (ruleDomainList.isEmpty()) {
            if (rdbTableDomain.getSchema() == null) {
                WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                if (domainBy != null) {
                    rdbTableDomain.setVirtual(true);
                    RdbSelectDomain selectDomain = domainBy.getSelectDomain();
                    selectDomain.setMode(RdbQueryMode.SUB_FROM);
                    rdbTableDomain.addChild(selectDomain);
                }
            }
            ruleDomainList.add(rdbTableDomain);
        } else {
            List<RuleDomain> ruleDomains = new ArrayList<>();
            for (Domain ruleDomain : ruleDomainList) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    ruleDomains.add((RdbSelectDomain) ruleDomain);
                }
            }
            if (ruleDomains.isEmpty()) {
                if (rdbTableDomain.getSchema() == null) {
                    WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                    if (domainBy != null) {
                        rdbTableDomain.setVirtual(true);
                        RdbSelectDomain selectDomain = domainBy.getSelectDomain();
                        selectDomain.setMode(RdbQueryMode.SUB_FROM);
                        rdbTableDomain.addChild(selectDomain);
                    }
                }
                if (rdbTableDomain.getTable() != null) {
                    ruleDomainList.add(0, rdbTableDomain);
                }

                return;
            }
            ruleDomainList.removeAll(ruleDomains);

            rdbTableDomain.setVirtual(true);
            for (Domain domain : ruleDomains) {
                RdbSelectDomain selectDomain = (RdbSelectDomain) domain;
                selectDomain.setMode(RdbQueryMode.SUB_FROM);
                rdbTableDomain.addChild(selectDomain);
            }
            ruleDomainList.add(0, rdbTableDomain);
        }
    }

    protected RdbTableDomain getRdbTableDomain() {
        RdbTableDomain rdbTableDomain = new RdbTableDomain();
        Map<UmiTypes, String> nameMap = getNameMap(this.nameList);

        rdbTableDomain.setCatalog(nameMap.get(UmiTypes.Catalog));
        rdbTableDomain.setSchema(nameMap.get(UmiTypes.Schema));
        rdbTableDomain.setTable(nameMap.get(UmiTypes.Table));

        if (rdbTableDomain.getTable() == null) {
            rdbTableDomain.setTable(alias);
        }

        rdbTableDomain.setAlias(alias);
        return rdbTableDomain;
    }

    protected Map<UmiTypes, String> getNameMap(List<String> names) {
        return BuilderUtil.parseTableName(names);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        if (status == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
            return;
        } else if (status != DomainSource.JOIN && status != DomainSource.SELECT && status != DomainSource.TABLE) {
            super.handleSubDomain(list, status);
        }
        ruleDomainList.addAll(list);
    }
}

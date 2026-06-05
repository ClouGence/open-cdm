package com.clougence.clouddm.ds.secdomain.family.mongo;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.mongodb.analysis.MongoResAnalysisSpi;
import com.clougence.clouddm.ds.mongodb.analysis.MongoSecDomainResolveSpi;
import com.clougence.clouddm.ds.mongodb.analysis.MongoSplitAnalysisSpi;
import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCmdDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * @author: Lingma
 */
public class MongoSecDomainTest1 extends MongoSecDomainTestSupport {

    public MongoSecDomainTest1(){
        this.analysisSpi = new MongoResAnalysisSpi(new TestMetaServiceImpl());
        this.resolveSpi = new MongoSecDomainResolveSpi(new TestMetaServiceImpl());
        this.splitAnalysisSpi = new MongoSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MongoDB;
    }

    @Test
    public void testReadOperations() {
        String sql = "db.collection.find({name: 'test'}); db.collection.findOne({_id: 1}); db.collection.countDocuments({status: 'active'});";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/collection/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.collection.find({name: 'test'})");
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.findOne({_id: 1})");
            assert splitScripts.get(1).getType() == SecQueryType.READ;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.countDocuments({status: 'active'})");
            assert splitScripts.get(2).getType() == SecQueryType.READ;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getSchema() == null && domain.getFunc().equals("find");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.READ && domain.getAuditKind() == SecQueryKind.READ;
            assert domain.getSchema() == null && domain.getFunc().equals("findOne");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.READ && domain.getAuditKind() == SecQueryKind.READ;
            assert domain.getSchema() == null && domain.getFunc().equals("countDocuments");
        }
    }

    @Test
    public void testInsertOperations() {
        String sql = "db.collection.insert({name: 'John'}); db.collection.insertOne({name: 'Jane'}); db.collection.insertMany([{name: 'Bob'}, {name: 'Alice'}]);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/collection/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.collection.insert({name: 'John'})");
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.insertOne({name: 'Jane'})");
            assert splitScripts.get(1).getType() == SecQueryType.INSERT;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.insertMany([{name: 'Bob'}, {name: 'Alice'}])");
            assert splitScripts.get(2).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.INSERT && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("insert");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.INSERT && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("insertOne");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.INSERT && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("insertMany");
        }
    }

    @Test
    public void testUpdateOperations() {
        String sql = "db.collection.update({name: 'John'}, {$set: {age: 30}}); db.collection.updateOne({name: 'Jane'}, {$inc: {score: 5}}); db.collection.replaceOne({name: 'Bob'}, {name: 'Robert'});";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/collection/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.collection.update({name: 'John'}, {$set: {age: 30}})");
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.updateOne({name: 'Jane'}, {$inc: {score: 5}})");
            assert splitScripts.get(1).getType() == SecQueryType.UPDATE;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.replaceOne({name: 'Bob'}, {name: 'Robert'})");
            assert splitScripts.get(2).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.UPDATE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("update");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.UPDATE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("updateOne");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.UPDATE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("replaceOne");
        }
    }

    @Test
    public void testDeleteOperations() {
        String sql = "db.collection.deleteOne({name: 'John'}); db.collection.deleteMany({status: 'inactive'}); db.collection.findOneAndDelete({priority: 'low'});";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/collection/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.collection.deleteOne({name: 'John'})");
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.deleteMany({status: 'inactive'})");
            assert splitScripts.get(1).getType() == SecQueryType.DELETE;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.findOneAndDelete({priority: 'low'})");
            assert splitScripts.get(2).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DELETE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("deleteOne");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DELETE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("deleteMany");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.DELETE && domain.getAuditKind() == SecQueryKind.DML;
            assert domain.getSchema() == null && domain.getFunc().equals("findOneAndDelete");
        }
    }

    @Test
    public void testIndexOperations() {
        String sql = "db.collection.createIndex({name: 1}); db.collection.dropIndex({name: 1}); db.collection.dropIndexes();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/collection/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.collection.createIndex({name: 1})");
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.dropIndex({name: 1})");
            assert splitScripts.get(1).getType() == SecQueryType.DROP_INDEX;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.dropIndexes()");
            assert splitScripts.get(2).getType() == SecQueryType.DROP_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getSchema() == null && domain.getFunc().equals("createIndex");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getSchema() == null && domain.getFunc().equals("dropIndex");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getSchema() == null && domain.getFunc().equals("dropIndexes");
        }
    }

    @Test
    public void testCollectionOperations() {
        String sql = "db.createCollection('newCollection'); db.collection.drop(); db.collection.renameCollection('oldCollection');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/collection/");
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.createCollection('newCollection')");
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.collection.drop()");
            assert splitScripts.get(1).getType() == SecQueryType.DROP_TABLE;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.collection.renameCollection('oldCollection')");
            assert splitScripts.get(2).getType() == SecQueryType.RENAME_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getSchema() == null && domain.getFunc().equals("createCollection");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getSchema() == null && domain.getFunc().equals("drop");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.RENAME_TABLE && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getSchema() == null && domain.getFunc().equals("renameCollection");
        }
    }

    @Test
    public void testDatabaseOperations() {
        String sql = "use newDatabase; db.dropDatabase();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        {
            assert splitScripts.get(0).getScript().equals("use newDatabase");
            assert splitScripts.get(0).getType() == SecQueryType.SWITCH_SCHEMA;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.dropDatabase()");
            assert splitScripts.get(1).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SWITCH_SCHEMA && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getSchema() == null && domain.getFunc().equals("use");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getSchema() == null && domain.getFunc().equals("dropDatabase");
        }
    }

    @Test
    public void testAdminOperations() {
        String sql = "db.hostInfo(); db.serverStatus(); db.currentOp();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 3;
        {
            assert splitScripts.get(0).getScript().equals("db.hostInfo()");
            assert splitScripts.get(0).getType() == SecQueryType.ADMIN;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.serverStatus()");
            assert splitScripts.get(1).getType() == SecQueryType.ADMIN;
        }
        {
            assert splitScripts.get(2).getScript().equals("db.currentOp()");
            assert splitScripts.get(2).getType() == SecQueryType.ADMIN;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ADMIN && domain.getAuditKind() == SecQueryKind.ADMIN;
            assert domain.getSchema() == null && domain.getFunc().equals("hostInfo");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.ADMIN && domain.getAuditKind() == SecQueryKind.ADMIN;
            assert domain.getSchema() == null && domain.getFunc().equals("serverStatus");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.ADMIN && domain.getAuditKind() == SecQueryKind.ADMIN;
            assert domain.getSchema() == null && domain.getFunc().equals("currentOp");
        }
    }
}

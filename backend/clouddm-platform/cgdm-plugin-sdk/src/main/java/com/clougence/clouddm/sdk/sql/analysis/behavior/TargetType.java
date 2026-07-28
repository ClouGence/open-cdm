/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.sdk.sql.analysis.behavior;

import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;

@Getter
public enum TargetType {

    // common
    Unknown(null),
    Environment(null),
    Instance(UmiTypes.Instance),
    Machine(null),
    UserOrRole(null),
    User(UmiTypes.USER),
    Role(UmiTypes.ROLE),
    ConfigKey(null),
    File(null),

    Query(null),
    Update(null),
    Delete(null),
    Insert(null),
    Call(null),

    // rdb
    Catalog(UmiTypes.Catalog),
    Schema(UmiTypes.Schema),
    Table(UmiTypes.Table),
    View(UmiTypes.View),
    Materialized(UmiTypes.Materialized),
    Tablespace(UmiTypes.TABLESPACE),
    Column(UmiTypes.Column),
    Index(UmiTypes.Index),
    Constraint(null),
    Partition(UmiTypes.PARTITION),
    Sequence(UmiTypes.Sequence),
    ProgramObject(null),
    Function(UmiTypes.Function),
    Procedure(UmiTypes.Procedure),
    Trigger(UmiTypes.Trigger),
    Event(null),
    Synonym(UmiTypes.Synonym),
    Log(null),
    Policy(null),
    RowAccessPolicy(null),
    MaskingPolicy(null),
    RedactionPolicy(null),
    Job(null),
    Link(UmiTypes.DBLink),
    Package(null),
    Profile(null),
    Context(null),
    Queue(null),
    QueueSubscriber(null),
    Pipe(null),
    SchedulerObject(null),
    SchemaObject(null),
    Type(null),
    Operator(null),
    Library(null),
    ResourceGroup(null),
    Replication(null),
    PublicationSubscription(null),
    Publication(null),
    Subscription(null),
    Object(null),
    PrepareStatement(null),

    // cache
    Key(UmiTypes.Key),

    ;

    private final UmiTypes umiType;

    TargetType(UmiTypes umiType){
        this.umiType = umiType;
    }
}

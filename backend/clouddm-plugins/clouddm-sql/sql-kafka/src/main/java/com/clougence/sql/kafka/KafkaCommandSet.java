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
package com.clougence.sql.kafka;

import java.io.IOException;
import java.util.List;

import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.foramt.FmtWriter;

public class KafkaCommandSet implements StatementSet {
    private final List<Statement> statements;

    public KafkaCommandSet(List<KafkaCommand> commands){
        this.statements = List.copyOf(commands);
    }

    @Override
    public List<Statement> getStatements() { return statements; }

    @Override
    public void accept(Visitor visitor) {
        statements.forEach(statement -> statement.accept(visitor));
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        for (Statement statement : statements) {
            statement.doFormat(writer);
            writer.write(";\n");
        }
    }
}

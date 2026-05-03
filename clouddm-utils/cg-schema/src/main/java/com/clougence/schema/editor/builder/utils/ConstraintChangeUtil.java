package com.clougence.schema.editor.builder.utils;

import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.EConstraint;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstraintChangeUtil {

    public static void genChangeSql(List<EConstraint> sourceConstraints, List<EConstraint> targetConstraints, SqlBuilder provider, TriggerContext triggerContext,
                                    String sourceCatalog, String sourceSchema, String sourceName, List<Action> actions) {
        Map<String, EConstraint> sourceMap = sourceConstraints.stream().collect(Collectors.toMap(EConstraint::getName, EConstraint -> EConstraint));
        Map<String, EConstraint> targetMap = targetConstraints.stream().collect(Collectors.toMap(EConstraint::getName, index -> index));

        for (EConstraint eConstraint : sourceConstraints) {
            EConstraint targetEConstraint = targetMap.get(eConstraint.getName());
            if (!targetMap.containsKey(eConstraint.getName())) {
                // drop
                List<String> sqlDrops = provider.dropConstraint(triggerContext, sourceCatalog, sourceSchema, sourceName, eConstraint);
                actions.add(new Action(sqlDrops, sourceCatalog, sourceSchema, sourceName));
            } else if (eConstraint.testChanged(targetEConstraint)) {
                // rebuild
                List<String> sqlDrops = provider.dropConstraint(triggerContext, sourceCatalog, sourceSchema, sourceName, eConstraint);
                actions.add(new Action(sqlDrops, sourceCatalog, sourceSchema, sourceName));

                List<String> sqlCreate = provider.createConstraint(triggerContext, sourceCatalog, sourceSchema, sourceName, targetEConstraint);
                actions.add(new Action(sqlCreate, sourceCatalog, sourceSchema, sourceName));
            }
        }
        for (EConstraint eConstraint : targetConstraints) {
            if (!sourceMap.containsKey(eConstraint.getName())) {
                // add target
                List<String> sqlAddStrings = provider.createConstraint(triggerContext, sourceCatalog, sourceSchema, sourceName, eConstraint);
                actions.add(new Action(sqlAddStrings, sourceCatalog, sourceSchema, sourceName));
            }
        }
    }
}

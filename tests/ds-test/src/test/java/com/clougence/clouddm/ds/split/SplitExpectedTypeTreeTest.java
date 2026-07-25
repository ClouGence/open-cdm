package com.clougence.clouddm.ds.split;

import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

class SplitExpectedTypeTreeTest {

    @Test
    void shouldParseDescendantTypeSummary() {
        String value = "CREATE_PROG_OBJ|COMMENT_PROG_OBJ(BLOCK,SELECT,PROGRAM_CONTROL,INSERT,UPDATE,SESSION_VARIABLE_RW)";

        SplitTextTest.ExpectedTypeTree tree = SplitTextTest.ExpectedTypeTree.parse(value);

        Assertions.assertEquals(value, tree.toString());
        Assertions.assertEquals("CREATE_PROG_OBJ", tree.primaryType());
        Assertions.assertEquals(6, tree.children().size());
    }

    @Test
    void shouldFlattenAndDeduplicateRecursiveSplitScriptTypes() {
        SplitScript select = split(SecQueryType.SELECT);
        SplitScript update = split(SecQueryType.UPDATE);
        select.setChildren(List.of(update));
        SplitScript block = split(SecQueryType.BLOCK);
        block.setChildren(List.of(select, split(SecQueryType.SELECT)));

        SplitScript root = split(SecQueryType.CREATE_PROG_OBJ, SecQueryType.COMMENT_PROG_OBJ);
        root.setChildren(List.of(block));

        Assertions.assertEquals("CREATE_PROG_OBJ|COMMENT_PROG_OBJ(BLOCK,SELECT,UPDATE)",
                SplitTextTest.ExpectedTypeTree.from(root).toString());
    }

    @Test
    void shouldAllowNullTypeAndChildren() {
        SplitScript script = split(SecQueryType.SELECT);

        script.setType(null);
        Assertions.assertNull(script.getType());
        Assertions.assertEquals(SecQueryType.UNKNOWN, script.getPrimaryType());
        Assertions.assertNull(script.getChildren());
        script.setChildren(null);
        Assertions.assertNull(script.getChildren());
    }

    @Test
    void shouldRejectEmptyChildren() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> SplitTextTest.ExpectedTypeTree.parse("BLOCK()"));
    }

    @Test
    void shouldRejectNestedDescendantSummary() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> SplitTextTest.ExpectedTypeTree.parse("CREATE_PROG_OBJ(BLOCK(SELECT))"));
    }

    private static SplitScript split(SecQueryType... types) {
        SplitScript script = new SplitScript();
        script.setType(new LinkedHashSet<>(List.of(types)));
        return script;
    }
}

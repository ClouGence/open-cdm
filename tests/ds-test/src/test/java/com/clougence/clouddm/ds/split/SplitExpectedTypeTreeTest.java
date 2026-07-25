package com.clougence.clouddm.ds.split;

import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
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
        SplitScript select = split(SplitQueryType.SELECT);
        SplitScript update = split(SplitQueryType.UPDATE);
        select.setChildren(List.of(update));
        SplitScript block = split(SplitQueryType.BLOCK);
        block.setChildren(List.of(select, split(SplitQueryType.SELECT)));

        SplitScript root = split(SplitQueryType.CREATE_PROG_OBJ, SplitQueryType.COMMENT_PROG_OBJ);
        root.setChildren(List.of(block));

        Assertions.assertEquals("CREATE_PROG_OBJ|COMMENT_PROG_OBJ(BLOCK,SELECT,UPDATE)", SplitTextTest.ExpectedTypeTree.from(root).toString());
    }

    @Test
    void shouldAllowNullTypeAndChildren() {
        SplitScript script = split(SplitQueryType.SELECT);

        script.setType(null);
        Assertions.assertNull(script.getType());
        Assertions.assertEquals(SplitQueryType.UNKNOWN, script.getPrimaryType());
        Assertions.assertNull(script.getChildren());
        script.setChildren(null);
        Assertions.assertNull(script.getChildren());
    }

    @Test
    void shouldRejectEmptyChildren() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SplitTextTest.ExpectedTypeTree.parse("BLOCK()"));
    }

    @Test
    void shouldRejectNestedDescendantSummary() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SplitTextTest.ExpectedTypeTree.parse("CREATE_PROG_OBJ(BLOCK(SELECT))"));
    }

    private static SplitScript split(SplitQueryType... types) {
        SplitScript script = new SplitScript();
        script.setType(new LinkedHashSet<>(List.of(types)));
        return script;
    }
}

package com.clougence.clouddm.ds.split;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

public abstract class SplitTextTest {

    static final String DELIMITER_LONG  = "------------------------------------------------------------------------------------------";
    static final String DELIMITER_SHORT = "----------";

    @TestFactory
    public Stream<DynamicTest> splitScripts() {
        return dynamicTests();
    }

    protected Stream<DynamicTest> dynamicTests() {
        List<DynamicTest> tests = new ArrayList<>();
        for (Fixture fixture : fixtures()) {
            if (!accepts(fixture)) {
                continue;
            }
            SplitFixture splitFixture = loadFixtureUnchecked(fixture.resourcePath);
            SplitRun run = createRun(splitFixture, () -> splitAnalysisSpi(fixture));
            for (SplitCase splitCase : splitFixture.cases()) {
                tests.add(DynamicTest.dynamicTest(splitCase.displayName(), () -> verifyCase(splitCase, run, verifyAllTypes())));
            }
        }
        return tests.stream();
    }

    protected boolean verifyAllTypes() {
        return true;
    }

    protected boolean accepts(Fixture fixture) {
        return true;
    }

    protected SplitAnalysisSpi splitAnalysisSpi(Fixture fixture) {
        return SqlTestSupport.sqlEngine(fixture.datasource()).splitAnalysisSpi(SqlParserParameters.empty());
    }

    protected void splitRejectedCase(String resourcePath, String datasource, int splitIndex) throws Exception {
        String rejectedSql = rejectedScript(resourcePath, splitIndex);
        splitAnalysisSpi(new Fixture(resourcePath, datasource)).splitScript(rejectedSql, null, 0, 0);
    }

    protected final String rejectedScript(String resourcePath, int splitIndex) throws IOException {
        SplitFixture fixture = loadFixture(resourcePath);
        Assert.assertTrue("reject split index missing: " + resourcePath + " index " + splitIndex,
            splitIndex < fixture.expected().size());
        String rejectedSql = fixture.expected().get(splitIndex).script();
        Assert.assertFalse("empty rejected SQL: " + resourcePath + " index " + splitIndex, rejectedSql.isBlank());
        return rejectedSql;
    }

    protected final Stream<DynamicTest> rejectedDynamicTests(List<String> resourcePaths, String datasource) {
        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : resourcePaths) {
            SplitFixture fixture = loadFixtureUnchecked(resourcePath);
            for (SplitCase splitCase : fixture.cases()) {
                tests.add(DynamicTest.dynamicTest(splitCase.displayName(), () -> {
                    Assertions.assertThrows(RuntimeException.class,
                        () -> splitRejectedCase(resourcePath, datasource, splitCase.splitIndex()));
                }));
            }
        }
        return tests.stream();
    }

    static SplitFixture loadFixture(String resourcePath) throws IOException {
        String content = TextCaseSupport.readResource(resourcePath);
        int splitIndex = content.indexOf(DELIMITER_LONG);
        Assert.assertTrue("invalid split fixture format: " + resourcePath, splitIndex >= 0);

        String inputSql = content.substring(0, splitIndex);
        String expectedPart = content.substring(splitIndex + DELIMITER_LONG.length());
        return new SplitFixture(resourcePath, inputSql, parseExpected(expectedPart));
    }

    static SplitRun createRun(SplitFixture fixture, Supplier<SplitAnalysisSpi> spiFactory) {
        return new SplitRun(fixture.resourcePath(), fixture.expected().size(), spiFactory);
    }

    static void verifyCase(SplitCase splitCase, SplitRun run, boolean verifyAllTypes) {
        try {
            SplitResult result = run.result();
            SplitFixture fixture = result.fixture();
            List<ExpectedSplit> expected = fixture.expected();
            List<ExpectedSplit> actual = result.actual();

            Assert.assertEquals("split count mismatch: " + fixture.resourcePath(), expected.size(), actual.size());
            int index = splitCase.splitIndex();
            Assert.assertTrue("split index missing: " + fixture.resourcePath() + " index " + index, index < actual.size());
            ExpectedTypeTree expectedType = expected.get(index).type;
            ExpectedTypeTree actualType = actual.get(index).type;
            if (verifyAllTypes || expectedType.hasMultipleTypes() || expectedType.hasChildren()) {
                Assert.assertEquals("split type mismatch: " + fixture.resourcePath() + " index " + index, expectedType, actualType);
            } else {
                Assert.assertEquals("split primary type mismatch: " + fixture.resourcePath() + " index " + index,
                        expectedType.primaryType(), actualType.primaryType());
            }
            Assert.assertEquals("split script mismatch: " + fixture.resourcePath() + " index " + index, expected.get(index).script, actual.get(index).script);
        } finally {
            run.complete();
        }
    }

    private static List<ExpectedSplit> parseExpected(String expectedPart) {
        List<ExpectedSplit> result = new ArrayList<>();
        for (String block : expectedBlocks(expectedPart)) {
            String normalized = block.strip();
            if (normalized.isEmpty()) {
                continue;
            }
            result.add(parseExpectedBlock(normalized));
        }
        return result;
    }

    private static List<String> expectedBlocks(String expectedPart) {
        List<String> blocks = new ArrayList<>();
        StringBuilder block = new StringBuilder();
        for (String line : expectedPart.split("\\n", -1)) {
            if (DELIMITER_SHORT.equals(line.strip())) {
                addExpectedBlock(blocks, block);
                block.setLength(0);
                continue;
            }
            block.append(line).append('\n');
        }
        addExpectedBlock(blocks, block);
        return blocks;
    }

    private static void addExpectedBlock(List<String> blocks, StringBuilder block) {
        String value = block.toString().strip();
        if (!value.isEmpty()) {
            blocks.add(value);
        }
    }

    private static ExpectedSplit parseExpectedBlock(String block) {
        Assert.assertTrue("invalid expected split block: " + block, block.startsWith("["));
        int typeEnd = block.indexOf(']');
        Assert.assertTrue("invalid expected split block: " + block, typeEnd > 1);
        String type = block.substring(1, typeEnd).trim();
        String script = block.substring(typeEnd + 1).strip();
        Assert.assertFalse("empty expected split type: " + block, type.isEmpty());
        return new ExpectedSplit(ExpectedTypeTree.parse(type), script);
    }

    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles("split");
    }

    private List<Fixture> fixtures() {
        return fixtureResources().stream().map(SplitTextTest::toFixture).toList();
    }

    private static Fixture toFixture(String resourcePath) {
        String relative = resourcePath.substring("split/".length());
        String datasource = relative.substring(0, relative.indexOf('/'));
        return new Fixture(resourcePath, datasource);
    }

    private static SplitFixture loadFixtureUnchecked(String resourcePath) {
        try {
            return loadFixture(resourcePath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load split fixture: " + resourcePath, e);
        }
    }

    protected record Fixture(String resourcePath, String datasource) {
    }

    record SplitFixture(String resourcePath, String inputSql, List<ExpectedSplit> expected) {

        List<SplitCase> cases() {
            List<SplitCase> cases = new ArrayList<>();
            for (int i = 0; i < expected.size(); i++) {
                ExpectedSplit split = expected.get(i);
                cases.add(new SplitCase(resourcePath, i, split.type, summarize(split.script)));
            }
            return cases;
        }
    }

    static final class SplitCase extends TextTestCase {

        private final int              splitIndex;
        private final ExpectedTypeTree type;
        private final String           summary;

        SplitCase(String resourcePath, int index, ExpectedTypeTree type, String summary){
            super(resourcePath, String.format("%03d", index + 1), index + 1);
            this.splitIndex = index;
            this.type = type;
            this.summary = summary;
        }

        int splitIndex() {
            return splitIndex;
        }

        String displayName() {
            return caseId() + " [" + type + "] " + summary;
        }
    }

    static final class SplitRun {

        private final String                     resourcePath;
        private final Supplier<SplitAnalysisSpi> spiFactory;
        private int                              remaining;
        private SplitResult                      result;
        private Throwable                        failure;

        private SplitRun(String resourcePath, int caseCount, Supplier<SplitAnalysisSpi> spiFactory){
            this.resourcePath = resourcePath;
            this.remaining = caseCount;
            this.spiFactory = spiFactory;
        }

        synchronized SplitResult result() {
            if (failure != null) {
                throw new AssertionError("failed to split fixture: " + resourcePath, failure);
            }
            if (result == null) {
                try {
                    SplitFixture fixture = loadFixture(resourcePath);
                    List<SplitScript> scripts = spiFactory.get().splitScript(fixture.inputSql(), null, 0, 0);
                    scripts.forEach(SplitTextTest::verifySplitTree);
                    List<ExpectedSplit> actual = scripts.stream().map(ExpectedSplit::from).toList();
                    result = new SplitResult(fixture, actual);
                } catch (IOException | RuntimeException e) {
                    failure = e;
                    throw new AssertionError("failed to split fixture: " + resourcePath, e);
                }
            }
            return result;
        }

        synchronized void complete() {
            remaining--;
            if (remaining == 0) {
                result = null;
                failure = null;
            }
        }
    }

    private static void verifySplitTree(SplitScript parent) {
        Assert.assertNotNull("split script must not be null", parent.getScript());
        Assert.assertFalse("split script must not be blank", parent.getScript().isBlank());
        Assert.assertNotNull("split type must not be null", parent.getType());
        Assert.assertFalse("split type must not be empty: " + parent.getScript(), parent.getType().isEmpty());

        List<SplitScript> children = parent.getChildren();
        if (children == null) {
            return;
        }
        for (SplitScript child : children) {
            Assert.assertTrue("child is not part of parent: " + child.getScript(),
                    parent.getScript().contains(child.getScript()));
            Assert.assertTrue("child starts before parent: " + child.getScript(),
                    child.getBodyStartCodeLine() >= parent.getBodyStartCodeLine());
            Assert.assertTrue("child ends before it starts: " + child.getScript(),
                    child.getBodyEndCodeLine() >= child.getBodyStartCodeLine());
            verifySplitTree(child);
        }
    }

    record SplitResult(SplitFixture fixture, List<ExpectedSplit> actual) {
    }

    private record ExpectedSplit(ExpectedTypeTree type, String script) {

        static ExpectedSplit from(SplitScript script) {
            return new ExpectedSplit(ExpectedTypeTree.from(script), script.getScript().strip());
        }
    }

    record ExpectedTypeTree(List<String> types, List<ExpectedTypeTree> children) {

        ExpectedTypeTree {
            types = List.copyOf(types);
            children = List.copyOf(children);
            if (types.isEmpty()) {
                throw new IllegalArgumentException("empty split type");
            }
        }

        static ExpectedTypeTree parse(String value) {
            TypeTreeParser parser = new TypeTreeParser(value);
            ExpectedTypeTree result = parser.parseNode();
            parser.requireEnd();
            for (ExpectedTypeTree child : result.children) {
                if (child.types.size() != 1 || !child.children.isEmpty()) {
                    throw new IllegalArgumentException(
                            "split descendant summary must contain one flat type per child: " + value);
                }
            }
            return result;
        }

        static ExpectedTypeTree from(SplitScript script) {
            List<String> types = script.getType().stream().map(Enum::name).toList();
            LinkedHashSet<String> descendantTypes = new LinkedHashSet<>();
            collectDescendantTypes(script, descendantTypes);
            List<ExpectedTypeTree> children = descendantTypes.stream()
                    .map(type -> new ExpectedTypeTree(List.of(type), List.of()))
                    .toList();
            return new ExpectedTypeTree(types, children);
        }

        private static void collectDescendantTypes(SplitScript script, LinkedHashSet<String> result) {
            List<SplitScript> children = script.getChildren();
            if (children == null) {
                return;
            }
            for (SplitScript child : children) {
                child.getType().stream().map(Enum::name).forEach(result::add);
                collectDescendantTypes(child, result);
            }
        }

        boolean hasMultipleTypes() {
            return this.types.size() > 1;
        }

        boolean hasChildren() {
            return !this.children.isEmpty();
        }

        String primaryType() {
            return this.types.get(0);
        }

        @Override
        public String toString() {
            String value = String.join("|", this.types);
            if (this.children.isEmpty()) {
                return value;
            }
            return value + "(" + this.children.stream().map(ExpectedTypeTree::toString).collect(Collectors.joining(",")) + ")";
        }
    }

    private static final class TypeTreeParser {

        private final String value;
        private int          index;

        private TypeTreeParser(String value){
            this.value = value;
        }

        private ExpectedTypeTree parseNode() {
            List<String> types = new ArrayList<>();
            types.add(parseType());
            while (consume('|')) {
                types.add(parseType());
            }

            List<ExpectedTypeTree> children = new ArrayList<>();
            if (consume('(')) {
                children.add(parseNode());
                while (consume(',')) {
                    children.add(parseNode());
                }
                require(')');
            }
            return new ExpectedTypeTree(types, children);
        }

        private String parseType() {
            skipWhitespace();
            int start = this.index;
            while (this.index < this.value.length()) {
                char current = this.value.charAt(this.index);
                if (current == '|' || current == '(' || current == ')' || current == ',' || Character.isWhitespace(current)) {
                    break;
                }
                this.index++;
            }
            if (start == this.index) {
                throw new IllegalArgumentException("missing split type at index " + this.index + ": " + this.value);
            }
            String type = this.value.substring(start, this.index);
            skipWhitespace();
            return type;
        }

        private boolean consume(char expected) {
            skipWhitespace();
            if (this.index >= this.value.length() || this.value.charAt(this.index) != expected) {
                return false;
            }
            this.index++;
            return true;
        }

        private void require(char expected) {
            if (!consume(expected)) {
                throw new IllegalArgumentException("expected '" + expected + "' at index " + this.index + ": " + this.value);
            }
        }

        private void requireEnd() {
            skipWhitespace();
            if (this.index != this.value.length()) {
                throw new IllegalArgumentException("unexpected split type content at index " + this.index + ": " + this.value);
            }
        }

        private void skipWhitespace() {
            while (this.index < this.value.length() && Character.isWhitespace(this.value.charAt(this.index))) {
                this.index++;
            }
        }
    }

    private static String summarize(String script) {
        String text = script.replaceAll("\\s+", " ").strip();
        if (text.length() <= 120) {
            return text;
        }
        return text.substring(0, 117) + "...";
    }

}

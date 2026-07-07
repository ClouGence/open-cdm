/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.language.completion;

import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class CompletionScriptTest {

    @TestFactory
    Collection<DynamicTest> completionScripts() {
        return new CompletionTestRunner().tests();
    }
}

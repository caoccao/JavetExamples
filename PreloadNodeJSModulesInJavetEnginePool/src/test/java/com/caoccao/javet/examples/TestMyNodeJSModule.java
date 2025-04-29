package com.caoccao.javet.examples;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestMyNodeJSModule {
    @Test
    public void testModuleExistence() throws IOException {
        for (var module : MyNodeJSModule.values()) {
            assertNotNull(module.getSourceCode());
        }
    }
}

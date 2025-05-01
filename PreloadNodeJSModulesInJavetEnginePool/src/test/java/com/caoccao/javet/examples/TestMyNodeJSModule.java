/*
 * Copyright (c) 2025. Sam Cao caoccao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caoccao.javet.examples;

import com.caoccao.javet.entities.JavetEntityError;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyNodeJSModule {
    protected V8Runtime v8Runtime;

    @AfterEach
    public void afterEach() throws JavetException {
        v8Runtime.lowMemoryNotification();
        assertEquals(
                v8Runtime.getV8ModuleCount(),
                v8Runtime.getReferenceCount(),
                "Reference count should be the same as module count.");
        v8Runtime.close();
    }

    @BeforeEach
    public void beforeEach() throws JavetException {
        v8Runtime = V8Host.getV8Instance().createV8Runtime();
        v8Runtime.setV8ModuleResolver(new MyV8ModuleResolver());
        v8Runtime.setPromiseRejectCallback((event, promise, value) -> {
            try {
                JavetEntityError javetEntityError = v8Runtime.toObject(value);
                fail(javetEntityError.getMessage());
            } catch (JavetException e) {
                fail(e);
            }
        });
    }

    @Test
    public void testDecimalJS() throws JavetException {
        v8Runtime.getExecutor("""
                import { Decimal } from 'npm/decimal.js';
                globalThis.result = new Decimal('1.23456789').toString();
                """).setModule(true).executeVoid();
        assertEquals("1.23456789", v8Runtime.getGlobalObject().getString("result"));
    }

    @Test
    public void testJSONata() throws JavetException {
        v8Runtime.getExecutor("""
                import { jsonata } from 'npm/jsonata';
                const data = { "a": "b" };
                const expr = await jsonata("a");
                globalThis.result = await expr.evaluate(data);
                """).setModule(true).executeVoid();
        assertEquals("b", v8Runtime.getGlobalObject().getString("result"));
    }

    @Test
    public void testModuleExistence() throws IOException {
        for (var module : MyNodeJSModule.values()) {
            assertNotNull(module.getSourceCode());
        }
    }
}

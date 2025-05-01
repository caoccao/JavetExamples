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

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.reference.V8ValuePromise;

public class Main {
    private static void testDecimalJS(V8Runtime v8Runtime) throws JavetException {
        v8Runtime.getExecutor("""
                import { Decimal } from 'npm/decimal.js';
                function test() {
                    return new Decimal('1.23456789').toString();
                }
                globalThis.test = test;
                """).setModule(true).executeVoid();
        if ("1.23456789".equals(v8Runtime.getGlobalObject().invokeString("test"))) {
            System.out.println("Test Decimal.js passed.");
        } else {
            System.err.println("Test Decimal.js failed.");
        }
    }

    private static void testJsonata(V8Runtime v8Runtime) throws JavetException {
        v8Runtime.getExecutor("""
                import { jsonata } from 'npm/jsonata';
                async function test() {
                    const data = { "hello": "world" };
                    const expr = await jsonata("hello");
                    const result = await expr.evaluate(data);
                    return result;
                }
                globalThis.test = test;
                """).setModule(true).executeVoid();
        try (V8ValuePromise v8ValuePromise = v8Runtime.getGlobalObject().invoke("test")) {
            if ("world".equals(v8ValuePromise.getResultString())) {
                System.out.println("Test JSONata passed.");
            } else {
                System.err.println("Test JSONata failed.");
            }
        }
    }

    public static void main(String[] args) throws JavetException {
        try (var myJavetEnginePool = new MyJavetEnginePool()) {
            try (var myJavetEngine = myJavetEnginePool.getEngine()) {
                V8Runtime v8Runtime = myJavetEngine.getV8Runtime();
                testDecimalJS(v8Runtime);
                testJsonata(v8Runtime);
            }
        }
    }
}
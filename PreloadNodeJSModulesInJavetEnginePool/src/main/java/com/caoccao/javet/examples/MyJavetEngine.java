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
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.interop.engine.JavetEngine;

public class MyJavetEngine extends JavetEngine<V8Runtime> {
    protected JavetStandardConsoleInterceptor consoleInterceptor;

    public MyJavetEngine(IJavetEnginePool<V8Runtime> iJavetEnginePool, V8Runtime v8Runtime) throws JavetException {
        super(iJavetEnginePool, v8Runtime);
        // Register console interceptor.
        consoleInterceptor = new JavetStandardConsoleInterceptor(v8Runtime);
        consoleInterceptor.register(v8Runtime.getGlobalObject());
        v8Runtime.setV8ModuleResolver(new MyV8ModuleResolver());
        v8Runtime.setPromiseRejectCallback((event, promise, value) -> {
            try {
                JavetEntityError javetEntityError = v8Runtime.toObject(value);
                getConfig().getJavetLogger().logError(javetEntityError.getMessage());
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void close(boolean forceClose) throws JavetException {
        if (forceClose) {
            // Unregister console interceptor.
            consoleInterceptor.unregister(v8Runtime.getGlobalObject());
            // Free memory.
            v8Runtime.lowMemoryNotification();
            consoleInterceptor = null;
        }
        super.close(forceClose);
    }
}

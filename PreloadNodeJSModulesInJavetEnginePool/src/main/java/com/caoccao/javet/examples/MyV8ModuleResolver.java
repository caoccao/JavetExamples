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
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.values.reference.IV8Module;

import java.io.IOException;

public class MyV8ModuleResolver implements IV8ModuleResolver {
    protected static final String GLOBAL = "global";

    @Override
    public IV8Module resolve(
            V8Runtime v8Runtime,
            String resourceName,
            IV8Module v8ModuleReferrer) throws JavetException {
        MyNodeJSModule module = MyNodeJSModule.of(resourceName);
        if (module != null) {
            try {
                String sourceCode = module.getSourceCode();
                switch (module.getType()) {
                    case CMD -> {
                        var globalObject = v8Runtime.getGlobalObject();
                        try (var v8ValueObject = v8Runtime.createV8ValueObject()) {
                            globalObject.set(GLOBAL, v8ValueObject);
                            v8Runtime.getExecutor(sourceCode).executeVoid();
                            return v8Runtime.createV8Module(module.getModuleName(), v8ValueObject);
                        } finally {
                            globalObject.delete(GLOBAL);
                        }
                    }
                    case ESM -> {
                        return v8Runtime.getExecutor(sourceCode)
                                .setResourceName(module.getModuleName())
                                .compileV8Module();
                    }
                    default -> v8Runtime.getLogger().logWarn("Unknown module type " + module.getType().name());
                }
            } catch (IOException e) {
                v8Runtime.getLogger().logError(e, "Failed to load module '" + module.getModuleName() + "'");
            }
        }
        return null;
    }
}

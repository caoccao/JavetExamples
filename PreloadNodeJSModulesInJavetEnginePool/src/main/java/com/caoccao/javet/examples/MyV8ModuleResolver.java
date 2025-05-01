package com.caoccao.javet.examples;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.values.reference.IV8Module;

import java.io.IOException;

public class MyV8ModuleResolver implements IV8ModuleResolver {
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
                            try {
                                globalObject.set("global", v8ValueObject);
                                v8Runtime.getExecutor(sourceCode).executeVoid();
                                return v8Runtime.createV8Module(module.getModuleName(), v8ValueObject);
                            } finally {
                                globalObject.delete("global");
                            }
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
                v8Runtime.getLogger().logError(e, "Failed to load module " + module.getModuleName());
            }
        }
        return null;
    }
}

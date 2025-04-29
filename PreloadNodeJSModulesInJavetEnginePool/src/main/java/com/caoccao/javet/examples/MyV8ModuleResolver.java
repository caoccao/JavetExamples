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
            var globalObject = v8Runtime.getGlobalObject();
            try (var v8ValueObject = v8Runtime.createV8ValueObject()) {
                try {
                    globalObject.set("global", v8ValueObject);
                    String sourceCode = module.getSourceCode();
                    v8Runtime.getExecutor(sourceCode).executeVoid();
                    return v8Runtime.createV8Module(module.getModuleName(), v8ValueObject);
                } catch (IOException e) {
                    v8Runtime.getLogger().logError(e, "Failed to load module " + module.getModuleName());
                } finally {
                    globalObject.delete("global");
                }
            }
        }
        return null;
    }
}

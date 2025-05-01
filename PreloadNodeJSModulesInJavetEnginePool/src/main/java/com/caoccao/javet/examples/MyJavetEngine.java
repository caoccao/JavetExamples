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

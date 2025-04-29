package com.caoccao.javet.examples;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.engine.JavetEngine;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.interop.options.V8RuntimeOptions;

public class MyJavetEnginePool extends JavetEnginePool<V8Runtime> {
    @Override
    protected JavetEngine<V8Runtime> createEngine() throws JavetException {
        V8RuntimeOptions v8RuntimeOptions = new V8RuntimeOptions();
        V8Runtime v8Runtime = V8Host.getV8Instance().createV8Runtime(true, v8RuntimeOptions);
        v8Runtime.allowEval(config.isAllowEval());
        v8Runtime.setLogger(config.getJavetLogger());
        return new MyJavetEngine(this, v8Runtime);
    }
}

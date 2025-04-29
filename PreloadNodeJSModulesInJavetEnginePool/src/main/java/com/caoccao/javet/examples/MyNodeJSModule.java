package com.caoccao.javet.examples;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MyNodeJSModule {
    JSONata("jsonata", "jsonata.min.js"),
    ;

    private static final Map<String, MyNodeJSModule> moduleMap = Stream.of(MyNodeJSModule.values())
            .collect(Collectors.toMap(MyNodeJSModule::getModuleName, Function.identity()));

    private final String fileName;
    private final String name;
    private final String moduleName;

    MyNodeJSModule(String name, String fileName) {
        this.fileName = Objects.requireNonNull(fileName);
        this.moduleName = "npm/" + Objects.requireNonNull(name);
        this.name = Objects.requireNonNull(name);
    }

    public static MyNodeJSModule of(String moduleName) {
        return moduleMap.get(moduleName);
    }

    public String getFileName() {
        return fileName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getName() {
        return name;
    }

    public String getSourceCode() throws IOException {
        String resourcePath = "/" + name + "/" + fileName;
        return IOUtils.resourceToString(resourcePath, StandardCharsets.UTF_8);
    }
}

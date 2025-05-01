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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MyNodeJSModule {
    DecimalJS("decimal.js", "decimal.mjs", MyNodeJSModuleType.ESM),
    JSONata("jsonata", "jsonata.min.js", MyNodeJSModuleType.CMD),
    ;

    private static final Map<String, MyNodeJSModule> moduleMap = Stream.of(MyNodeJSModule.values())
            .collect(Collectors.toMap(MyNodeJSModule::getModuleName, Function.identity()));

    private final String fileName;
    private final String name;
    private final String moduleName;
    private final MyNodeJSModuleType type;

    MyNodeJSModule(String name, String fileName, MyNodeJSModuleType type) {
        this.fileName = Objects.requireNonNull(fileName);
        this.moduleName = "npm/" + Objects.requireNonNull(name);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
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

    public MyNodeJSModuleType getType() {
        return type;
    }

    public String getSourceCode() throws IOException {
        String resourcePath = "/" + name + "/" + fileName;
        return IOUtils.resourceToString(resourcePath, StandardCharsets.UTF_8);
    }
}

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

const fs = require('node:fs');
const path = require('node:path');

const libraries = {
    'decimal.js': [
        'node_modules/decimal.js/decimal.mjs',
    ],
    'jsonata': [
        'node_modules/jsonata/jsonata.min.js',
    ],
};

// Validate
Object.entries(libraries).forEach(([libraryName, sourceFilePaths]) => {
    sourceFilePaths.forEach((sourceFilePath) => {
        if (!fs.existsSync(sourceFilePath)) {
            console.error(`File not found: ${sourceFilePath}`);
            process.exit(1);
        }
    })
});

// Copy
Object.entries(libraries).forEach(([libraryName, sourceFilePaths]) => {
    sourceFilePaths.forEach((sourceFilePath) => {
        const sourceFileName = path.basename(sourceFilePath);
        const targetDirPath = path.join("src", "main", "resources", libraryName);
        const targetFilePath = path.join(targetDirPath, sourceFileName);
        if (fs.existsSync(targetFilePath)) {
            console.info(`File already exists: ${targetFilePath}`);
        } else {
            console.info(`Copy from ${sourceFilePath} to ${targetFilePath}`);
            if (!fs.existsSync(targetDirPath)) {
                fs.mkdirSync(targetDirPath, { recursive: true });
            }
            fs.copyFileSync(sourceFilePath, targetFilePath);
        }
    })
});

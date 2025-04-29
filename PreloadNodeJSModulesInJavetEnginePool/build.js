const fs = require('fs');
const path = require('path');

const libraries = {
    'jsonata': [
        'node_modules/jsonata/jsonata.min.js',
    ]
};

// Validation
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
        console.info(`Copy from ${sourceFilePath} to ${targetFilePath}`);
        if (!fs.existsSync(targetDirPath)) {
            fs.mkdirSync(targetDirPath, { recursive: true });
        }
        fs.copyFileSync(sourceFilePath, targetFilePath);
    })
});

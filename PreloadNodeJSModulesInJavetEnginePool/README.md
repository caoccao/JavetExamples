# Preload Node.js Modules in Javet Engine Pool

It is quite common to load Node.js modules in the Javet Engine Pool under the V8 mode. This project demonstrates how to do that.

## Prerequisites

- JDK 17+
- Gradle 8+
- Node.js 22+
- Pnpm 10+

## Build and Run

```shell
pnpm install
pnpm run copy
gradle build
jar -jar build/libs/PreloadNodeJSModulesInJavetEnginePool.jar
```

## License

[APACHE LICENSE, VERSION 2.0](../LICENSE)

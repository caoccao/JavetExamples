import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
}

group = "com.caoccao.javet.examples"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val os = OperatingSystem.current()
    val arch = System.getProperty("os.arch")
    val isI18n = false
    val isNode = false
    val i18nType = if (isI18n) "-i18n" else ""
    val jsRuntimeTimeType = if (isNode) "node" else "v8"
    val osType = if (os.isWindows) "windows" else
        if (os.isMacOsX) "macos" else
            if (os.isLinux) "linux" else ""
    val archType = if (arch == "aarch64" || arch == "arm64") "arm64" else "x86_64"
    val javetVersion = "4.1.3"

    implementation("com.caoccao.javet:javet:$javetVersion")
    implementation("com.caoccao.javet:javet-$jsRuntimeTimeType-$osType-$archType$i18nType:$javetVersion")

    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.19.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "${project.group}.Main"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.test {
    useJUnitPlatform()
}
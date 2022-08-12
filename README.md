# True Sight

📝 [Burp Suite](https://portswigger.net/burp) extension providing auditors with collaborative documentation capabilities.

## Features

This extension provides a way for auditors to **document HTTP messages** as well as quality-of-life enhancements to facilitate the reverse-engineering process such as **workflows** and **variable name substitutions**.

When running on a non-temporary project, the extension saves its data as JSON text files. This lets version control systems such as Git easily keep track of changes and thus provides a way for auditors to **share and collaborate on projects**.

## Manual

In order to understand how to use this extension, please refer to the **manual** present at the **root of this repository**.

## Dependencies

This extension depends on the [Burp Extender API](https://mvnrepository.com/artifact/net.portswigger.burp.extender/burp-extender-api), [JSON In Java](https://mvnrepository.com/artifact/org.json/json) and the [Kotlin Stdlib Jdk8](https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8).

## Building

This project uses [Gradle](https://gradle.org) as its build system.

In order to produce a JAR file that can be imported in Burp, simply run `gradlew shadowJar` from the root of this repository.

The output JAR file will be located under `build/libs/truesight-all.jar`.
